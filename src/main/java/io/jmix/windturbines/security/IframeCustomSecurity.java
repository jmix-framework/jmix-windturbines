package io.jmix.windturbines.security;

import io.jmix.securityflowui.security.FlowuiVaadinWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
public class IframeCustomSecurity extends FlowuiVaadinWebSecurity {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http.headers(headers -> {
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
            headers.contentSecurityPolicy(
                    secPolicy -> secPolicy.policyDirectives("frame-ancestors *")
            );
        });
    }
}