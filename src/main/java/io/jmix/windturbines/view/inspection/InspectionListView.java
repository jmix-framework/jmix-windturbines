package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.online.DemoDataCreator;
import io.jmix.windturbines.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

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
    private DemoDataCreator demoDataCreator;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        demoDataCreator.createDemoData(() -> getViewData().loadAll());
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
