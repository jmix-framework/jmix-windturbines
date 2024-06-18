package io.jmix.windturbines.view.inspectionrecommendation;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.view.main.MainView;

@Route(value = "inspectionRecommendations/:id", layout = MainView.class)
@ViewController("InspectionRecommendation.detail")
@ViewDescriptor("inspection-recommendation-detail-view.xml")
@EditedEntityContainer("inspectionRecommendationDc")
public class InspectionRecommendationDetailView extends StandardDetailView<InspectionRecommendation> {

    private CollectionPropertyContainer<InspectionFinding> findingsDc;
    @ViewComponent
    private EntityComboBox<InspectionFinding> relatedFindingField;

    public void setFindingsDc(CollectionPropertyContainer<InspectionFinding> findingsDc) {
        this.findingsDc = findingsDc;
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
//        relatedFindingField.setItems(findingsDc);
//        relatedFindingField.setValue(getEditedEntity().getRelatedFinding());
    }

    @Subscribe
    public void onInit(final InitEvent event) {
//        relatedFindingField.setItems(findingsDc);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        relatedFindingField.setItems(findingsDc);
    }

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        relatedFindingField.setItems(findingsDc);
    }


}
