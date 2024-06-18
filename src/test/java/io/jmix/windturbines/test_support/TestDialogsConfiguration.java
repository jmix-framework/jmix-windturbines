package io.jmix.windturbines.test_support;

import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiViewProperties;
import io.jmix.flowui.backgroundtask.BackgroundWorker;
import io.jmix.windturbines.test_support.jmix.TestDialogs;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDialogsConfiguration {

    @Bean("flowui_Dialogs")
    public TestDialogs dialogs(ApplicationContext applicationContext, Messages messages, UiViewProperties flowUiViewProperties, DialogWindows dialogWindows, UiComponents uiComponents, BackgroundWorker backgroundWorker) {
        return new TestDialogs(applicationContext, messages, flowUiViewProperties, dialogWindows, uiComponents, backgroundWorker);
    }
}
