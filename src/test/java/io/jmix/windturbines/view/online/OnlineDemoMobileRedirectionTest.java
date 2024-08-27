package io.jmix.windturbines.view.online;

import io.jmix.core.session.SessionData;
import io.jmix.windturbines.online.BrowserInteraction;
import io.jmix.windturbines.online.OnlineDemoMobileRedirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OnlineDemoMobileRedirectionTest {

    private BrowserInteraction browserInteraction;
    private SessionData sessionData;
    private OnlineDemoMobileRedirection onlineDemoMobileRedirection;

    @BeforeEach
    void setUp() {
        browserInteraction = mock(BrowserInteraction.class);
        sessionData = mock(SessionData.class);

        onlineDemoMobileRedirection = new OnlineDemoMobileRedirection(
                sessionData, browserInteraction
        );
    }

    @Nested
    class DesktopBrowser {

        @BeforeEach
        void setUp() {
            setupTouchDevice(false);
        }

        @Nested
        class RequiredByUrlParamsOnly {

            @Test
            void given_mobileParameterTrue_expect_noRedirect_and_sessionIsUpdated() {

                // given
                setupCurrentUrl("https://example.com?mobile=true");

                // when
                onlineDemoMobileRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(sessionData, times(1)).setAttribute("mobile", true);
                verify(browserInteraction, never()).redirectTo(anyString());
            }

            @Test
            void given_mobileParameterNotPresent_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com");

                // when
                onlineDemoMobileRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }

            @Test
            void given_mobileParameterFalse_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com?mobile=false");

                // when
                onlineDemoMobileRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }
        }

        @Nested
        class ConsideringSessionAndUrl {

            @Test
            void given_mobileParameterTrue_expect_noRedirect_and_sessionIsUpdated() {

                // given
                setupCurrentUrl("https://example.com?mobile=true");

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(sessionData, times(1)).setAttribute("mobile", true);
                verify(browserInteraction, never()).redirectTo(anyString());
            }

            @Test
            void given_mobileParameterNotPresent_and_sessionIsMobileTrue_expect_noRedirect() {

                // given
                setupCurrentUrl("https://example.com");
                when(sessionData.getAttribute("mobile")).thenReturn(true);

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, never()).redirectTo(anyString());
            }

            @Test
            void given_mobileParameterNotPresent_and_sessionIsMobileNull_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com");
                when(sessionData.getAttribute("mobile")).thenReturn(null);

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }

            @Test
            void given_mobileParameterFalse_and_sessionIsMobileNull_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com?mobile=false");
                when(sessionData.getAttribute("mobile")).thenReturn(null);

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }
        }
    }

    @Nested
    class MobileBrowser {

        @BeforeEach
        void setUp() {
            setupTouchDevice(true);
        }

        @Nested
        class RequiredByUrlParamsOnly {

            @Test
            void given_mobileParameterNotPresent_expect_noRedirect() {

                // given
                setupCurrentUrl("https://example.com");

                // when
                onlineDemoMobileRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(browserInteraction, never()).redirectTo(anyString());
            }
        }

        @Nested
        class ConsideringSessionAndUrl {

            @Test
            void given_mobileParameterNotPresent_and_sessionIsMobileNull_expect_noRedirect() {

                // given
                setupCurrentUrl("https://example.com");
                when(sessionData.getAttribute("mobile")).thenReturn(null);

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, never()).redirectTo(anyString());
                verify(sessionData, times(1)).setAttribute("mobile", true);
            }

            @Test
            void given_mobileParameterNotPresent_and_sessionIsMobileTrue_expect_noRedirect() {

                // given
                setupCurrentUrl("https://example.com");
                when(sessionData.getAttribute("mobile")).thenReturn(true);

                // when
                onlineDemoMobileRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, never()).redirectTo(anyString());
            }
        }
    }

    private void setupCurrentUrl(String currentUrl) {
        doAnswer(invocation -> {
            Consumer<URL> consumer = invocation.getArgument(0);
            consumer.accept(URI.create(currentUrl).toURL());
            return null;
        }).when(browserInteraction).fetchCurrentUrl(any());
    }

    private void setupTouchDevice(Boolean touchDevice) {
        doAnswer(invocation -> {
            Consumer<Boolean> consumer = invocation.getArgument(0);
            consumer.accept(touchDevice);
            return null;
        }).when(browserInteraction).fetchTouchDevice(any());
    }
}
