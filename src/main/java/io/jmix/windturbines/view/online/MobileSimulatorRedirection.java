package io.jmix.windturbines.view.online;

public interface MobileSimulatorRedirection {

    void redirectIfRequiredByUrlParamsOnly();

    void redirectConsideringSessionAndUrl();
}
