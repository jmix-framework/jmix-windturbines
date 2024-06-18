package io.jmix.windturbines.view.turbine;

import com.vaadin.flow.component.HasValue;
import io.jmix.core.DataManager;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.JmixWindturbinesApplication;
import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.TurbineStatus;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.ManufacturerData;
import io.jmix.windturbines.test_data.entity.OperatorData;
import io.jmix.windturbines.test_data.entity.TurbineData;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@UiTest
@SpringBootTest(classes = {JmixWindturbinesApplication.class, FlowuiTestAssistConfiguration.class})
public class TurbineDetailViewTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    ViewNavigators viewNavigators;

    private Turbine vestasV150;
    private Operator skywind;
    private Manufacturer vestas;

    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();
        vestas = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        skywind = entityTestData.saveWithDefaults(new OperatorData(), it -> {
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
            it.setLastMaintenanceDate(LocalDate.now().minusMonths(1));
            it.setInstallationDate(LocalDate.now().minusYears(3));
        });
    }

    @Test
    void when_openDetail_then_detailViewIsShown() {

        // given
        TurbineDetailView detailView = navigateTo(TurbineDetailView.class, vestasV150, Turbine.class);

        // expect
        assertThat(detailView.getEditedEntity())
                .isEqualTo(vestasV150);
    }

    @Test
    void when_openDetail_then_detailsTabShowsTurbineDetails() {

        // when
        TurbineDetailView detailView = navigateTo(TurbineDetailView.class, vestasV150, Turbine.class);

        // then
        assertThat(fieldValue(detailView, "turbineIdField"))
                .isEqualTo("#738901");
        assertThat(fieldValue(detailView, "statusField"))
                .isEqualTo(TurbineStatus.OPERATING);
        assertThat(fieldValue(detailView, "manufacturerField"))
                .isEqualTo(vestas);
        assertThat(fieldValue(detailView, "modelField"))
                .isEqualTo("V150-4.2 MW");
        assertThat(fieldValue(detailView, "locationField"))
                .isEqualTo("Santa Barbara, California");
        assertThat(fieldValue(detailView, "heightField"))
                .isEqualTo("150");
        assertThat(fieldValue(detailView, "rotorDiameterField"))
                .isEqualTo("120");
        assertThat(fieldValue(detailView, "lastMaintenanceDateField"))
                .isEqualTo(LocalDate.now().minusMonths(1));
        assertThat(fieldValue(detailView, "installationDateField"))
                .isEqualTo(LocalDate.now().minusYears(3));
    }

    @Test
    void when_openDetail_then_operatorTabShowsOperatorDetails() {

        // when
        TurbineDetailView detailView = navigateTo(TurbineDetailView.class, vestasV150, Turbine.class);

        JmixTabSheet tabSheet = UiTestUtils.getComponent(detailView, "contentTabSheet");
        tabSheet.setSelectedTab(tabSheet.getTabAt(1));

        // then
        assertThat(fieldValue(detailView, "operatorNameField"))
                .isEqualTo("SkyWind Enterprises");

        assertThat(fieldValue(detailView, "operatorContactPersonField"))
                .isEqualTo("John Doe");

        assertThat(fieldValue(detailView, "operatorAddressField"))
                .isEqualTo("123 Windy Lane");

        assertThat(fieldValue(detailView, "operatorEmailField"))
                .isEqualTo("info@skywind.com");

        assertThat(fieldValue(detailView, "operatorPhoneField"))
                .isEqualTo("+1 555 1234");

    }

    private static Object fieldValue(View<?> view, String componentId) {
        return ((HasValue) UiTestUtils.getComponent(view, componentId)).getValue();
    }

    private <V extends View<?>, E> V navigateTo(Class<V> viewClass, E entity, Class<E> entityClass) {
        viewNavigators.detailView(entityClass)
                .withViewClass(viewClass)
                .editEntity(entity)
                .withReadOnly(true)
                .navigate();
        return UiTestUtils.getCurrentView();
    }
}
