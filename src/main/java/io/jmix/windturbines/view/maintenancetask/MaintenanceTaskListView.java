package io.jmix.windturbines.view.maintenancetask;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.MaintenanceTask;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "maintenanceTasks", layout = MainView.class)
@ViewController("MaintenanceTask.list")
@ViewDescriptor("maintenance-task-list-view.xml")
@DialogMode(width = "64em")
public class MaintenanceTaskListView extends StandardListView<MaintenanceTask> {
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private DatatypeFormatter datatypeFormatter;

    @Supply(to = "allMaintenanceTasksVirtualList", subject = "renderer")
    private Renderer<MaintenanceTask> allMaintenanceTasksVirtualListRenderer() {
        return maintenanceTaskRenderer();
    }

    @Supply(to = "myMaintenanceTasksVirtualList", subject = "renderer")
    private Renderer<MaintenanceTask> myMaintenanceTasksVirtualListRenderer() {
        return maintenanceTaskRenderer();
    }

    private ComponentRenderer<VerticalLayout, MaintenanceTask> maintenanceTaskRenderer() {
        return new ComponentRenderer<>(maintenanceTask -> {

            VerticalLayout mainLayout = createVerticalLayout();
            mainLayout.addClassNames();
            mainLayout.setWidth("99%");
            mainLayout.setId("maintenanceTask-" + maintenanceTask.getId());
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

            Icon calendarIcon = new Icon(VaadinIcon.CALENDAR_O);
            calendarIcon.addClassName(LumoUtility.Margin.Right.SMALL);
            firstRow.add(calendarIcon);

            Span maintenanceTaskDate = uiComponents.create(Span.class);
            maintenanceTaskDate.setText(datatypeFormatter.formatLocalDate(maintenanceTask.getMaintenanceTaskDate()));
            firstRow.add(maintenanceTaskDate);

            HorizontalLayout statusLayout = createHorizontalLayout();
            statusLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            statusLayout.setWidthFull();

            Span status = uiComponents.create(Span.class);
            status.getElement().getThemeList().addAll(List.of("badge", maintenanceTask.getTaskStatus().getBadgeThemeName()));
            status.setWidth("100px");
            status.setText(messages.getMessage(maintenanceTask.getTaskStatus()));
            statusLayout.add(status);
            firstRow.add(statusLayout);

            mainLayout.add(firstRow);

            HorizontalLayout secondRow = createHorizontalLayout();
            secondRow.setWidthFull();
            secondRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Span type = uiComponents.create(Span.class);
            type.setText(messages.getMessage(maintenanceTask.getType()));
            secondRow.add(type);


            HorizontalLayout detailButtonLayout = createHorizontalLayout();
            detailButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

            Button detailsButton = detailsButton(maintenanceTask);
            detailButtonLayout.add(detailsButton);
            secondRow.add(detailButtonLayout);
            secondRow.expand(type);

            mainLayout.add(secondRow);

            mainLayout.addClickListener(event ->
                    viewNavigators.detailView(MaintenanceTask.class)
                            .withReadOnly(maintenanceTask.getTaskStatus().equals(TaskStatus.COMPLETED))
                            .editEntity(maintenanceTask)
                            .navigate()
            );

            return mainLayout;
        });
    }


    private Button detailsButton(MaintenanceTask maintenanceTask) {
        Button button = uiComponents.create(Button.class);
        button.setId("detailsButton");
        button.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(e ->
                viewNavigators.detailView(MaintenanceTask.class)
                        .withReadOnly(maintenanceTask.getTaskStatus().equals(TaskStatus.COMPLETED))
                        .editEntity(maintenanceTask)
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
