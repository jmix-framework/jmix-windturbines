package io.jmix.windturbines.test_support.jmix;

import io.jmix.core.*;
import io.jmix.flowui.UiViewProperties;
import io.jmix.flowui.Views;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewRegistry;
import io.jmix.flowui.view.builder.LookupWindowBuilder;
import io.jmix.flowui.view.builder.LookupWindowBuilderProcessor;
import org.springframework.context.ApplicationContext;

public class TestLookupWindowBuilderProcessor extends LookupWindowBuilderProcessor {
    private final DialogWindowsTracking dialogWindowsTracking;

    public TestLookupWindowBuilderProcessor(
            ApplicationContext applicationContext,
            Views views,
            ViewRegistry viewRegistry,
            Metadata metadata,
            MetadataTools metadataTools,
            DataManager dataManager,
            FetchPlans fetchPlans,
            EntityStates entityStates,
            ExtendedEntities extendedEntities,
            UiViewProperties viewProperties,
            UiAccessChecker uiAccessChecker
    ) {
        super(applicationContext, views, viewRegistry, metadata, metadataTools, dataManager, fetchPlans, entityStates, extendedEntities, viewProperties, uiAccessChecker);
        this.dialogWindowsTracking = new DialogWindowsTracking();
    }


    @Override
    public <E, V extends View<?>> DialogWindow<V> build(LookupWindowBuilder<E, V> builder) {
        DialogWindow<V> dialogWindow = super.build(builder);
        dialogWindowsTracking.add(dialogWindow);
        return dialogWindow;
    }

    public DialogWindowsTracking getDialogWindowsTracking() {
        return dialogWindowsTracking;
    }
}
