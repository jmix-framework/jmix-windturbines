package io.jmix.windturbines.view.online;

import com.vaadin.flow.router.QueryParameters;
import io.jmix.core.session.SessionData;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class OnlineDemoMobileRedirection implements MobileSimulatorRedirection {

    private static final String MOBILE_SESSION_KEY = "mobile";
    private static final String REDIRECT_LOCATION = "icons/mobile-simulator/index.html";

    private final SessionData sessionData;
    private final BrowserInteraction browserInteraction;

    public OnlineDemoMobileRedirection(SessionData sessionData, BrowserInteraction browserInteraction) {
        this.sessionData = sessionData;
        this.browserInteraction = browserInteraction;
    }

    @Override
    public void redirectIfRequiredByUrlParamsOnly() {
        fetchMobileParam(mobileParam ->
                mobileParam
                        .filter(mobile -> mobile)
                        .ifPresentOrElse(
                                mobile -> markSessionAsMobile(),
                                this::checkAndRedirectForDesktop
                        )
        );
    }

    @Override
    public void redirectConsideringSessionAndUrl() {
        fetchMobileParam(mobileParam ->
                mobileParam
                        .filter(mobile -> mobile)
                        .ifPresentOrElse(
                                mobile -> markSessionAsMobile(),
                                () -> mobileParam.ifPresentOrElse(
                                        mobile -> checkAndRedirectForDesktop(),
                                        this::handleSessionBasedRedirect
                                )
                        )
        );
    }

    private void fetchMobileParam(Consumer<Optional<Boolean>> consumer) {
        browserInteraction.fetchCurrentUrl(url -> consumer.accept(parseMobileParam(url)));
    }

    private Optional<Boolean> parseMobileParam(URL url) {
        String query = url.getQuery();
        if (query != null) {
            Map<String, String> parameters = parseQueryParameters(query);
            if (parameters.containsKey(MOBILE_SESSION_KEY)) {
                return Optional.of("true".equalsIgnoreCase(parameters.get(MOBILE_SESSION_KEY)));
            }
        }
        return Optional.empty();
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


    private void handleSessionBasedRedirect() {
        Boolean mobileFromSession = (Boolean) sessionData.getAttribute(MOBILE_SESSION_KEY);
        if (mobileFromSession == null || !mobileFromSession) {
            checkAndRedirectForDesktop();
        }
    }

    private void checkAndRedirectForDesktop() {
        browserInteraction.fetchTouchDevice(isTouchDevice -> {
            if (!isTouchDevice) {
                browserInteraction.redirectTo(REDIRECT_LOCATION);
            }
            else {
                markSessionAsMobile();
            }
        });
    }

    private void markSessionAsMobile() {
        sessionData.setAttribute(MOBILE_SESSION_KEY, true);
    }
}
