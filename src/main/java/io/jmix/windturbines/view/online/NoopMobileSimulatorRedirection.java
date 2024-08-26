package io.jmix.windturbines.view.online;

public class NoopMobileSimulatorRedirection implements MobileSimulatorRedirection {

    @Override
    public void redirectIfRequiredByUrlParamsOnly() {
        // noop
    }

    @Override
    public void redirectConsideringSessionAndUrl() {
        // noop
    }
}
