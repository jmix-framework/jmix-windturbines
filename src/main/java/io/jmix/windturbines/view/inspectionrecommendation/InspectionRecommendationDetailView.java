package io.jmix.windturbines.view.inspectionrecommendation;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.combobox.EntityComboBox;
import io.jmix.flowui.component.select.JmixSelect;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.view.main.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Route(value = "inspectionRecommendations/:id", layout = MainView.class)
@ViewController("InspectionRecommendation.detail")
@ViewDescriptor("inspection-recommendation-detail-view.xml")
@EditedEntityContainer("inspectionRecommendationDc")
public class InspectionRecommendationDetailView extends StandardDetailView<InspectionRecommendation> {

    private static final Logger log = LoggerFactory.getLogger(InspectionRecommendationDetailView.class);
    @ViewComponent
    private JmixSelect<InspectionFinding> relatedFindingField;

    @Subscribe
    public void onInit(final InitEvent event) {
        setReloadEdited(false);
    }


    @Subscribe
    public void onReady(final ReadyEvent event) {
        InspectionRecommendation recommendation = getEditedEntity();
        InspectionFinding relatedFinding = recommendation.getRelatedFinding();
        log.info("[InspectionRecommendationDetailView::onReady] setting value for field");
        log.info("[InspectionRecommendationDetailView::onReady] recommendation: {}", recommendation);
        log.info("[InspectionRecommendationDetailView::onReady] relatedFinding: {}", relatedFinding);

        relatedFindingField.setValue(relatedFinding);
    }


    public void setFindings(List<InspectionFinding> findings) {
        relatedFindingField.setItems(findings);
    }
}
