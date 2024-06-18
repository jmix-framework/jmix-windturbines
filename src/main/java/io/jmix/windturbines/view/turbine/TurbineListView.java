package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
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
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private ViewNavigators viewNavigators;

    @Supply(to = "turbinesVirtualList", subject = "renderer")
    private Renderer<Turbine> turbinesVirtualListRenderer() {
        return new ComponentRenderer<>(turbine -> {

            VerticalLayout mainLayout = createVerticalLayout();
            mainLayout.addClassNames();
            mainLayout.setWidth("99%");
            mainLayout.setId("turbine-" + turbine.getId());
            mainLayout.addClassNames(
                    LumoUtility.Margin.Bottom.MEDIUM,
                    LumoUtility.Padding.SMALL,
                    LumoUtility.Gap.MEDIUM,
                    "white-card",
                    "cursor-pointer"
            );


            HorizontalLayout firstRow = createHorizontalLayout();
            firstRow.setWidthFull();
            firstRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            firstRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Span turbineId = uiComponents.create(Span.class);
            turbineId.setText(turbine.getTurbineId());
            turbineId.setClassName(LumoUtility.FontWeight.BOLD);
            firstRow.add(turbineId);

            Span manufacturer = uiComponents.create(Span.class);
            manufacturer.setText(turbine.getManufacturer().getName());
            firstRow.add(manufacturer);

            HorizontalLayout statusLayout = createHorizontalLayout();
            statusLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            statusLayout.setWidthFull();

            Span status = uiComponents.create(Span.class);
            status.getElement().getThemeList().addAll(List.of("badge", turbine.getStatus().getBadgeThemeName()));
            status.setWidth("100px");
            status.setText(messages.getMessage(turbine.getStatus()));
            statusLayout.add(status);
            firstRow.add(statusLayout);

            mainLayout.add(firstRow);

            HorizontalLayout secondRow = createHorizontalLayout();
            secondRow.setWidthFull();
            secondRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);


            Span location = uiComponents.create(Span.class);
            location.setText(turbine.getLocation());
            location.setWidthFull();
            location.setClassName("cut-overflow-text");
            secondRow.add(location);


            HorizontalLayout detailButtonLayout = createHorizontalLayout();
            detailButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

            Button detailsButton = detailsButton(turbine);
            detailButtonLayout.add(detailsButton);
            secondRow.add(detailButtonLayout);
            secondRow.expand(location);

            mainLayout.add(secondRow);

            mainLayout.addClickListener(event ->
                    viewNavigators.detailView(Turbine.class)
                            .withReadOnly(true)
                            .editEntity(turbine)
                            .navigate()
            );

            return mainLayout;
        });
    }

    private Button detailsButton(Turbine turbine) {
        Button button = uiComponents.create(Button.class);
        button.setId("detailsButton");
        button.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(e ->
                viewNavigators.detailView(Turbine.class)
                        .withReadOnly(true)
                        .editEntity(turbine)
                        .navigate()
        );
        return button;
    }
    private VerticalLayout createVerticalLayout() {
        VerticalLayout layout = uiComponents.create(VerticalLayout.class);
        layout.setSpacing(false);
        layout.setPadding(false);
        return layout;
    }

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout layout = uiComponents.create(HorizontalLayout.class);
        layout.setPadding(false);
        return layout;
    }
}
