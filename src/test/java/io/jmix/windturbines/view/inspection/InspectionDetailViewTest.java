package io.jmix.windturbines.view.inspection;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.accordion.JmixAccordion;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.JmixWindturbinesApplication;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.ManufacturerData;
import io.jmix.windturbines.test_data.entity.OperatorData;
import io.jmix.windturbines.test_data.entity.ScheduledInspectionData;
import io.jmix.windturbines.test_data.entity.TurbineData;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@UiTest
@SpringBootTest(classes = {JmixWindturbinesApplication.class, FlowuiTestAssistConfiguration.class})
public class InspectionDetailViewTest {

    private final LocalDate MAINTENANCE_TASK_DATE = LocalDate.now();
    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    ViewNavigators viewNavigators;
    private Turbine vestasV150;
    private Manufacturer vestas;
    private Inspection inspection;


    private static Object fieldValue(View<?> view, String componentId) {
        return ((HasValue) UiTestUtils.getComponent(view, componentId)).getValue();
    }

    private static JmixButton button(View<?> view, String componentId) {
        return UiTestUtils.getComponent(view, componentId);
    }

    private static DatePicker datePicker(View<?> view, String componentId) {
        return UiTestUtils.getComponent(view, componentId);
    }

    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();
        vestas = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        Operator skywind = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("SkyWind Enterprises");
            it.setContactPerson("John Doe");
            it.setAddress("123 Windy Lane");
            it.setEmail("info@skywind.com");
            it.setPhone("+1 555 1234");
        });
        vestasV150 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738901");
            it.setModel("V150-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
            it.setHeight(150);
            it.setRotorDiameter(120);
            it.setLastMaintenanceDate(MAINTENANCE_TASK_DATE.minusMonths(1));
            it.setInstallationDate(MAINTENANCE_TASK_DATE.minusYears(3));
        });

        inspection = entityTestData.saveWithDefaults(new ScheduledInspectionData(vestasV150, null), it -> {
            it.setMaintenanceTaskDate(MAINTENANCE_TASK_DATE);
            it.setTaskStatus(TaskStatus.SCHEDULED);
        });
    }

    @Test
    void when_openDetail_then_detailViewIsShown() {

        // given
        InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

        // expect
        assertThat(detailView.getEditedEntity())
                .isEqualTo(inspection);
    }

    @Test
    void when_openDetail_then_detailsPanelShowsInspectionDetails() {

        // when
        InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

        // then
        assertThat(fieldValue(detailView, "turbineField"))
                .isEqualTo(vestasV150);
        assertThat(fieldValue(detailView, "typeField"))
                .isEqualTo(TaskType.INSPECTION);
        assertThat(fieldValue(detailView, "maintenanceTaskDateField"))
                .isEqualTo(MAINTENANCE_TASK_DATE);
        assertThat(fieldValue(detailView, "technicanField"))
                .isEqualTo(null);
    }

    private <V extends View<?>, E> V navigateTo(Class<V> viewClass, E entity, Class<E> entityClass) {
        viewNavigators.detailView(entityClass)
                .withViewClass(viewClass)
                .editEntity(entity)
                .navigate();
        return UiTestUtils.getCurrentView();
    }

    @Nested
    class WizardBehavior {

        @Test
        void when_openDetail_then_prevNextAreShown_and_finishIsNotShown() {
            // when
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // then
            assertThat(prev(detailView).isVisible())
                    .isTrue();
            assertThat(next(detailView).isVisible())
                    .isTrue();

            // and
            assertThat(finish(detailView).isVisible())
                    .isFalse();
        }

        @Test
        void when_openDetail_then_nextIsEnabled_and_prevIsDisabled() {
            // when
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // then
            assertThat(prev(detailView).isEnabled())
                    .isFalse();
            assertThat(next(detailView).isEnabled())
                    .isTrue();
        }

        @Test
        void when_next_then_prevBtnIsEnabled() {
            // given
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // and
            assertThat(currentAccordionIndex(detailView))
                    .isEqualTo(0);

            // when
            next(detailView).click();

            // then
            assertThat(prev(detailView).isEnabled())
                    .isTrue();
        }

        @Test
        void when_next_then_nextPanelIsShown() {
            // given
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // and
            assertThat(currentAccordionIndex(detailView))
                    .isEqualTo(0);

            // when
            next(detailView).click();

            // then
            assertThat(currentAccordionIndex(detailView))
                    .isEqualTo(1);

        }

        @Test
        void when_lastPanel_then_nextIsInvisible_and_finishIsVisible() {
            // given
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // when
            next(detailView).click();
            next(detailView).click();
            next(detailView).click();

            // then
            assertThat(next(detailView).isVisible())
                    .isFalse();

            // and
            assertThat(finish(detailView).isVisible())
                    .isTrue();
        }

        @Test
        void when_finish_then_valuesArSaved_and_statusIsCompleted() {
            // given
            InspectionDetailView detailView = navigateTo(InspectionDetailView.class, inspection, Inspection.class);

            // given
            LocalDate newMaintenanceTaskDate = LocalDate.now().minusDays(1);
            datePicker(detailView, "maintenanceTaskDateField").setValue(newMaintenanceTaskDate);


            // and
            next(detailView).click();
            next(detailView).click();
            next(detailView).click();

            // when
            finish(detailView).click();

            // then
            Inspection savedInspection = entityTestData.reload(Id.of(inspection));

            assertThat(savedInspection.getTaskStatus())
                    .isEqualTo(TaskStatus.COMPLETED);
            assertThat(savedInspection.getMaintenanceTaskDate())
                    .isEqualTo(newMaintenanceTaskDate);
        }

        private static JmixButton next(InspectionDetailView detailView) {
            return button(detailView, "nextBtn");
        }
        private static JmixButton prev(InspectionDetailView detailView) {
            return button(detailView, "prevBtn");
        }
        private static JmixButton finish(InspectionDetailView detailView) {
            return button(detailView, "finishBtn");
        }
        private static int currentAccordionIndex(InspectionDetailView detailView) {
            return accordion(detailView).getOpenedIndex().orElse(-1);
        }

        private static JmixAccordion accordion(InspectionDetailView detailView) {
            return UiTestUtils.getComponent(detailView, "mainAccordion");
        }
    }

}
