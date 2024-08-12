package io.jmix.windturbines.view.inspectionfinding.inspectionfindingevidencefragment;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.FileRef;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.data.value.ContainerValueSource;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.DataComponents;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@FragmentDescriptor("inspection-finding-evidence-fragment.xml")
public class InspectionFindingEvidenceFragment extends Fragment<HorizontalLayout> {

    @Autowired
    private DataComponents dataComponents;
    @ViewComponent
    private JmixImage<FileRef> image;
    @ViewComponent
    private Span filename;

    private InspectionFindingEvidence evidence;
    private Consumer<InspectionFindingEvidence> afterEvidenceRemovedHandler;

    @Subscribe("removeAction")
    public void onRemoveAction(final ActionPerformedEvent event) {
        afterEvidenceRemovedHandler.accept(evidence);
    }

    public void setEvidence(InspectionFindingEvidence evidence) {
        this.evidence = evidence;
        filename.setText(evidence.getFile().getFileName());

        InstanceContainer<InspectionFindingEvidence> instanceContainer = dataComponents.createInstanceContainer(InspectionFindingEvidence.class);
        instanceContainer.setItem(evidence);
        image.setValueSource(new ContainerValueSource<>(instanceContainer, "file"));
    }

    public void setAfterAssignmentPerformedHandler(Consumer<InspectionFindingEvidence> afterEvidenceRemovedHandler) {
        this.afterEvidenceRemovedHandler = afterEvidenceRemovedHandler;
    }
}