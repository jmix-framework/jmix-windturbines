package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
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
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.virtuallist.JmixVirtualList;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.MaintenanceTask;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Route(value = "turbines/:id", layout = MainView.class)
@ViewController("Turbine.detail")
@ViewDescriptor("turbine-detail-view.xml")
@EditedEntityContainer("turbineDc")
public class TurbineDetailView extends StandardDetailView<Turbine> {
    @ViewComponent
    private TypedTextField<String> operatorPhoneField;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private DatatypeFormatter datatypeFormatter;
    @ViewComponent
    private Span emptyMaintenanceTaskBox;
    @ViewComponent
    private JmixVirtualList<MaintenanceTask> maintenanceTasksVirtualList;

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        operatorPhoneField.setSuffixComponent(callButton(getEditedEntity().getOperator().getPhone()));
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (CollectionUtils.isEmpty(getEditedEntity().getMaintenanceTasks())) {
            emptyMaintenanceTaskBox.setVisible(true);
            maintenanceTasksVirtualList.setVisible(false);
        }
    }



    private Button callButton(String phoneNumber) {
        Button btn = uiComponents.create(Button.class);
        btn.setIcon(VaadinIcon.PHONE.create());
        btn.addClassNames("call-button", LumoUtility.BorderRadius.SMALL, LumoUtility.Background.CONTRAST_5);
        btn.addClickListener(e ->
                // We're using executeJs with window.open instead of UI.getCurrent().getPage().open()
                // because Vaadin internally calls 'this.stopApplication()', which breaks the application
                // when the system dialog asking to call the number is cancelled.
                UI.getCurrent().getPage().executeJs("window.open($0, '_self')", "tel:" + phoneNumber)
        );
        return btn;
    }

    @Supply(to = "maintenanceTasksVirtualList", subject = "renderer")
    private Renderer<MaintenanceTask> maintenanceTasksVirtualListRenderer() {
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
