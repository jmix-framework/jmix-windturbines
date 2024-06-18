package io.jmix.windturbines.view.inspectionfinding;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.view.main.MainView;

@Route(value = "inspectionFindings/:id", layout = MainView.class)
@ViewController("InspectionFinding.detail")
@ViewDescriptor("inspection-finding-detail-view.xml")
@EditedEntityContainer("inspectionFindingDc")
public class InspectionFindingDetailView extends StandardDetailView<InspectionFinding> {
}
