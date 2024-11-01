package io.jmix.windturbines.view.inspectionfinding;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.component.upload.receiver.TemporaryStorageFileData;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import io.jmix.windturbines.view.inspectionfinding.inspectionfindingevidencefragment.InspectionFindingEvidenceFragment;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "inspectionFindings/:id", layout = MainView.class)
@ViewController("InspectionFinding.detail")
@ViewDescriptor("inspection-finding-detail-view.xml")
@EditedEntityContainer("inspectionFindingDc")
public class InspectionFindingDetailView extends StandardDetailView<InspectionFinding> {

    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private Fragments fragments;
    @Autowired
    private FileStorage fileStorage;


    @ViewComponent
    private CollectionPropertyContainer<InspectionFindingEvidence> evidencesDc;
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private FileStorageUploadField upload;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        upload.setEnabled(!isReadOnly());
    }

    @Supply(to = "evidencesVirtualList", subject = "renderer")
    private Renderer<InspectionFindingEvidence> evidencesVirtualListRenderer() {
        return new ComponentRenderer<>(this::createInspectionFindingEvidenceCard);
    }

    private InspectionFindingEvidenceFragment createInspectionFindingEvidenceCard(InspectionFindingEvidence evidence) {
        InspectionFindingEvidenceFragment fragment = fragments.create(this, InspectionFindingEvidenceFragment.class);
        fragment.setEvidence(evidence);
        fragment.setAfterAssignmentPerformedHandler(it -> {
            evidencesDc.getMutableItems().remove(it);
            dataContext.remove(dataContext.merge(it));
        });
        return fragment;
    }

    public CollectionPropertyContainer<InspectionFindingEvidence> getEvidencesDc() {
        return evidencesDc;
    }

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
            evidencesDc.getMutableItems().add(inspectionFindingEvidence);

        }
    }
}
