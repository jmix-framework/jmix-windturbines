package io.jmix.windturbines.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class DesktopRedirection {

    private static final String MOBILE = "mobile";

    @Autowired
    private SessionData sessionData;

    public void redirectIfRequiredByUrlParamsOnly() {
        fetchMobileParam(mobileParam ->
                mobileParam.ifPresentOrElse(mobile -> {
                            if (mobile) {
                                sessionData.setAttribute(MOBILE, true);
                            } else {
                                checkAndRedirectForDesktop();
                            }
                        }, this::checkAndRedirectForDesktop
                ));
    }

    public void redirectConsideringSessionAndUrl() {
        fetchMobileParam(mobileParam -> {
            if (mobileParam.isPresent()) {
                if (mobileParam.get()) {
                    sessionData.setAttribute(MOBILE, true);
                } else {
                    checkAndRedirectForDesktop();
                }
            } else {
                handleSessionBasedRedirect();
            }
        });
    }

    private void fetchMobileParam(Consumer<Optional<Boolean>> consumer) {
        UI.getCurrent().getPage().fetchCurrentURL(url -> {
            String query = url.getQuery();
            Optional<Boolean> mobileParam = Optional.empty();
            if (query != null) {
                Map<String, String> parameters = parseQueryParameters(query);
                if (parameters.containsKey(MOBILE)) {
                    mobileParam = Optional.of("true".equalsIgnoreCase(parameters.get(MOBILE)));
                }
            }
            consumer.accept(mobileParam);
        });
    }

    private void handleSessionBasedRedirect() {
        Boolean mobileFromSession = (Boolean) sessionData.getAttribute(MOBILE);
        if (mobileFromSession == null || !mobileFromSession) {
            checkAndRedirectForDesktop();
        }
    }

    private Map<String, String> parseQueryParameters(String query) {
        return QueryParameters.fromString(query).getParameters()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> String.join(",", entry.getValue())
                ));
    }

    private void checkAndRedirectForDesktop() {
        UI ui = UI.getCurrent();
        ui.getPage().retrieveExtendedClientDetails(details -> {
            if (!details.isTouchDevice()) {
                ui.getPage().setLocation("icons/mobile-simulator/index.html");
            }
        });
    }
}
