package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.Turbine;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("turbine-card.xml")
@RendererItemContainer("turbineDc")
public class TurbineCard extends FragmentRenderer<VerticalLayout, Turbine> {

    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private Span statusBadge;

    @ViewComponent
    private InstanceContainer<Turbine> turbineDc;


    @Subscribe(id = "turbineDc", target = Target.DATA_CONTAINER)
    public void onTurbineDcItemChange(final InstanceContainer.ItemChangeEvent<Turbine> event) {
        statusBadge.getElement().getThemeList().add(event.getItem().getStatus().getBadgeThemeName());
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        getContent().addClickListener(e -> navigateToDetailView(turbineDc.getItem()));
    }

    @Subscribe("detailsAction")
    public void onDetailsAction(final ActionPerformedEvent event) {
        navigateToDetailView(turbineDc.getItem());
    }

    private void navigateToDetailView(Turbine turbine) {
        viewNavigators.detailView(UiComponentUtils.getCurrentView(), Turbine.class)
                .withReadOnly(true)
                .editEntity(turbine)
                .navigate();
    }

}