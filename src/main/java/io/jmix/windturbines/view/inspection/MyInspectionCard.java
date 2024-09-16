package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.inspection.Inspection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

@FragmentDescriptor("my-inspection-card.xml")
public class MyInspectionCard extends Fragment<VerticalLayout> {

    @Autowired
    private ViewNavigators viewNavigators;

    @ViewComponent
    private Span statusBadge;
    @ViewComponent
    private Span location;
    @ViewComponent
    private InstanceContainer<Inspection> inspectionDc;
    @ViewComponent
    private HorizontalLayout secondRowBox;

    private View<?> originView;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        // example on how to add Vaadin Utility class names programmatically
        secondRowBox.addClassNames(
                LumoUtility.Padding.SMALL,
                LumoUtility.Gap.MEDIUM
        );

        getContent().addClickListener(e -> navigateToDetailView(inspectionDc.getItem()));
    }

    public void setInspection(Inspection inspection) {
        inspectionDc.setItem(inspection);
        statusBadge.getElement().getThemeList().add(inspection.getTaskStatus().getBadgeThemeName());

        location.setText(inspection.getTurbine().getLocation());
    }

    public void setOriginView(View<?> originView) {
        this.originView = originView;
    }


    @Subscribe("detailsAction")
    public void onDetailsAction(final ActionPerformedEvent event) {
        navigateToDetailView(inspectionDc.getItem());
    }

    private void navigateToDetailView(Inspection inspection) {
        viewNavigators.detailView(originView, Inspection.class)
                .withReadOnly(inspection.getTaskStatus().equals(TaskStatus.COMPLETED))
                .editEntity(inspection)
                .navigate();
    }
}