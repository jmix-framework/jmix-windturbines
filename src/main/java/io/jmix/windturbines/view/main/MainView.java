package io.jmix.windturbines.view.main;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.AbstractIcon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.windturbines.entity.MaintenanceTask;
import io.jmix.windturbines.entity.Turbine;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private Tabs mainMenuTabs;

    private Tab turbineTab;
    private Tab maintenanceTaskTab;

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        mainMenu();
    }


    private void mainMenu() {
        StreamResource iconResource = new StreamResource("code-branch.svg",
                () -> getClass().getResourceAsStream("/META-INF/resources/icons/turbine.svg"));
        SvgIcon icon = new SvgIcon(iconResource);
        maintenanceTaskTab = createTab("Tasks", "maintenanceTasks", VaadinIcon.CHECK_SQUARE.create());
        turbineTab = createTab("Turbines", "turbines", icon);
        mainMenuTabs.add(
                maintenanceTaskTab,
                turbineTab
        );
    }

    private Tab createTab(String title, String id, AbstractIcon icon) {
        Tab tab = new Tab(icon, new Span(title));
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.setId(id);
        return tab;
    }

    @Subscribe("mainMenuTabs")
    public void onTabsSelectedChange(final Tabs.SelectedChangeEvent e) {
        if (e.getSelectedTab() != null) {
            Tab selectedTab = e.getSelectedTab();
            if (selectedTab.equals(turbineTab)) {
                    viewNavigators.listView(this, Turbine.class)
                            .navigate();
            }
            if (selectedTab.equals(maintenanceTaskTab)) {
                    viewNavigators.listView(this, MaintenanceTask.class)
                            .navigate();
            }
        }
    }
}
