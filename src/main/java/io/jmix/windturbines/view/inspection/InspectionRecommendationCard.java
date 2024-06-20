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
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.ReadOnlyAwareView;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.view.inspectionrecommendation.InspectionRecommendationDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class InspectionRecommendationCard extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(InspectionRecommendationCard.class);
    private final InspectionRecommendation recommendation;
    private final UiComponents uiComponents;
    private final Dialogs dialogs;
    private final DataContext dataContext;
    private final DialogWindows dialogWindows;
    private final Messages messages;
    private final CollectionPropertyContainer<InspectionRecommendation> recommendationsDc;
    private final CollectionPropertyContainer<InspectionFinding> findingsDc;
    private final View<?> originView;
    private final boolean readOnly;

    public InspectionRecommendationCard(
            InspectionRecommendation recommendation,
            UiComponents uiComponents,
            Dialogs dialogs,
            DataContext dataContext,
            DialogWindows dialogWindows,
            Messages messages,
            CollectionPropertyContainer<InspectionRecommendation> recommendationsDc,
            CollectionPropertyContainer<InspectionFinding> findingsDc,
            View<?> originView,
            boolean readOnly
    ) {
        this.recommendation = recommendation;
        this.uiComponents = uiComponents;
        this.dialogs = dialogs;
        this.dataContext = dataContext;
        this.dialogWindows = dialogWindows;
        this.messages = messages;
        this.recommendationsDc = recommendationsDc;
        this.findingsDc = findingsDc;
        this.originView = originView;
        this.readOnly = readOnly;

        initLayout();
    }


    private void initLayout() {

        setSpacing(false);
        setPadding(false);
        setWidthFull();
        setId("finding-" + recommendation.getId());
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
        firstRow.setAlignItems(Alignment.STRETCH);
        firstRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

        Span title = uiComponents.create(Span.class);
        title.setId("title");
        title.setClassName("cut-overflow-text");
        Optional.ofNullable(recommendation.getRelatedFinding())
                .ifPresent(finding -> title.setText("Finding: %s".formatted(finding.getTitle())));
        title.setWidthFull();
        firstRow.add(title);

        HorizontalLayout priorityLayout = createHorizontalLayout();
        priorityLayout.setJustifyContentMode(JustifyContentMode.END);

        Span priority = uiComponents.create(Span.class);
        priority.setId("priority");
        priority.getElement().getThemeList().addAll(List.of("badge", recommendation.getPriority().getBadgeThemeName()));
        priority.setWidth("100px");
        priority.setText(messages.getMessage(recommendation.getPriority()));
        priorityLayout.add(priority);
        firstRow.add(priorityLayout);

        add(firstRow);

        HorizontalLayout secondRow = createHorizontalLayout();
        secondRow.setWidthFull();
        secondRow.setAlignItems(Alignment.STRETCH);
        secondRow.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);


        Span description = uiComponents.create(Span.class);
        description.setId("description");
        description.setClassName("cut-overflow-text");
        description.setText(recommendation.getDescription());
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
                    DialogWindow<InspectionRecommendationDetailView> build = dialogWindows.detail(originView, InspectionRecommendation.class)
                            .withViewClass(InspectionRecommendationDetailView.class)
                            .editEntity(recommendation)
                            .withParentDataContext(dataContext)
                            .withViewConfigurer(view -> {
                                        log.info("[View Configurer] setting findings to view");
                                        log.info("[View Configurer] recommendation: {}", recommendation);
                                        log.info("[View Configurer] relatedFinding: {}", recommendation.getRelatedFinding());
                                        view.setFindings(findingsDc.getDisconnectedItems());
                                    }
                            )
                            .withContainer(recommendationsDc)
                            .open();
                }
        );
        editBtn.setVisible(!readOnly);

        Button readBtn = uiComponents.create(Button.class);
        readBtn.setId("readBtn");
        readBtn.setIcon(VaadinIcon.SEARCH.create());
        readBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        readBtn.addClickListener(e ->
                dialogWindows.detail(originView, InspectionRecommendation.class)
                        .withViewConfigurer(view -> ((ReadOnlyAwareView) view).setReadOnly(true))
                        .editEntity(recommendation)
                        .open()
        );
        readBtn.setVisible(readOnly);

        Button removeBtn = uiComponents.create(Button.class);
        removeBtn.setId("removeBtn");
        removeBtn.setIcon(VaadinIcon.TRASH.create());
        removeBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY_INLINE);
        removeBtn.addClickListener(e ->
                dialogs.createOptionDialog()
                        .withHeader(messages.getMessage("io.jmix.windturbines.view.inspection", "removeRecommendationDialog.header"))
                        .withText(messages.getMessage("io.jmix.windturbines.view.inspection", "removeRecommendationDialog.text"))
                        .withActions(
                                new DialogAction(DialogAction.Type.OK)
                                        .withVariant(ActionVariant.DANGER)
                                        .withHandler(actionPerformedEvent -> {
                                            recommendationsDc.getMutableItems().remove(recommendation);
                                            dataContext.remove(dataContext.merge(recommendation));
                                        }),
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

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout layout = uiComponents.create(HorizontalLayout.class);
        layout.setPadding(false);
        return layout;
    }
}
