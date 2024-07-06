package io.jmix.windturbines.view.inspectionfinding;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.component.upload.receiver.TemporaryStorageFileData;
import io.jmix.flowui.data.value.ContainerValueSource;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataComponents;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import io.jmix.windturbines.view.inspectionfindingevidence.InspectionFindingEvidenceDetailView;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route(value = "inspectionFindings/:id", layout = MainView.class)
@ViewController("InspectionFinding.detail")
@ViewDescriptor("inspection-finding-detail-view.xml")
@EditedEntityContainer("inspectionFindingDc")
public class InspectionFindingDetailView extends StandardDetailView<InspectionFinding> {

    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private CollectionPropertyContainer<InspectionFindingEvidence> evidencesDc;
    @ViewComponent
    private DataContext dataContext;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private DataComponents dataComponents;
    @ViewComponent
    private Div evidencesListWrapper;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private FileStorageUploadField upload;

    @Subscribe("upload")
    public void onUploadFileUploadSucceeded(final FileUploadSucceededEvent<FileStorageUploadField> event) {
        if (event.getReceiver() instanceof FileTemporaryStorageBuffer buffer) {

            TemporaryStorageFileData fileData = buffer.getFileData();
            FileRef fileRef = temporaryStorage.putFileIntoStorage(
                    fileData.getFileInfo().getId(),
                    fileData.getFileName(),
                    fileStorage
            );

            InspectionFindingEvidence inspectionFindingEvidence = dataContext.create(InspectionFindingEvidence.class);
            inspectionFindingEvidence.setFile(fileRef);
            inspectionFindingEvidence.setInspectionFinding(getEditedEntity());
            evidencesDc.getMutableItems().add(
                    inspectionFindingEvidence
            );

            renderEvidenceList();
        }
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        renderEvidenceList();
        upload.setEnabled(!isReadOnly());
    }

    private void renderEvidenceList() {
        evidencesListWrapper.removeAll();
        evidencesListWrapper.add(evidencesList());
    }

    private Component evidencesList() {

        FlexLayout flexLayout = uiComponents.create(FlexLayout.class);
        flexLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        flexLayout.setAlignItems(FlexLayout.Alignment.START);
        flexLayout.addClassName("evidence-flex-container");

        Optional.ofNullable(getEditedEntity().getEvidences())
                .ifPresent(evidences ->
                        evidences.stream()
                                .map(this::createEvidenceItem)
                                .forEach(flexLayout::add)
                );

        return flexLayout;
    }

    private Component createEvidenceItem(InspectionFindingEvidence findingEvidence) {

        InstanceContainer<InspectionFindingEvidence> instanceContainer = dataComponents.createInstanceContainer(InspectionFindingEvidence.class);
        instanceContainer.setItem(findingEvidence);

        JmixImage<FileRef> image = uiComponents.create(JmixImage.class);
        image.setValueSource(new ContainerValueSource<>(instanceContainer, "file"));
        image.addClassName("evidence-image");

        Div imageContainer = uiComponents.create(Div.class);
        imageContainer.addClassName("image-container");
        imageContainer.add(image);

        imageContainer.addClickListener(event -> {
            DialogWindow<InspectionFindingEvidenceDetailView> dialogWindow = dialogWindows.detail(this, InspectionFindingEvidence.class)
                    .withViewClass(InspectionFindingEvidenceDetailView.class)
                    .editEntity(findingEvidence)
                    .build();

            dialogWindow.setWidthFull();
            dialogWindow.setHeightFull();

            dialogWindow.open();
        });

        if (!isReadOnly()) {
            Div deleteEvidenceButton = new Div();
            deleteEvidenceButton.addClassName("delete-button");
            deleteEvidenceButton.setText("✕");
            deleteEvidenceButton.addClickListener(e -> {
                evidencesDc.getMutableItems().remove(findingEvidence);
                dataContext.remove(findingEvidence);
                renderEvidenceList();
            });

            imageContainer.add(deleteEvidenceButton);
        }

        return imageContainer;
    }


}
