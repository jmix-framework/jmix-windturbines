package io.jmix.windturbines.view.online;

import io.jmix.core.session.SessionData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class MobileSimulatorRedirectionConfig {

    @Bean("mobileSimulatorRedirection")
    @Profile("online-demo")
    @Scope(value = WebApplicationContext.SCOPE_SESSION)
    public MobileSimulatorRedirection onlineDemoMobileRedirection(SessionData sessionData, BrowserInteraction browserInteraction) {
        return new OnlineDemoMobileRedirection(sessionData, browserInteraction);
    }

    @Bean("mobileSimulatorRedirection")
    @Profile("!online-demo")
    public MobileSimulatorRedirection noopMobileSimulatorRedirection() {
        return new NoopMobileSimulatorRedirection();
    }
}