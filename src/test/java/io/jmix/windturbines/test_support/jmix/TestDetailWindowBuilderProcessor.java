package io.jmix.windturbines.test_support.jmix;

import io.jmix.core.ExtendedEntities;
import io.jmix.core.Metadata;
import io.jmix.flowui.UiViewProperties;
import io.jmix.flowui.Views;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.DialogWindow;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewRegistry;
import io.jmix.flowui.view.builder.DetailWindowBuilder;
import io.jmix.flowui.view.builder.DetailWindowBuilderProcessor;
import io.jmix.flowui.view.builder.EditedEntityTransformer;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class TestDetailWindowBuilderProcessor extends DetailWindowBuilderProcessor {
    private final DialogWindowsTracking dialogWindowsTracking;

    public TestDetailWindowBuilderProcessor(
            ApplicationContext applicationContext,
            Views views,
            ViewRegistry viewRegistry,
            Metadata metadata,
            ExtendedEntities extendedEntities,
            UiViewProperties viewProperties,
            UiAccessChecker uiAccessChecker,
            List<EditedEntityTransformer> editedEntityTransformers
    ) {
        super(applicationContext, views, viewRegistry, metadata, extendedEntities, viewProperties, uiAccessChecker, editedEntityTransformers);
        this.dialogWindowsTracking = new DialogWindowsTracking();
    }

    @Override
    public <E, V extends View<?>> DialogWindow<V> build(DetailWindowBuilder<E, V> builder) {
        DialogWindow<V> dialogWindow = super.build(builder);
        dialogWindowsTracking.add(dialogWindow);
        return dialogWindow;
    }

    public DialogWindowsTracking getDialogWindowsTracking() {
        return dialogWindowsTracking;
    }
}
