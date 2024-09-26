package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.DataManager;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.Inspection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.function.Consumer;

@FragmentDescriptor("all-inspection-card.xml")
public class AllInspectionCard extends Fragment<VerticalLayout> {
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Notifications notifications;

    @ViewComponent
    private MessageBundle messageBundle;
    @ViewComponent
    private Span statusBadge;
    @ViewComponent
    private Span secondRowText;
    @ViewComponent
    private Span location;
    @ViewComponent
    private JmixButton assignBtn;
    @ViewComponent
    private InstanceContainer<Inspection> inspectionDc;

    private Consumer<Inspection> afterAssignmentPerformedHandler;

    public void setInspection(Inspection inspection) {
        inspectionDc.setItem(inspection);
        statusBadge.getElement().getThemeList().add(inspection.getTaskStatus().getBadgeThemeName());
        secondRowText.setText("%s - %s".formatted(inspection.getTurbine().getManufacturer().getName(), inspection.getTurbine().getModel()));
        location.setText(inspection.getTurbine().getLocation());
        assignBtn.setEnabled(!TaskStatus.COMPLETED.equals(inspection.getTaskStatus()));
    }

    @Subscribe("assignAction")
    public void onAssignAction(final ActionPerformedEvent event) {
        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("assignInspectionHeader"))
                .withText(messageBundle.getMessage("assignInspectionText"))
                .withActions(
                        new DialogAction(DialogAction.Type.OK)
                                .withVariant(ActionVariant.PRIMARY)
                                .withHandler(e -> {
                                    inspectionDc.getItem().setTechnician(currentUser());
                                    dataManager.save(inspectionDc.getItem());
                                    notifications.create(messageBundle.getMessage("inspectionAssigned"))
                                            .withType(Notifications.Type.SUCCESS)
                                            .show();
                                    afterAssignmentPerformedHandler.accept(inspectionDc.getItem());
                                }),
                        new DialogAction(DialogAction.Type.CANCEL)
                )
                .open();
    }

    private User currentUser() {
        return (User) currentAuthentication.getUser();
    }

    public void setAfterAssignmentPerformedHandler(Consumer<Inspection> afterAssignmentPerformedHandler) {
        this.afterAssignmentPerformedHandler = afterAssignmentPerformedHandler;
    }
}