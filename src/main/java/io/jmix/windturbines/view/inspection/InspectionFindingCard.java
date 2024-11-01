package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.Messages;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@FragmentDescriptor("inspection-finding-card.xml")
public class InspectionFindingCard extends Fragment<VerticalLayout> {

    @ViewComponent
    private Span severity;
    @ViewComponent
    private JmixButton readBtn;
    @ViewComponent
    private JmixButton editBtn;
    @ViewComponent
    private JmixButton removeBtn;
    @ViewComponent
    private InstanceContainer<InspectionFinding> inspectionFindingDc;

    private Consumer<InspectionFinding> editActionHandler;
    private Consumer<InspectionFinding> readActionHandler;
    private Consumer<InspectionFinding> removeActionHandler;

    public void setInspectionFinding(InspectionFinding finding) {
        inspectionFindingDc.setItem(finding);
        severity.getElement().getThemeList().add(finding.getSeverity().getBadgeThemeName());
    }

    public void setEditActionHandler(Consumer<InspectionFinding> editActionHandler) {
        this.editActionHandler = editActionHandler;
    }

    public void setReadActionHandler(Consumer<InspectionFinding> readActionHandler) {
        this.readActionHandler = readActionHandler;
    }

    public void setRemoveActionHandler(Consumer<InspectionFinding> removeActionHandler) {
        this.removeActionHandler = removeActionHandler;
    }

    @Subscribe("editAction")
    public void onEditAction(final ActionPerformedEvent event) {
        editActionHandler.accept(inspectionFindingDc.getItem());
    }

    @Subscribe("readAction")
    public void onReadAction(final ActionPerformedEvent event) {
        readActionHandler.accept(inspectionFindingDc.getItem());
    }

    @Subscribe("removeAction")
    public void onRemoveAction(final ActionPerformedEvent event) {
        removeActionHandler.accept(inspectionFindingDc.getItem());
    }

    public void setReadOnly(boolean readOnly) {
        readBtn.setVisible(readOnly);
        editBtn.setVisible(!readOnly);
        removeBtn.setEnabled(!readOnly);
    }
}