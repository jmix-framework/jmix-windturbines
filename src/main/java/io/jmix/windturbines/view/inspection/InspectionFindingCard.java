package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@FragmentDescriptor("inspection-finding-card.xml")
public class InspectionFindingCard extends Fragment<VerticalLayout> {

    @Autowired
    private Messages messages;

    @ViewComponent
    private Span severity;
    @ViewComponent
    private Span description;
    @ViewComponent
    private Span title;
    @ViewComponent
    private JmixButton readBtn;
    @ViewComponent
    private JmixButton editBtn;
    @ViewComponent
    private JmixButton removeBtn;

    private Consumer<InspectionFinding> editActionHandler;
    private Consumer<InspectionFinding> readActionHandler;
    private Consumer<InspectionFinding> removeActionHandler;
    private InspectionFinding finding;

    public void setInspectionFinding(InspectionFinding finding) {
        this.finding = finding;
        title.setText(finding.getTitle());
        severity.setText(messages.getMessage(finding.getSeverity()));
        severity.getElement().getThemeList().add(finding.getSeverity().getBadgeThemeName());
        description.setText(finding.getDescription());
    }


    @Subscribe("editAction")
    public void onEditAction(final ActionPerformedEvent event) {
        editActionHandler.accept(finding);
    }


    @Subscribe("readAction")
    public void onReadAction(final ActionPerformedEvent event) {
        readActionHandler.accept(finding);
    }

    @Subscribe("removeAction")
    public void onRemoveAction(final ActionPerformedEvent event) {
        removeActionHandler.accept(finding);
    }


    public void setEditActionHandler(Consumer<InspectionFinding> editActionHandler) {
        this.editActionHandler = editActionHandler;
    }

    public void setReadOnly(boolean readOnly) {
        readBtn.setVisible(readOnly);
        editBtn.setVisible(!readOnly);
        removeBtn.setEnabled(!readOnly);
    }

    public void setReadActionHandler(Consumer<InspectionFinding> readActionHandler) {
        this.readActionHandler = readActionHandler;
    }

    public void setRemoveActionHandler(Consumer<InspectionFinding> removeActionHandler) {
        this.removeActionHandler = removeActionHandler;
    }
}