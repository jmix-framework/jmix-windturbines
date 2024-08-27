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
import io.jmix.flowui.*;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.online.OnlineDemoDataCreator;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static io.jmix.flowui.view.ViewControllerUtils.getViewData;

@Route(value = "inspections", layout = MainView.class)
@ViewController("Inspection.list")
@ViewDescriptor("inspection-list-view.xml")
@DialogMode(width = "64em")
public class InspectionListView extends StandardListView<Inspection> {

    @Autowired
    private Fragments fragments;
    @ViewComponent
    private JmixTabSheet contentTabSheet;
    @ViewComponent("contentTabSheet.myInspectionsTab")
    private Tab contentTabSheetMyInspectionsTab;
    @Autowired
    private Optional<OnlineDemoDataCreator> onlineDemoDataCreator;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        onlineDemoDataCreator.ifPresent(it -> {
            it.createDemoData(() -> getViewData().loadAll());
        });
    }

    @Supply(to = "allInspectionsVirtualList", subject = "renderer")
    private Renderer<Inspection> allInspectionsVirtualListRenderer() {
        return new ComponentRenderer<>(inspection -> {
            AllInspectionCard card = fragments.create(this, AllInspectionCard.class);
            card.setInspection(inspection);
            card.setAfterAssignmentPerformedHandler(it -> {
                contentTabSheet.setSelectedTab(contentTabSheetMyInspectionsTab);
                getViewData().loadAll();
            });
            return card;
        });
    }

    @Supply(to = "myInspectionsVirtualList", subject = "renderer")
    private Renderer<Inspection> myInspectionsVirtualListRenderer() {
        return new ComponentRenderer<>(inspection -> {
            MyInspectionCard card = fragments.create(this, MyInspectionCard.class);
            card.setInspection(inspection);
            card.setOriginView(this);
            return card;
        });
    }
}
