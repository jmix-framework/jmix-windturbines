package io.jmix.windturbines.security;

import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.*;
import io.jmix.windturbines.test_support.AuthenticatedAsAdmin;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
public class TechnicanRoleTest {


    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    SystemAuthenticator systemAuthenticator;

    private Turbine vestasV150;
    private Turbine vestasV160;
    private Turbine siemensSG5;
    private User mike;
    private User tom;
    private Inspection mikeVestasV160TomorrowInspection;
    private Inspection mikeVestasV150TodayInspection;
    private Inspection tomSiemensSG5NextWeekInspection;
    private Manufacturer vestas;
    private Manufacturer siemens;
    private Operator skywind;
    private Operator windtech;


    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();

        vestas = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        siemens = entityTestData.saveWithDefaults(new ManufacturerData("Siemens"));

        skywind = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("SkyWind Enterprises");
            it.setContactPerson("John Doe");
            it.setAddress("123 Windy Lane");
        });
        windtech = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("WindTech Innovations Inc.");
            it.setContactPerson("Jane Smith");
            it.setAddress("456 Breeze Blvd");
        });

        vestasV150 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738901");
            it.setModel("V150-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });

        mike = entityTestData.saveWithDefaults(new TechnicanData());
        entityTestData.saveWithDefaults(new TechnicanRoleData(mike));

        tom = entityTestData.saveWithDefaults(new TechnicanData());
        entityTestData.saveWithDefaults(new TechnicanRoleData(tom));

        mikeVestasV150TodayInspection = entityTestData.saveWithDefaults(new InspectionData(vestasV150, mike), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now());
        });

        vestasV160 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738902");
            it.setModel("V160-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });

        mikeVestasV160TomorrowInspection = entityTestData.saveWithDefaults(new InspectionData(vestasV160, mike), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now().plusDays(1));
        });

        siemensSG5 = entityTestData.saveWithDefaults(new TurbineData(siemens, windtech), it -> {
            it.setTurbineId("#892920");
            it.setModel("SG 5.8-155");
            it.setStatus(TurbineStatus.MAINTENANCE);
            it.setLocation("Palm Springs, California");
        });


        tomSiemensSG5NextWeekInspection = entityTestData.saveWithDefaults(new InspectionData(siemensSG5, tom), it -> {
            it.setTaskStatus(TaskStatus.SCHEDULED);
            it.setInspectionDate(LocalDate.now().plusDays(7));
        });

        systemAuthenticator.begin(mike.getUsername());
    }

    @Nested
    class InspectionPermissions {

        @Test
        void readAllowed() {
            // expect
            assertThat(dataManager.load(Inspection.class)
                    .all().list())
                    .containsExactlyInAnyOrder(
                            mikeVestasV150TodayInspection,
                            mikeVestasV160TomorrowInspection,
                            tomSiemensSG5NextWeekInspection
                    );
        }

        @Test
        void createAllowed() {

            // given
            Inspection inspection = entityTestData.createWithDefaults(new InspectionData(vestasV150, mike));

            // when
            dataManager.save(inspection);

            // then
            assertThat(entityTestData.reload(Id.of(inspection)))
                    .isEqualTo(inspection);
        }

        @Test
        void updateAllowed() {

            // given
            Inspection inspection = entityTestData.saveWithDefaults(new InspectionData(vestasV150, mike));

            // and
            Inspection reloadedInspection = entityTestData.reload(Id.of(inspection));
            reloadedInspection.setTaskStatus(TaskStatus.COMPLETED);

            // when
            dataManager.save(reloadedInspection);

            // then
            assertThat(entityTestData.reload(Id.of(inspection)).getTaskStatus())
                    .isEqualTo(TaskStatus.COMPLETED);
        }

        @Test
        void deletionForbidden() {

            // given
            Inspection inspection = entityTestData.saveWithDefaults(new InspectionData(vestasV150, mike));

            // when
            assertThatThrownBy(() -> dataManager.remove(entityTestData.reload(Id.of(inspection))))
                    .isInstanceOf(AccessDeniedException.class);

            // then
            assertThat(entityTestData.reload(Id.of(inspection)))
                    .isEqualTo(inspection);
        }
    }

    @Nested
    class TurbinePermissions {

        @Test
        void readAllowed() {
            // expect
            assertThat(dataManager.load(Turbine.class)
                    .all().list())
                    .containsExactlyInAnyOrder(
                            vestasV150,
                            vestasV160,
                            siemensSG5
                    );
        }

        @Test
        void createForbidden() {
            // expect
            assertThatThrownBy(() -> entityTestData.saveWithDefaults(new TurbineData(vestas, skywind)))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void updateForbidden() {
            // given
            vestasV150.setHeight(200);
            // expect
            assertThatThrownBy(() -> dataManager.save(vestasV150))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void deletionForbidden() {
            // expect
            assertThatThrownBy(() -> dataManager.remove(vestasV150))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @Nested
    class ManufacturerPermissions {

        @Test
        void readAllowed() {
            // expect
            assertThat(dataManager.load(Manufacturer.class)
                    .all().list())
                    .containsExactlyInAnyOrder(
                            vestas, siemens
                    );
        }

        @Test
        void createForbidden() {
            // expect
            assertThatThrownBy(() -> entityTestData.saveWithDefaults(new ManufacturerData("GE")))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void updateForbidden() {
            // given
            vestas.setName("New Vestas");
            // expect
            assertThatThrownBy(() -> dataManager.save(vestas))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void deletionForbidden() {
            // expect
            assertThatThrownBy(() -> dataManager.remove(vestas))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @Nested
    class OperatorPermissions {

        @Test
        void readAllowed() {
            // expect
            assertThat(dataManager.load(Operator.class)
                    .all().list())
                    .containsExactlyInAnyOrder(
                            skywind, windtech
                    );
        }

        @Test
        void createForbidden() {
            // expect
            assertThatThrownBy(() -> entityTestData.saveWithDefaults(new OperatorData()))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void updateForbidden() {
            // given
            windtech.setName("New WindTech");
            // expect
            assertThatThrownBy(() -> dataManager.save(windtech))
                    .isInstanceOf(AccessDeniedException.class);
        }

        @Test
        void deletionForbidden() {
            // expect
            assertThatThrownBy(() -> dataManager.remove(windtech))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @AfterEach
    void tearDown() {
        systemAuthenticator.end();
    }
}
