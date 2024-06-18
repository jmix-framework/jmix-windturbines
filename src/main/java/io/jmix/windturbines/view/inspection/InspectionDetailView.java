package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.accordion.JmixAccordion;
import io.jmix.flowui.component.radiobuttongroup.JmixRadioButtonGroup;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.view.inspectionrecommendation.InspectionRecommendationDetailView;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "inspections/:id", layout = MainView.class)
@ViewController("Inspection.detail")
@ViewDescriptor("inspection-detail-view.xml")
@EditedEntityContainer("inspectionDc")
public class InspectionDetailView extends StandardDetailView<Inspection> {
    public static final int LAST_SECTION_INDEX = 3;
    @ViewComponent
    private JmixRadioButtonGroup<Integer> generatorCheckAnswerField;
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Wizard behaviour
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Subscribe
    public void onInit(final InitEvent event) {
        generatorCheckAnswerField.setItems(List.of(1, 2, 3, 4, 5));
        openAccordionPanel(0);
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        createFindingBtn.setEnabled(!isReadOnly());
    }

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

    @Subscribe("finishAction")
    public void onFinishAction(final ActionPerformedEvent event) {
        getEditedEntity().setTaskStatus(TaskStatus.COMPLETED);
        closeWithSave();
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
        DialogWindow<InspectionRecommendationDetailView> build = dialogWindows.detail(this, InspectionRecommendation.class)
                .withViewClass(InspectionRecommendationDetailView.class)
                .withParentDataContext(dataContext)
                .withContainer(recommendationsDc)
                .build();

        build.getView().setFindingsDc(findingsDc);

        build
                .open();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UI integration testing support
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    protected CollectionPropertyContainer<InspectionFinding> getFindingsDc() {
        return findingsDc;
    }

    protected DataContext getDataContext() {
        return dataContext;
    }

}
