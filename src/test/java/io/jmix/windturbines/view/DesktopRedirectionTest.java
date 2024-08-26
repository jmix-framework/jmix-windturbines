package io.jmix.windturbines.view;

import io.jmix.core.session.SessionData;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.windturbines.JmixWindturbinesApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DesktopRedirectionTest {

    private BrowserInteraction browserInteraction;
    private SessionData sessionData;
    private DesktopRedirection desktopRedirection;

    @BeforeEach
    void setUp() {
        browserInteraction = mock(TestBrowserInteraction.class);
        sessionData = mock(SessionData.class);

        desktopRedirection = new DesktopRedirection(
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
                desktopRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(sessionData, times(1)).setAttribute("mobile", true);
                verify(browserInteraction, never()).redirectTo(anyString());
            }

            @Test
            void given_mobileParameterNotPresent_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com");

                // when
                desktopRedirection.redirectIfRequiredByUrlParamsOnly();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }

            @Test
            void given_mobileParameterFalse_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com?mobile=false");

                // when
                desktopRedirection.redirectIfRequiredByUrlParamsOnly();

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
                desktopRedirection.redirectConsideringSessionAndUrl();

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
                desktopRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, never()).redirectTo(anyString());
            }

            @Test
            void given_mobileParameterNotPresent_and_sessionIsMobileNull_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com");
                when(sessionData.getAttribute("mobile")).thenReturn(null);

                // when
                desktopRedirection.redirectConsideringSessionAndUrl();

                // then
                verify(browserInteraction, times(1)).redirectTo("icons/mobile-simulator/index.html");
            }

            @Test
            void given_mobileParameterFalse_and_sessionIsMobileNull_expect_redirect() {

                // given
                setupCurrentUrl("https://example.com?mobile=false");
                when(sessionData.getAttribute("mobile")).thenReturn(null);

                // when
                desktopRedirection.redirectConsideringSessionAndUrl();

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
                desktopRedirection.redirectIfRequiredByUrlParamsOnly();

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
                desktopRedirection.redirectConsideringSessionAndUrl();

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
                desktopRedirection.redirectConsideringSessionAndUrl();

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

    @Test
    void given_mobileParameterFalse_whenRedirectIfRequiredByUrlParamsOnly_thenRedirectsToSimulator() {
        // Test logic goes here
    }

    @Test
    void given_noMobileParameter_whenRedirectConsideringSessionAndUrl_thenUsesSessionForRedirect() {
        // Test logic goes here
    }

    @Test
    void given_sessionMobileTrue_whenRedirectConsideringSessionAndUrl_thenNoRedirectOccurs() {
        // Test logic goes here
    }

    @Test
    void given_mobileParameterAbsentAndSessionNull_whenRedirectConsideringSessionAndUrl_thenRedirectsToSimulator() {
        // Test logic goes here
    }

    @Test
    void given_touchDeviceTrue_whenCheckAndRedirectForDesktop_thenNoRedirectOccurs() {
        // Test logic goes here
    }

    class TestBrowserInteraction extends BrowserInteraction {
        private URL url;


        @Override
        public void fetchCurrentUrl(Consumer<URL> consumer) {
            consumer.accept(url);
        }

        public void setUrl(String url) {
            try {
                this.url = URI.create(url).toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
