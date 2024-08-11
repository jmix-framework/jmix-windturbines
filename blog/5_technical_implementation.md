### 5. Technical Implementation of the Example Application

In this section, we will dive into the technical implementation details of the Jmix Wind Turbines application. This
includes how to enable PWA functionality, the use of Vaadin components like VirtualList, standard Jmix views, and
customizing styles.

### Enabling PWA Functionality

To enable PWA functionality in your Jmix application, you use the `@PWA` annotation from Vaadin. This annotation
transforms your web application into a Progressive Web App (PWA), which can be installed on a user’s device, providing
an app-like experience. The `@PWA` annotation includes several configuration options such
as `name`, `shortName`, `backgroundColor`, and `display`, which determine how the PWA behaves.

Here is an example of enabling PWA functionality in a Jmix application:

```java

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

The `display` attribute defines how the web app is presented. There are four main modes:

Standalone and fullscreen modes are designed to run the app in its own window, separate from the browser. In these
modes, browser UI elements like the address bar and navigation are hidden, creating a focused, immersive interface. This
is particularly suitable for applications that need a distraction-free environment, such as messaging apps, productivity
tools, or media viewers. Fullscreen mode goes a step further by using the entire screen, hiding even the status bar,
making it ideal for applications like games or photo galleries that benefit from a completely immersive experience.

Minimal-UI mode is similar to standalone mode but retains some browser UI elements like the address bar, which cannot be
typed into. This mode is useful for applications that require some browser features, providing a balance between a full
browser experience and a standalone app. It is beneficial for simple web pages that need basic browser functionalities.

Browser mode, on the other hand, makes the app behave like a regular web page within a browser tab, retaining the full
browser UI. This mode is best for content that requires extensive navigation or multi-tab support, such as news websites
or blogs.

When choosing the appropriate display mode, consider the specific needs of your application. Standalone and fullscreen
modes are excellent for field maintenance apps where technicians require a focused interface without browser
distractions. Minimal-UI mode can be useful for applications that still need some browser functionalities like simple
navigation or basic controls. Browser mode is ideal for content-heavy applications that need extensive navigation, such
as documentation or news sites.

Different display modes can be applied based on these scenarios. For instance, a standalone mode can be used for field
maintenance apps where technicians need a focused interface without browser distractions. Fullscreen mode is best suited
for applications requiring a fully immersive experience like interactive data visualization tools. Minimal-UI mode is
useful for applications needing basic navigation controls, and browser mode is ideal for content-heavy applications that
require extensive navigation.

When deciding which display mode to use, consider whether your application has its own back-navigation and refresh
mechanisms. Standalone and fullscreen modes are appropriate if browser navigation elements are not needed. If some
browser controls are still necessary for navigation or other functionalities, minimal-UI or browser modes should be
chosen.

By leveraging these features, you can create a seamless and engaging user experience for your web applications. These
display modes can significantly impact the user experience by providing the right level of browser control and immersion
depending on the application's needs.