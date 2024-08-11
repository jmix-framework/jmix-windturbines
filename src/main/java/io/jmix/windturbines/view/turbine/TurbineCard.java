package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@FragmentDescriptor("turbine-card.xml")
public class TurbineCard extends Fragment<VerticalLayout> {

    @Autowired
    private Messages messages;
    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private Span turbineId;
    @ViewComponent
    private Span statusBadge;
    @ViewComponent
    private Span model;
    @ViewComponent
    private Span manufacturerName;
    @ViewComponent
    private Span location;

    private View<?> originView;
    private Turbine turbine;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        getContent().addClickListener(e -> navigateToDetailView(turbine));
    }

    public void setTurbine(Turbine turbine) {
        this.turbine = turbine;
        turbineId.setText(turbine.getTurbineId());
        statusBadge.setText(messages.getMessage(turbine.getStatus()));
        statusBadge.getElement().getThemeList().add(turbine.getStatus().getBadgeThemeName());
        manufacturerName.setText(turbine.getManufacturer().getName());
        model.setText(turbine.getModel());
        location.setText(turbine.getLocation());
    }

    @Subscribe("detailsAction")
    public void onDetailsAction(final ActionPerformedEvent event) {
        navigateToDetailView(turbine);
    }

    private void navigateToDetailView(Turbine turbine) {
        viewNavigators.detailView(originView, Turbine.class)
                .withReadOnly(true)
                .editEntity(turbine)
                .navigate();
    }

    public void setOrigin(View<?> originView) {
        this.originView = originView;
    }
}