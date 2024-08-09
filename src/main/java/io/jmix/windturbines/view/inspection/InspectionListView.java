package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Route(value = "inspections", layout = MainView.class)
@ViewController("Inspection.list")
@ViewDescriptor("inspection-list-view.xml")
@DialogMode(width = "64em")
public class InspectionListView extends StandardListView<Inspection> {
    @Autowired
    private ViewNavigators viewNavigators;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Messages messages;
    @Autowired
    private DatatypeFormatter datatypeFormatter;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private JmixTabSheet contentTabSheet;
    @ViewComponent("contentTabSheet.myInspectionsTab")
    private Tab contentTabSheetMyInspectionsTab;

    @Supply(to = "allInspectionsVirtualList", subject = "renderer")
    private Renderer<Inspection> allInspectionsVirtualListRenderer() {
        return new ComponentRenderer<>(inspection -> {

            VerticalLayout mainLayout = createVerticalLayout();
            mainLayout.setId("inspection-" + inspection.getId());
            mainLayout.addClassNames(
                    LumoUtility.Margin.MEDIUM,
                    "white-card",
                    "cursor-pointer",
                    "turbine-list-white-card"
            );

            HorizontalLayout firstRow = createHorizontalLayout();
            firstRow.setWidthFull();
            firstRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            firstRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Icon calendarIcon = new Icon(VaadinIcon.CALENDAR_O);
            calendarIcon.addClassName(LumoUtility.Margin.Right.SMALL);
            firstRow.add(calendarIcon);

            Span inspectionDate = uiComponents.create(Span.class);
            inspectionDate.setText(datatypeFormatter.formatLocalDate(inspection.getInspectionDate()));
            firstRow.add(inspectionDate);

            HorizontalLayout statusLayout = createHorizontalLayout();
            statusLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            statusLayout.setWidthFull();

            Span status = uiComponents.create(Span.class);
            status.addClassName("task-status");
            status.getElement().getThemeList().addAll(List.of("badge", "pill", inspection.getTaskStatus().getBadgeThemeName()));
            status.setWidth("100px");
            status.setText(messages.getMessage(inspection.getTaskStatus()));
            statusLayout.add(status);
            firstRow.add(statusLayout);

            mainLayout.add(firstRow);

            HorizontalLayout secondRow = createHorizontalLayout();
            secondRow.setWidthFull();
            secondRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Span secondRowText = uiComponents.create(Span.class);


            String text = "%s - %s".formatted(
                    inspection.getInspectionDate(),
                    Optional.ofNullable(inspection.getTechnician())
                            .map(User::getDisplayName)
                            .orElse("Not assigned")
            );

            secondRowText.setText(text);
            secondRow.add(secondRowText);


            HorizontalLayout assignButtonLayout = createHorizontalLayout();
            assignButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

            Button assignButton = assignButton(inspection);
            assignButtonLayout.add(assignButton);
            secondRow.add(assignButtonLayout);
            secondRow.expand(secondRowText);

            mainLayout.add(secondRow);

            return mainLayout;
        });
    }

    @Supply(to = "myInspectionsVirtualList", subject = "renderer")
    private Renderer<Inspection> myInspectionsVirtualListRenderer() {
        return inspectionsRenderer();
    }

    private ComponentRenderer<VerticalLayout, Inspection> inspectionsRenderer() {
        return new ComponentRenderer<>(inspection -> {

            VerticalLayout mainLayout = createVerticalLayout();
            mainLayout.setId("inspection-" + inspection.getId());
            mainLayout.addClassNames(
                    LumoUtility.Margin.MEDIUM,
                    "white-card",
                    "cursor-pointer",
                    "turbine-list-white-card"
            );

            HorizontalLayout firstRow = createHorizontalLayout();
            firstRow.setWidthFull();
            firstRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            firstRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Icon calendarIcon = new Icon(VaadinIcon.CALENDAR_O);
            calendarIcon.addClassName(LumoUtility.Margin.Right.SMALL);
            firstRow.add(calendarIcon);

            Span inspectionDate = uiComponents.create(Span.class);
            inspectionDate.setText(datatypeFormatter.formatLocalDate(inspection.getInspectionDate()));
            firstRow.add(inspectionDate);

            HorizontalLayout statusLayout = createHorizontalLayout();
            statusLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            statusLayout.setWidthFull();

            Span status = uiComponents.create(Span.class);
            status.getElement().getThemeList().addAll(List.of("badge", "pill", inspection.getTaskStatus().getBadgeThemeName()));
            status.setWidth("100px");
            status.setText(messages.getMessage(inspection.getTaskStatus()));
            statusLayout.add(status);
            firstRow.add(statusLayout);

            mainLayout.add(firstRow);

            HorizontalLayout secondRow = createHorizontalLayout();
            secondRow.setWidthFull();
            secondRow.setAlignItems(FlexComponent.Alignment.STRETCH);
            secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

            Span secondRowText = uiComponents.create(Span.class);


            String text = "%s - %s".formatted(
                    inspection.getTurbine().getLocation(),
                    inspection.getTurbine().getManufacturer().getName()
            );

            secondRowText.setText(text);
            secondRow.addAndExpand(secondRowText);

            HorizontalLayout detailButtonLayout = createHorizontalLayout();
            detailButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

            Button detailsButton = detailsButton(inspection);
            detailButtonLayout.add(detailsButton);
            secondRow.add(detailButtonLayout);

            mainLayout.add(secondRow);

            mainLayout.addClickListener(event ->
                    viewNavigators.detailView(this, Inspection.class)
                            .withReadOnly(inspection.getTaskStatus().equals(TaskStatus.COMPLETED))
                            .editEntity(inspection)
                            .navigate()
            );

            return mainLayout;
        });
    }


    private Button detailsButton(Inspection inspection) {
        Button button = uiComponents.create(Button.class);
        button.setId("detailsButton");
        button.setIcon(VaadinIcon.CHEVRON_RIGHT.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(e ->
                viewNavigators.detailView(this, Inspection.class)
                        .withReadOnly(inspection.getTaskStatus().equals(TaskStatus.COMPLETED))
                        .editEntity(inspection)
                        .navigate()
        );
        return button;
    }

    private Button assignButton(Inspection inspection) {
        Button button = uiComponents.create(Button.class);
        button.setId("assignButton");
        button.setIcon(VaadinIcon.CHECK.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(e ->
                dialogs.createOptionDialog()
                        .withHeader(messageBundle.getMessage("assignInspectionHeader"))
                        .withText(messageBundle.getMessage("assignInspectionText"))
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withVariant(ActionVariant.PRIMARY)
                                        .withHandler(event -> {
                                            inspection.setTechnician(currentUser());
                                            dataManager.save(inspection);
                                            notifications.create(messageBundle.getMessage("inspectionAssigned"))
                                                    .withType(Notifications.Type.SUCCESS)
                                                    .show();
                                            contentTabSheet.setSelectedTab(contentTabSheetMyInspectionsTab);
                                            getViewData().loadAll();
                                        }),
                                new DialogAction(DialogAction.Type.CANCEL)
                        )
                        .open()
        );
        return button;
    }

    private User currentUser() {
        return (User) currentAuthentication.getUser();
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
