package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.function.Consumer;

@FragmentDescriptor("inspection-recommendation-card.xml")
public class InspectionRecommendationCard extends Fragment<VerticalLayout> {

    @Autowired
    private Messages messages;

    @ViewComponent
    private Span title;
    @ViewComponent
    private Span priority;
    @ViewComponent
    private Span description;
    @ViewComponent
    private JmixButton readBtn;
    @ViewComponent
    private JmixButton editBtn;
    @ViewComponent
    private JmixButton removeBtn;

    private Consumer<InspectionRecommendation> editActionHandler;
    private Consumer<InspectionRecommendation> readActionHandler;
    private Consumer<InspectionRecommendation> removeActionHandler;
    @ViewComponent
    private InstanceContainer<InspectionRecommendation> inspectionRecommendationDc;

    public void setInspectionRecommendation(InspectionRecommendation recommendation) {
        inspectionRecommendationDc.setItem(recommendation);
        Optional.ofNullable(recommendation.getRelatedFinding())
                .ifPresent(finding -> title.setText("Finding: %s".formatted(finding.getTitle())));
        priority.getElement().getThemeList().add(recommendation.getPriority().getBadgeThemeName());
    }

    public void setEditActionHandler(Consumer<InspectionRecommendation> editActionHandler) {
        this.editActionHandler = editActionHandler;
    }

    public void setReadActionHandler(Consumer<InspectionRecommendation> readActionHandler) {
        this.readActionHandler = readActionHandler;
    }

    public void setRemoveActionHandler(Consumer<InspectionRecommendation> removeActionHandler) {
        this.removeActionHandler = removeActionHandler;
    }


    @Subscribe("editAction")
    public void onEditAction(final ActionPerformedEvent event) {
        editActionHandler.accept(inspectionRecommendationDc.getItem());
    }

    @Subscribe("readAction")
    public void onReadAction(final ActionPerformedEvent event) {
        readActionHandler.accept(inspectionRecommendationDc.getItem());
    }

    @Subscribe("removeAction")
    public void onRemoveAction(final ActionPerformedEvent event) {
        removeActionHandler.accept(inspectionRecommendationDc.getItem());
    }

    public void setReadOnly(boolean readOnly) {
        readBtn.setVisible(readOnly);
        editBtn.setVisible(!readOnly);
        removeBtn.setEnabled(!readOnly);
    }

}