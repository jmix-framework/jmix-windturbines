package io.jmix.windturbines.view.main;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.AbstractIcon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.kit.component.main.ListMenu;
import io.jmix.flowui.view.*;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.online.OnlineDemoMobileRedirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Route("")
@ViewController("MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {
    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private Tabs mainMenuTabs;
    @ViewComponent
    private JmixListMenu menu;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Optional<OnlineDemoMobileRedirection> onlineDemoMobileRedirection;

    private Tab turbineTab;
    private Tab inspectionsTab;

    @Subscribe
    public void onInit(final InitEvent event) {
        onlineDemoMobileRedirection.ifPresent(OnlineDemoMobileRedirection::redirectIfRequiredByUrlParamsOnly);
    }

    @Subscribe
    public void onQueryParametersChange(final QueryParametersChangeEvent event) {
        onlineDemoMobileRedirection.ifPresent(OnlineDemoMobileRedirection::redirectConsideringSessionAndUrl);
    }

    @Subscribe
    public void onAttachEvent(final AttachEvent event) {
        initMobileMenu();
        initSideMenu();
    }


    private void initSideMenu() {

        menu.addMenuItem(
                ListMenu.MenuItem.create("myProfile")
                        .withTitle(messageBundle.getMessage("myProfile"))
                        .withPrefixComponent(icon("user.svg"))
                        .withClickHandler(e -> notifications.create(messageBundle.getMessage("notImplemented"))
                                .withType(Notifications.Type.WARNING)
                                .show()
                        ),
                0
        );


        setMenuIcon("Turbine.list", "turbine-white.svg");
        setMenuIcon("Inspection.list", "inspection-white.svg");

        Span notificationsBadge = uiComponents.create(Span.class);
        notificationsBadge.getElement().getThemeList().addAll(List.of("badge", "pill"));
        notificationsBadge.setText(new Random().nextInt(10) + "");

        menu.addMenuItem(
                ListMenu.MenuItem.create("notifications")
                        .withTitle(messageBundle.getMessage("notifications"))
                        .withSuffixComponent(notificationsBadge)
                        .withClickHandler(e -> notifications.create(messageBundle.getMessage("notImplemented"))
                                .withType(Notifications.Type.WARNING)
                                .show()
                        )
        );


        menu.addMenuItem(
                ListMenu.MenuItem.create("settings")
                        .withTitle(messageBundle.getMessage("settings"))
                        .withPrefixComponent(icon("settings.svg"))
                        .withClickHandler(e -> notifications.create(messageBundle.getMessage("notImplemented"))
                                .withType(Notifications.Type.WARNING)
                                .show()
                        )
        );
        menu.addMenuItem(
                ListMenu.MenuItem.create("technicalSupport")
                        .withTitle(messageBundle.getMessage("technicalSupport"))
                        .withPrefixComponent(icon("technical-support.svg"))
                        .withClickHandler(e -> notifications.create(messageBundle.getMessage("notImplemented"))
                                .withType(Notifications.Type.WARNING)
                                .show()
                        )
        );
    }

    private void setMenuIcon(String menuItemId, String iconFilename) {
        Optional.ofNullable(menu.getMenuItem(menuItemId))
                .ifPresent(it -> it.setPrefixComponent(icon(iconFilename)));
    }

    private void initMobileMenu() {
        inspectionsTab = createTab("Inspections", "inspections", icon("inspection.svg"));
        turbineTab = createTab("Turbines", "turbines", icon("turbine.svg"));
        mainMenuTabs.add(
                inspectionsTab,
                turbineTab
        );
    }

    private SvgIcon icon(String filename) {
        StreamResource iconResource = new StreamResource(filename,
                () -> getClass().getResourceAsStream("/META-INF/resources/icons/%s".formatted(filename)));
        return new SvgIcon(iconResource);
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
            if (selectedTab.equals(inspectionsTab)) {
                viewNavigators.listView(this, Inspection.class)
                        .navigate();
            }
        }
    }
}
