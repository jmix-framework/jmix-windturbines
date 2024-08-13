package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.ReadOnlyAwareView;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.view.inspectionfinding.InspectionFindingDetailView;

import java.util.List;

public class InspectionFindingCardOld extends VerticalLayout {

    private final InspectionFinding finding;
    private final UiComponents uiComponents;
    private final Dialogs dialogs;
    private final DataContext dataContext;
    private final DialogWindows dialogWindows;
    private final Messages messages;
    private final CollectionPropertyContainer<InspectionFinding> findingsDc;
    private final CollectionPropertyContainer<InspectionRecommendation> recommendationsDc;
    private final View<?> originView;
    private final boolean readOnly;

    public InspectionFindingCardOld(
            InspectionFinding finding,
            UiComponents uiComponents,
            Dialogs dialogs,
            DataContext dataContext,
            DialogWindows dialogWindows,
            Messages messages,
            CollectionPropertyContainer<InspectionFinding> findingsDc,
            CollectionPropertyContainer<InspectionRecommendation> recommendationsDc,
            View<?> originView,
            boolean readOnly
    ) {
        this.finding = finding;
        this.uiComponents = uiComponents;
        this.dialogs = dialogs;
        this.dataContext = dataContext;
        this.dialogWindows = dialogWindows;
        this.messages = messages;
        this.findingsDc = findingsDc;
        this.recommendationsDc = recommendationsDc;
        this.originView = originView;
        this.readOnly = readOnly;

        initLayout();
    }

    private void initLayout() {

        setSpacing(false);
        setPadding(false);
        setWidthFull();
        setId("finding-" + finding.getId());
        addClassNames(
                LumoUtility.Margin.Bottom.MEDIUM,
                LumoUtility.Padding.SMALL,
                LumoUtility.Gap.MEDIUM,
                "white-card",
                "cursor-pointer",
                "finding-card"
        );


        HorizontalLayout firstRow = createHorizontalLayout();
        firstRow.setWidthFull();
        firstRow.setAlignItems(Alignment.CENTER);
        firstRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

        Span title = uiComponents.create(Span.class);
        title.setId("title");
        title.setClassName("cut-overflow-text");
        title.setText(finding.getTitle());
        title.setWidthFull();
        firstRow.add(title);

        HorizontalLayout severityLayout = createHorizontalLayout();
        severityLayout.setJustifyContentMode(JustifyContentMode.END);

        Span severity = uiComponents.create(Span.class);
        severity.setId("severity");
        severity.getElement().getThemeList().addAll(List.of("badge", "pill", finding.getSeverity().getBadgeThemeName()));
        severity.setWidth("120px");
        severity.setText(messages.getMessage(finding.getSeverity()));
        severityLayout.add(severity);
        firstRow.add(severityLayout);

        add(firstRow);

        HorizontalLayout secondRow = createHorizontalLayout();
        secondRow.setWidthFull();
        secondRow.setAlignItems(Alignment.STRETCH);
        secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);


        Span description = uiComponents.create(Span.class);
        description.setId("description");
        description.setClassName("cut-overflow-text");
        description.setText(finding.getDescription());
        description.setWidthFull();
        secondRow.add(description);


        HorizontalLayout detailButtonLayout = createHorizontalLayout();
        detailButtonLayout.setJustifyContentMode(JustifyContentMode.END);

        Button editBtn = uiComponents.create(Button.class);
        editBtn.setId("editBtn");
        editBtn.setIcon(VaadinIcon.PENCIL.create());
        editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        editBtn.addClickListener(e ->
                {
                    DialogWindow<InspectionFindingDetailView> dialogWindow = dialogWindows.detail(originView, InspectionFinding.class)
                            .withViewClass(InspectionFindingDetailView.class)
                            .withParentDataContext(dataContext)
                            .withContainer(findingsDc)
                            .editEntity(finding)
                            .build();

                    dialogWindow.setHeight("90%");
                    dialogWindow.setWidth("90%");
                    dialogWindow.open();
                }

        );
        editBtn.setVisible(!readOnly);

        Button readBtn = uiComponents.create(Button.class);
        readBtn.setId("readBtn");
        readBtn.setIcon(VaadinIcon.SEARCH.create());
        readBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        readBtn.addClickListener(e ->
                dialogWindows.detail(originView, InspectionFinding.class)
                        .withViewConfigurer(view -> ((ReadOnlyAwareView) view).setReadOnly(true))
                        .editEntity(finding)
                        .open()
        );
        readBtn.setVisible(readOnly);

        Button removeBtn = uiComponents.create(Button.class);
        removeBtn.setId("removeBtn");
        removeBtn.setIcon(VaadinIcon.TRASH.create());
        removeBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
        removeBtn.addClickListener(e ->
                dialogs.createOptionDialog()
                        .withHeader(messages.getMessage("io.jmix.windturbines.view.inspection", "removeFindingDialog.header"))
                        .withText(messages.getMessage("io.jmix.windturbines.view.inspection", "removeFindingDialog.text"))
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withVariant(ActionVariant.DANGER)
                                        .withHandler(this::removeFinding),
                                new DialogAction(DialogAction.Type.CANCEL)
                        )
                        .open()
        );
        removeBtn.setEnabled(!readOnly);

        detailButtonLayout.add(readBtn);
        detailButtonLayout.add(editBtn);
        detailButtonLayout.add(removeBtn);
        secondRow.add(detailButtonLayout);
        secondRow.expand(description);

        add(secondRow);
    }

    private void removeFinding(ActionPerformedEvent event) {
        findingsDc.getMutableItems().remove(finding);
        dataContext.remove(dataContext.merge(finding));

        List<InspectionRecommendation> relatedRecommendations = recommendationsDc.getMutableItems().stream()
                .filter(it -> finding.equals(it.getRelatedFinding()))
                .toList();
        relatedRecommendations.forEach(recommendation -> {
            recommendationsDc.getMutableItems().remove(recommendation);
            dataContext.remove(dataContext.merge(recommendation));
        });
    }

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout layout = uiComponents.create(HorizontalLayout.class);
        layout.setPadding(false);
        return layout;
    }
}
