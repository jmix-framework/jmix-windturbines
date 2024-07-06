package io.jmix.windturbines.view.inspectionfindingevidence;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import io.jmix.windturbines.view.main.MainView;

@Route(value = "inspectionFindingEvidences/:id", layout = MainView.class)
@ViewController("InspectionFindingEvidence.detail")
@ViewDescriptor("inspection-finding-evidence-detail-view.xml")
@EditedEntityContainer("inspectionFindingEvidenceDc")
public class InspectionFindingEvidenceDetailView extends StandardDetailView<InspectionFindingEvidence> {
}
