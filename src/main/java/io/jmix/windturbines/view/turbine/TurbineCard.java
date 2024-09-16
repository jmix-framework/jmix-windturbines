package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.Turbine;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("turbine-card.xml")
public class TurbineCard extends Fragment<VerticalLayout> {

    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private Span statusBadge;

    private View<?> originView;
    @ViewComponent
    private InstanceContainer<Turbine> turbineDc;

    public void setOriginView(View<?> originView) {
        this.originView = originView;
    }

    public void setTurbine(Turbine turbine) {
        turbineDc.setItem(turbine);
        statusBadge.getElement().getThemeList().add(turbine.getStatus().getBadgeThemeName());
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
        viewNavigators.detailView(originView, Turbine.class)
                .withReadOnly(true)
                .editEntity(turbine)
                .navigate();
    }
}