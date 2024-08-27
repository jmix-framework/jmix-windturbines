package io.jmix.windturbines.online;

import com.vaadin.flow.component.UI;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.function.Consumer;

@Component
public class BrowserInteraction {

    public void fetchCurrentUrl(Consumer<URL> consumer) {
        UI.getCurrent().getPage().fetchCurrentURL(consumer::accept);
    }

    public void redirectTo(String location) {
        UI.getCurrent().getPage().setLocation(location);
    }

    public void fetchTouchDevice(Consumer<Boolean> consumer) {
        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
            consumer.accept(details.isTouchDevice());
        });
    }
}
