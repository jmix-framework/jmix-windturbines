package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.inspection.Inspection;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("my-inspection-card.xml")
@RendererItemContainer("inspectionDc")
public class MyInspectionCard extends FragmentRenderer<VerticalLayout, Inspection> {

    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private Span statusBadge;
    @ViewComponent
    private Span location;
    @ViewComponent
    private InstanceContainer<Inspection> inspectionDc;
    @ViewComponent
    private HorizontalLayout secondRowBox;

    @Subscribe(id = "inspectionDc", target = Target.DATA_CONTAINER)
    public void onInspectionDcItemChange(final InstanceContainer.ItemChangeEvent<Inspection> event) {
        statusBadge.getElement().getThemeList().add(inspectionDc.getItem().getTaskStatus().getBadgeThemeName());
        location.setText(inspectionDc.getItem().getTurbine().getLocation());
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        // example on how to add Vaadin Utility class names programmatically
        secondRowBox.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.Gap.MEDIUM
        );

        getContent().addClickListener(e -> navigateToDetailView(inspectionDc.getItem()));
    }


    @Subscribe("detailsAction")
    public void onDetailsAction(final ActionPerformedEvent event) {
        navigateToDetailView(inspectionDc.getItem());
    }

    private void navigateToDetailView(Inspection inspection) {
        viewNavigators.detailView(UiComponentUtils.getCurrentView(), Inspection.class)
                .withReadOnly(inspection.getTaskStatus().equals(TaskStatus.COMPLETED))
                .editEntity(inspection)
                .navigate();
    }
}