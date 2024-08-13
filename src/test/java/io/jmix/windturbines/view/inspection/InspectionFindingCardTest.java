package io.jmix.windturbines.view.inspection;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.JmixWindturbinesApplication;
import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.entity.inspection.Severity;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.*;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import io.jmix.windturbines.test_support.TestDialogsConfiguration;
import io.jmix.windturbines.test_support.jmix.*;
import io.jmix.windturbines.view.inspectionfinding.InspectionFindingDetailView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@UiTest
@SpringBootTest(
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {
                JmixWindturbinesApplication.class,
                FlowuiTestAssistConfiguration.class,
                TestDialogsConfiguration.class
        })
class InspectionFindingCardTest {

    @Autowired
    UiComponents uiComponents;
    @Autowired
    Messages messages;
    @Autowired
    Fragments fragments;
    @Autowired
    ViewNavigators viewNavigators;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    TestDialogs dialogs;
    @Autowired
    private EntityTestData entityTestData;
    @Autowired
    private DialogWindows dialogWindows;

    private Inspection inspection;
    private InspectionDetailView detailView;

    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();

        Manufacturer manufacturer = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        Operator operator = entityTestData.saveWithDefaults(new OperatorData());
        Turbine turbine = entityTestData.saveWithDefaults(new TurbineData(manufacturer, operator));
        inspection = entityTestData.saveWithDefaults(new ScheduledInspectionData(turbine, null));

        dialogs.clear();
    }

    @Nested
    class RenderedText {

        @BeforeEach
        void setUp() {
            detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);
        }

        @Test
        void given_title_whenRenderComponent_then_titleIsShown() {

            // given
            InspectionFinding inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> {
                it.setTitle("Rotor blade crack");
                it.setDescription("A significant crack was found on the rotor blade");
                it.setSeverity(Severity.HIGH);
            });

            // when
            var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

            // then
            assertThat(componentText(inspectionFindingCard, "title"))
                    .isEqualTo("Rotor blade crack");
        }

        @Test
        void given_description_whenRenderComponent_then_descriptionIsShown() {

            // given
            InspectionFinding inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> {
                it.setTitle("Rotor blade crack");
                it.setDescription("A significant crack was found on the rotor blade");
                it.setSeverity(Severity.HIGH);
            });

            // when
            var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

            // then
            assertThat(componentText(inspectionFindingCard, "description"))
                    .isEqualTo("A significant crack was found on the rotor blade");
        }

    }

    @Nested
    class SeverityBadge {

        @BeforeEach
        void setUp() {
            detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);
        }

        @Test
        void given_highSeverity_whenRenderComponent_then_severityIsDisplayedAsErrorBadge() {

            // given
            InspectionFinding inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> {
                it.setSeverity(Severity.HIGH);
            });

            // when
            var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

            // then
            Span severity = severityBadge(inspectionFindingCard);

            assertThat(severity.getText())
                    .isEqualTo("High");

            assertThat(severity.getElement().getThemeList())
                    .contains("badge", "error");
        }

        @Test
        void given_mediumSeverity_whenRenderComponent_then_severityIsDisplayedAsWarningBadge() {

            // given
            InspectionFinding inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> {
                it.setSeverity(Severity.MEDIUM);
            });

            // when
            var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

            // then
            Span severity = severityBadge(inspectionFindingCard);

            assertThat(severity.getText())
                    .isEqualTo("Medium");

            assertThat(severity.getElement().getThemeList())
                    .contains("badge", "contrast");
        }

        @Test
        void given_lowSeverity_whenRenderComponent_then_severityIsDisplayedAsInfoBadge() {

            // given
            InspectionFinding inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> {
                it.setSeverity(Severity.LOW);
            });

            // when
            var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

            // then
            Span severity = severityBadge(inspectionFindingCard);

            assertThat(severity.getText())
                    .isEqualTo("Low");

            assertThat(severity.getElement().getThemeList())
                    .contains("badge", "success");
        }

        private Span severityBadge(InspectionFindingCard inspectionFindingCard) {
            return findComponent(inspectionFindingCard, "severity");
        }
    }

    @Nested
    class ActionButtons {

        private InspectionFinding inspectionFinding;

        @BeforeEach
        void setUp() {
            inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection));
        }

        private JmixButton button(InspectionFindingCard inspectionFindingCard, String readBtn) {
            return findComponent(inspectionFindingCard, readBtn);
        }

        @Nested
        class RemoveBtn {

            @Test
            void given_readOnly_then_buttonIsDisabled() {

                // given
                detailView = navigateToInReadOnly(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // then
                assertThat(removeBtn(inspectionFindingCard).isEnabled())
                        .isFalse();
            }

            @Test
            void when_performRemoval_then_itemIsRemoved() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // and
                removeBtn(inspectionFindingCard).click();

                // and
                dialogs.openedOptionDialog().closeWith(DialogAction.Type.OK);

                // then
                assertThat(detailView.getFindingsDc().getMutableItems())
                        .doesNotContain(inspectionFinding);

                // and
                assertThat(detailView.getDataContext().getRemoved())
                        .contains(inspectionFinding);
            }
            @Test
            void when_performRemoval_then_relatedRecommendationsAreRemoved() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // and
                InspectionRecommendation relatedRecommendation = entityTestData.saveWithDefaults(new InspectionRecommendationData(inspection, inspectionFinding));
                detailView.getDataContext().merge(relatedRecommendation);
                detailView.getRecommendationsDc().getMutableItems().add(relatedRecommendation);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // and
                removeBtn(inspectionFindingCard).click();

                // and
                dialogs.openedOptionDialog().closeWith(DialogAction.Type.OK);

                // then
                assertThat(detailView.getRecommendationsDc().getMutableItems())
                        .doesNotContain(relatedRecommendation);

                // and
                assertThat(detailView.getDataContext().getRemoved())
                        .contains(relatedRecommendation);
            }

            @Test
            void when_cancelRemoval_then_itemIsNotRemovedFromDataContainer() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // and
                removeBtn(inspectionFindingCard).click();

                // and
                dialogs.openedOptionDialog().closeWith(DialogAction.Type.CANCEL);

                // then
                assertThat(detailView.getFindingsDc().getMutableItems())
                        .contains(inspectionFinding);

                // and
                assertThat(detailView.getDataContext().getRemoved())
                        .doesNotContain(inspectionFinding);
            }

            private JmixButton removeBtn(InspectionFindingCard inspectionFindingCard) {
                return button(inspectionFindingCard, "removeBtn");
            }
        }
        @Nested
        class EditBtn {

            @Test
            void given_readOnly_then_buttonIsInvisible() {

                // given
                detailView = navigateToInReadOnly(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // then
                assertThat(editBtn(inspectionFindingCard).isVisible())
                        .isFalse();
            }

            @Test
            void given_notReadOnly_then_buttonIsVisible() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // then
                assertThat(editBtn(inspectionFindingCard).isVisible())
                        .isTrue();
            }


            @Test
            void when_clickEdit_then_inspectionFindingDetailViewIsDisplayed_inEditMode() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // and
                editBtn(inspectionFindingCard).click();

                // then
                InspectionFindingDetailView inspectionFindingDetailView = findDialogByType(InspectionFindingDetailView.class);

                assertThat(inspectionFindingDetailView.getEditedEntity())
                        .isEqualTo(inspectionFinding);

                // and
                assertThat(inspectionFindingDetailView.isReadOnly())
                        .isFalse();
            }

            private JmixButton editBtn(InspectionFindingCard inspectionFindingCard) {
                return button(inspectionFindingCard, "editBtn");
            }

        }
        @Nested
        class ReadBtn {

            @Test
            void given_readOnly_then_buttonIsInvisible() {

                // given
                detailView = navigateToInReadOnly(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // then
                assertThat(readBtn(inspectionFindingCard).isVisible())
                        .isTrue();
            }

            @Test
            void given_notReadOnly_then_buttonIsVisible() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // then
                assertThat(readBtn(inspectionFindingCard).isVisible())
                        .isFalse();
            }


            @Test
            void when_clickRead_then_inspectionFindingDetailViewIsDisplayed_inReadOnlyMode() {

                // given
                detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

                // when
                var inspectionFindingCard = renderComponent(detailView, inspectionFinding);

                // and
                readBtn(inspectionFindingCard).click();

                // then
                InspectionFindingDetailView inspectionFindingDetailView = findDialogByType(InspectionFindingDetailView.class);

                assertThat(inspectionFindingDetailView.getEditedEntity())
                        .isEqualTo(inspectionFinding);

                // and
                assertThat(inspectionFindingDetailView.isReadOnly())
                        .isTrue();
            }

            private JmixButton readBtn(InspectionFindingCard inspectionFindingCard) {
                return button(inspectionFindingCard, "readBtn");
            }
        }
    }

    private String componentText(InspectionFindingCard inspectionFindingCard, String componentId) {
        Span component = findComponent(inspectionFindingCard, componentId);
        return component.getElement().getTextRecursively();
    }

    private <T> T findComponent(Component root, String componentId) {
        Optional<Component> component = UiComponentUtils.findComponent(root, componentId);
        assertThat(component.isPresent()).isTrue();
        return (T) component.get();
    }

    private <V extends View<?>, E> V navigateTo(Class<V> viewClass, E entity, Class<E> entityClass) {
        viewNavigators.detailView(UiTestUtils.getCurrentView(), entityClass)
                .withViewClass(viewClass)
                .editEntity(entity)
                .navigate();
        return UiTestUtils.getCurrentView();
    }

    private <V extends View<?>, E> V navigateToInReadOnly(Class<V> viewClass, E entity, Class<E> entityClass) {
        viewNavigators.detailView(UiTestUtils.getCurrentView(), entityClass)
                .withViewClass(viewClass)
                .editEntity(entity)
                .withReadOnly(true)
                .navigate();
        return UiTestUtils.getCurrentView();
    }

    private InspectionFindingCard renderComponent(InspectionDetailView detailView, InspectionFinding inspectionFinding) {
        return detailView.createInspectionFindingCard(inspectionFinding);
    }

    private <T> T findDialogByType(Class<T> viewClass) {
        return dialogWindows.getOpenedDialogWindows().getDialogs()
                .stream().map(d -> (T) d)
                .findFirst()
                .orElse(null);
    }
}
