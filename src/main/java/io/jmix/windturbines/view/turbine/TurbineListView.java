package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "turbines", layout = MainView.class)
@ViewController("Turbine.list")
@ViewDescriptor("turbine-list-view.xml")
@DialogMode(width = "64em")
public class TurbineListView extends StandardListView<Turbine> {

    @Autowired
    private Fragments fragments;

    @Supply(to = "turbinesVirtualList", subject = "renderer")
    private Renderer<Turbine> turbinesVirtualListRenderer() {
        return new ComponentRenderer<>(this::createTurbineCard);
    }

    private TurbineCard createTurbineCard(Turbine turbine) {
        TurbineCard turbineCard = fragments.create(this, TurbineCard.class);
        turbineCard.setTurbine(turbine);
        turbineCard.setOrigin(this);
        return turbineCard;
    }
}
