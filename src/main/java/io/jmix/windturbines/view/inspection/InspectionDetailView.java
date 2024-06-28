package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import de.f0rce.signaturepad.SignaturePad;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.Messages;
import io.jmix.core.TimeSource;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.accordion.JmixAccordion;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.radiobuttongroup.JmixRadioButtonGroup;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.inspection.*;
import io.jmix.windturbines.view.inspectionrecommendation.InspectionRecommendationDetailView;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Route(value = "inspections/:id", layout = MainView.class)
@ViewController("Inspection.detail")
@ViewDescriptor("inspection-detail-view.xml")
@EditedEntityContainer("inspectionDc")
public class InspectionDetailView extends StandardDetailView<Inspection> {
    public static final int LAST_SECTION_INDEX = 4;

    @Autowired
    private FileStorage fileStorage;
    @ViewComponent
    private JmixRadioButtonGroup<Integer> generatorCheckAnswerField;
    @ViewComponent
    private JmixSelect<RotorBladesAnswer> rotorBladesAnswerField;
    @ViewComponent
    private JmixSelect<GearboxOilLevelAnswer> gearboxOilLevelAnswerField;
    @ViewComponent
    private JmixSelect<YesNoAnswer> controlSystemStatusField;
    @ViewComponent
    private JmixAccordion mainAccordion;
    @ViewComponent
    private JmixButton finishBtn;
    @ViewComponent
    private JmixButton nextBtn;
    @ViewComponent
    private JmixButton prevBtn;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private CollectionPropertyContainer<InspectionFinding> findingsDc;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private Dialogs dialogs;
    @ViewComponent
    private JmixButton createFindingBtn;
    @ViewComponent
    private CollectionPropertyContainer<InspectionRecommendation> recommendationsDc;
    @ViewComponent
    private VerticalLayout signaturePadWrapper;
    private SignaturePad signature;
    @ViewComponent
    private JmixImage<FileRef> storedSignatureImage;
    @Autowired
    private TimeSource timeSource;
    @ViewComponent
    private TypedDateTimePicker<OffsetDateTime> operatorRepSignedAtField;
    @ViewComponent
    private JmixButton createRecommendationBtn;
    @Autowired
    private ViewValidation viewValidation;
    @ViewComponent
    private JmixCheckbox operatorConfirmationField;
    @ViewComponent
    private TypedTextField<String> operatorRepNameField;


    @Subscribe
    public void onInit(final InitEvent event) {
        generatorCheckAnswerField.setItems(List.of(1, 2, 3, 4, 5));
        openAccordionPanel(0);
        signature = new SignaturePad();
        signature.setWidthFull();
        signature.setHeight("200px");
        signaturePadWrapper.add(signature);
    }

    @Subscribe
    public void onValidation(final ValidationEvent event) {
        if (signature.isEmpty()) {
            ValidationErrors errors = ValidationErrors.of(messages.getMessage("operatorRepSignature.empty"));
            event.addErrors(errors);
        }
        if (!operatorConfirmationField.getValue()) {
            ValidationErrors errors = ValidationErrors.of(messages.getMessage("operatorConfirmation.required"));
            event.addErrors(errors);
        }
    }


    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) throws IOException {
        getEditedEntity().setOperatorRepSignature(
                saveSignature(signature.getImageBase64())
        );
        getEditedEntity().setOperatorRepSignedAt(timeSource.now().toOffsetDateTime());
    }

    private FileRef saveSignature(byte[] signatureImageBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(signatureImageBytes)) {
            return fileStorage.saveStream("inspection-operator-signature-%s.png".formatted(getEditedEntity().getId()), inputStream);
        }
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        createFindingBtn.setEnabled(!isReadOnly());
        createRecommendationBtn.setEnabled(!isReadOnly());
        finishBtn.setEnabled(!isReadOnly());
        signaturePadWrapper.setVisible(!isReadOnly());

        storedSignatureImage.setVisible(isReadOnly());
        operatorRepSignedAtField.setVisible(isReadOnly());

        generatorCheckAnswerField.setRequired(!isReadOnly());
        rotorBladesAnswerField.setRequired(!isReadOnly());
        gearboxOilLevelAnswerField.setRequired(!isReadOnly());
        controlSystemStatusField.setRequired(!isReadOnly());
        operatorRepNameField.setRequired(!isReadOnly());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Wizard behaviour
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe("prevAction")
    public void onPrevAction(final ActionPerformedEvent event) {
        if (currentAccordionIndex() > 0) {
            openAccordionPanel(currentAccordionIndex() - 1);
        }
    }

    @Subscribe("nextAction")
    public void onNextAction(final ActionPerformedEvent event) {
        if (currentAccordionIndex() < LAST_SECTION_INDEX) {
            openAccordionPanel(currentAccordionIndex() + 1);
        }
    }


    @Subscribe("mainAccordion")
    public void onMainAccordionOpenedChange(final Accordion.OpenedChangeEvent event) {
        refreshWizardButtonVisibility();
    }

    private void openAccordionPanel(int index) {
        mainAccordion.open(index);
        refreshWizardButtonVisibility();
    }

    private void refreshWizardButtonVisibility() {
        finishBtn.setVisible(currentAccordionIndex() == LAST_SECTION_INDEX);
        nextBtn.setVisible(currentAccordionIndex() != LAST_SECTION_INDEX);
        prevBtn.setEnabled(currentAccordionIndex() > 0);
    }

    private int currentAccordionIndex() {
        return mainAccordion.getOpenedIndex().orElse(-1);
    }

    @Subscribe("finishAction")
    public void onFinishAction(final ActionPerformedEvent event) {

        ValidationErrors validationErrors = validateView();
        if (!validationErrors.isEmpty()) {
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }

        completeInspection();
    }

    protected void completeInspection() {
        dialogs.createOptionDialog()
                .withHeader(messages.getMessage("finishConfirmation.header"))
                .withText(messages.getMessage("finishConfirmation.text"))
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withHandler(e -> {
                                    getEditedEntity().setTaskStatus(TaskStatus.COMPLETED);
                                    closeWithSave();
                                })
                                .withVariant(ActionVariant.PRIMARY),
                        new DialogAction(DialogAction.Type.NO)
                )
                .open();
    }

    @Supply(to = "findingsVirtualList", subject = "renderer")
    private Renderer<InspectionFinding> findingsVirtualListRenderer() {
        //noinspection JmixIncorrectCreateGuiComponent
        return new ComponentRenderer<>(finding ->
                new InspectionFindingCard(
                        finding,
                        uiComponents,
                        dialogs,
                        dataContext,
                        dialogWindows,
                        messages,
                        findingsDc,
                        recommendationsDc,
                        this,
                        isReadOnly()
                ));
    }


    @Supply(to = "recommendationsVirtualList", subject = "renderer")
    private Renderer<InspectionRecommendation> recommendationsVirtualListRenderer() {
        //noinspection JmixIncorrectCreateGuiComponent
        return new ComponentRenderer<>(recommendation ->
                new InspectionRecommendationCard(
                        recommendation,
                        uiComponents,
                        dialogs,
                        dataContext,
                        dialogWindows,
                        messages,
                        recommendationsDc,
                        findingsDc,
                        this,
                        isReadOnly()
                ));
    }


    @Subscribe("createFindingAction")
    public void onCreateFindingAction(final ActionPerformedEvent event) {
        dialogWindows.detail(this, InspectionFinding.class)
                .withParentDataContext(dataContext)
                .withContainer(findingsDc)
                .open();
    }

    @Subscribe("createRecommendationAction")
    public void onCreateRecommendationAction(final ActionPerformedEvent event) {
        DialogWindow<InspectionRecommendationDetailView> dialogWindow = dialogWindows.detail(this, InspectionRecommendation.class)
                .withViewClass(InspectionRecommendationDetailView.class)
                .withViewConfigurer(view -> view.setFindings(findingsDc.getDisconnectedItems()))
                .withParentDataContext(dataContext)
                .withContainer(recommendationsDc)
                .build();
//        dialogWindow.getView().setFindings(findingsDc.getDisconnectedItems());
        dialogWindow.open();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UI integration testing support
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CollectionPropertyContainer<InspectionFinding> getFindingsDc() {
        return findingsDc;
    }

    protected CollectionPropertyContainer<InspectionRecommendation> getRecommendationsDc() {
        return recommendationsDc;
    }

    protected DataContext getDataContext() {
        return dataContext;
    }

    public void setSignatureImage(String signatureImage) {
        signature.setImage(signatureImage);
    }
}
