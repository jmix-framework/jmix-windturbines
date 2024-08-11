### 5. Technical Implementation of the Example Application

In this section, we will dive into the technical implementation details of the Jmix Wind Turbines application. This
includes how to enable PWA functionality, the use of mobile optimised Vaadin components like VirtualList, leveraging
standard Jmix views, and customizing styles to create a good look & feel on a mobile device.

### Enabling PWA Functionality

To enable PWA functionality in your Jmix application, you use the `@PWA` annotation from Vaadin. By default eery Jmix
application has this annotation already active by default when creating a new application. This annotation
transforms your web application into a Progressive Web App (PWA), which can be installed on a user’s device, providing
an app-like experience. The `@PWA` annotation includes several configuration options such
as `name`, `shortName`, `backgroundColor`, and `display`, which determine how the PWA behaves.

Here is an example of enabling PWA functionality in the Jmix Windturbines application:

```java
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

@Push
@Theme(value = "jmix-windturbines")
@PWA(name = "Windturbines", shortName = "Jmix WT", backgroundColor = "#235FD5", display = "standalone")
@SpringBootApplication
public class JmixWindturbinesApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(JmixWindturbinesApplication.class, args);
    }
}
```

The `display` attribute defines how the web app is presented on the mobile operating system. There are four main modes:

Standalone and fullscreen modes are designed to run the app in its own window, separate from the browser. In these
modes, browser UI elements like the address bar and navigation are hidden, creating a focused, immersive interface. This
is particularly suitable for applications that supposed to look very close like a native application. Fullscreen mode
goes a step further by using the entire screen, hiding even the status bar, making it ideal for applications like games
or photo galleries that benefit from a completely immersive experience.

<<Screenshot of standalone mode for Windturbines>>

Minimal-UI mode is similar to standalone mode but retains some browser UI elements like the address bar, which cannot be
typed into. This mode is useful for applications that require some browser features like browser refresh or the back
button, providing a balance between a full browser experience and a standalone app. It is beneficial for simple web
pages that need basic browser functionalities.

<<Screenshot of minimal mode for Windturbines>>

Browser mode, on the other hand, makes the app behave like a regular web page within a browser tab, retaining the full
browser UI. This mode is best for content that requires extensive navigation or multi-tab support, such as news websites
or blogs.

For the Jmix Windturbines app, we use the standalone mode, which is also the default setting from Vaadin's `@PWA`
annotation.

### Optimizing Mobile Data Views via Card Layout

In Jmix applications, list views are typically generated using DataGrids. While they are efficient for desktop views,
they are not optimal for mobile interfaces due to the horizontal scrolling required to view multiple columns.
Representing a list of items on mobile is typically done by displaying each item underneath the other. One effective way
to achieve this is by using a card layout.

Using a card layout improves the user experience on mobile devices. It organizes information into distinct, easily
readable sections that fit well within the vertical space of a mobile screen. This layout is technically supported by
Vaadin's `VirtualList`, allowing for the creation of a dynamic, responsive UI.

To create a more mobile-friendly interface, you can start with a standard CRUD UI and then replace the DataGrid with
the `<virtualList />` XML tag. The data binding is the same as for the DataGrid. The difference is that instead of
defining a set of columns to render, you define a handler function that returns a component to be displayed for each
item.

Let's explore how we used the `VirtualList` component for the Turbine List View. Here is the XML view descriptor:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<view xmlns="http://jmix.io/schema/flowui/view"
      title="msg://turbineListView.title"
      focusComponent="turbinesVirtualList">
    <layout spacing="false" padding="false">
        <!-- ... -->
        <vbox classNames="layout-body" width="100%" height="100%" padding="false" spacing="false">
            <vbox width="100%" classNames="light-background" padding="false">
                <scroller width="100%" height="100%">
                    <virtualList id="turbinesVirtualList"
                                 width="100%"
                                 height="100%"
                                 itemsContainer="turbinesDc"
                                 alignSelf="CENTER"/>
                </scroller>
            </vbox>
        </vbox>
    </layout>
</view>
```

We use the `<virtualList />` tag to create a virtual list and bind it to the `turbinesDc` data container. The next step is that we have to specify programmatically how we display one particular turbine instance. For this, we create a renderer supplier in the Java controller of the list view:

```java

@Route(value = "turbines", layout = MainView.class)
@ViewController("Turbine.list")
@ViewDescriptor("turbine-list-view.xml")
@DialogMode(width = "64em")
public class TurbineListView extends StandardListView<Turbine> {

    @Autowired
    private Fragments fragments;

    @Supply(to = "turbinesVirtualList", subject = "renderer")
    private Renderer<Turbine> turbinesVirtualListRenderer() {
        return new ComponentRenderer<>(this::createTurbineCard);
    }

    private TurbineCard createTurbineCard(Turbine turbine) {
        TurbineCard turbineCard = fragments.create(this, TurbineCard.class);
        turbineCard.setTurbine(turbine);
        turbineCard.setOrigin(this);
        return turbineCard;
    }
}
```

### Utilizing Fragments for Reusable Components

Fragments are a mechanism to define reusable components declaratively using XML. This functionality is particularly useful for creating complex UI components that can be reused across different parts of the application.

In our example, we used Fragments to create a `TurbineCard` component. This component encapsulates all the necessary UI
elements and logic to display a turbine's details in a card layout. By using Fragments, we can define the layout definition of the turbine card in XML in the same way it works for general views.

Here is how we defined a `TurbineCard` fragment via XML:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<fragment xmlns="http://jmix.io/schema/flowui/fragment">
    <actions>
        <action id="detailsAction"/>
    </actions>
    <content>
        <vbox id="root"
              padding="false"
              spacing="false"
              classNames="white-card cursor-pointer turbine-list-white-card m-m">
            <hbox width="100%" classNames="gap-m p-s">
                <span id="turbineId" classNames="font-bold"/>
                <hbox justifyContent="END" width="100%">
                    <span id="statusBadge"
                          width="100px"
                          themeNames="badge pill"
                          classNames="turbine-status"/>
                </hbox>
            </hbox>
            <hbox width="100%" classNames="gap-xl p-s">
                <span id="manufacturerName"/>
                <span id="model"/>
            </hbox>
            <hbox width="100%" classNames="gap-m p-s">
                <hbox alignItems="CENTER" width="100%">
                    <svgIcon resource="/icons/location.svg"/>
                    <span id="location"
                          width="100%"
                          classNames="cut-overflow-text"/>
                </hbox>
                <hbox padding="false" justifyContent="END">
                    <button action="detailsAction" icon="CHEVRON_RIGHT"
                            themeNames="tertiary-inline"/>
                </hbox>
            </hbox>
        </vbox>
    </content>
</fragment>
```

In the Java controller we define how the data is set for the components from the turbine instance.

```java
@FragmentDescriptor("turbine-card.xml")
public class TurbineCard extends Fragment<VerticalLayout> {

    // ...

    @ViewComponent
    private Span turbineId;
    @ViewComponent
    private Span statusBadge;
    @ViewComponent
    private Span model;
    @ViewComponent
    private Span manufacturerName;
    @ViewComponent
    private Span location;

    private Turbine turbine;

    public void setTurbine(Turbine turbine) {
        this.turbine = turbine;
        turbineId.setText(turbine.getTurbineId());
        statusBadge.setText(messages.getMessage(turbine.getStatus()));
        statusBadge.getElement().getThemeList().add(turbine.getStatus().getBadgeThemeName());
        manufacturerName.setText(turbine.getManufacturer().getName());
        model.setText(turbine.getModel());
        location.setText(turbine.getLocation());
    }
}
```

By leveraging Vaadin's `VirtualList` and Jmix's Fragments, we can create highly optimized, mobile-friendly list views that enhance the user experience. This approach allows for efficient data display and easy maintenance of reusable UI components.