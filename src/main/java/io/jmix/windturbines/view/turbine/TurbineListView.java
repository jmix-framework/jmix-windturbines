package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

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
        turbineCard.setOriginView(this);
        return turbineCard;
    }
}
