package io.jmix.windturbines.test_support.jmix;

import io.jmix.core.*;
import io.jmix.flowui.UiViewProperties;
import io.jmix.flowui.Views;
import io.jmix.flowui.sys.UiAccessChecker;
import io.jmix.flowui.view.ViewRegistry;
import io.jmix.flowui.view.builder.EditedEntityTransformer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * This class is used to override the default WindowBuilderProcessor beans with test implementations.
 * This test implementation solves getting references to opened dialog views.
 * See:
 * - <a href="https://github.com/jmix-framework/jmix/issues/2147">Issue</a>
 * - ViewInteractions
 */
@TestConfiguration
public class TestWindowBuilderProcessorConfiguration {

    @Bean("flowui_DetailWindowBuilderProcessor")
    public TestDetailWindowBuilderProcessor detailWindowBuilderProcessor(
            ApplicationContext applicationContext,
            Views views,
            ViewRegistry viewRegistry,
            Metadata metadata,
            ExtendedEntities extendedEntities,
            UiViewProperties viewProperties,
            UiAccessChecker uiAccessChecker,
            List<EditedEntityTransformer> editedEntityTransformers
    ) {
        return new TestDetailWindowBuilderProcessor(
                applicationContext,
                views,
                viewRegistry,
                metadata,
                extendedEntities,
                viewProperties,
                uiAccessChecker,
                editedEntityTransformers
        );
    }

    @Bean("flowui_LookupWindowBuilderProcessor")
    public TestLookupWindowBuilderProcessor lookupWindowBuilderProcessor(
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
        return new TestLookupWindowBuilderProcessor(
                applicationContext,
                views,
                viewRegistry,
                metadata,
                metadataTools,
                dataManager,
                fetchPlans,
                entityStates,
                extendedEntities,
                viewProperties,
                uiAccessChecker
        );
    }

    @Bean("flowui_WindowBuilderProcessor")
    public TestWindowBuilderProcessor windowBuilderProcessor(
            ApplicationContext applicationContext,
            Views views,
            ViewRegistry viewRegistry,
            UiAccessChecker uiAccessChecker
    ) {
        return new TestWindowBuilderProcessor(
                applicationContext,
                views,
                viewRegistry,
                uiAccessChecker
        );
    }
}
