package io.jmix.windturbines.test_support.jmix;

import io.jmix.flowui.Views;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewRegistry;
import io.jmix.flowui.view.builder.WindowBuilder;
import io.jmix.flowui.view.builder.WindowBuilderProcessor;
import org.springframework.context.ApplicationContext;

public class TestWindowBuilderProcessor extends WindowBuilderProcessor {
    private final DialogWindowsTracking dialogWindowsTracking;

    public TestWindowBuilderProcessor(
            ApplicationContext applicationContext,
            Views views,
            ViewRegistry viewRegistry,
            UiAccessChecker uiAccessChecker
    ) {
        super(applicationContext, views, viewRegistry, uiAccessChecker);
        this.dialogWindowsTracking = new DialogWindowsTracking();
    }

    @Override
    public <V extends View<?>> DialogWindow<V> build(WindowBuilder<V> builder) {
        DialogWindow<V> dialogWindow = super.build(builder);
        dialogWindowsTracking.add(dialogWindow);
        return dialogWindow;
    }

    public DialogWindowsTracking getDialogWindowsTracking() {
        return dialogWindowsTracking;
    }
}
