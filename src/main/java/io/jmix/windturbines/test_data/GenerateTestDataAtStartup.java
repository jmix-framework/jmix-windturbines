package io.jmix.windturbines.test_data;

import io.jmix.core.security.Authenticated;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.test_data.entity.*;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

import static io.jmix.windturbines.test_data.RandomValues.*;

@Component
public class GenerateTestDataAtStartup {

    private static final Logger log = LoggerFactory.getLogger(GenerateTestDataAtStartup.class);
    private final EntityTestData entityTestData;

    public GenerateTestDataAtStartup(EntityTestData entityTestData) {
        this.entityTestData = entityTestData;
    }

    @Authenticated
    @EventListener
    public void onApplicationEvent(ApplicationStartedEvent event) {
        generateAndSaveTestData();
    }

    private void generateAndSaveTestData() {

        List<Manufacturer> manufacturers = entityTestData.loadAll(Manufacturer.class);

        if (!manufacturers.isEmpty()) {
            log.info("Test data already exists, skipping generation");
            return;
        }

        Manufacturer vestas = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        Manufacturer siemens = entityTestData.saveWithDefaults(new ManufacturerData("Siemens"));
        Manufacturer ge = entityTestData.saveWithDefaults(new ManufacturerData("GE"));

        Operator skywind = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("SkyWind Enterprises");
            it.setContactPerson("John Doe");
            it.setAddress("123 Windy Lane");
        });
        Operator windtech = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("WindTech Innovations Inc.");
            it.setContactPerson("Jane Smith");
            it.setAddress("456 Breeze Blvd");
        });
        Operator ecopower = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("EcoPower Corporation");
            it.setContactPerson("Jim Brown");
            it.setAddress("789 Gusty St");
        });
        Operator greenfield = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("GreenField Energy");
            it.setContactPerson("Jenny White");
            it.setAddress("101 Tempest Ave");
        });
        Operator renewableDynamics = entityTestData.saveWithDefaults(new OperatorData(), it -> {
            it.setName("Renewable Dynamics LLC");
            it.setContactPerson("Jack Black");
            it.setAddress("202 Zephyr Dr");
        });

        Turbine vestasV150 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738901");
            it.setModel("V150-4.2 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Santa Barbara, California");
        });
        addInspectionsForTurbine(vestasV150);
        Turbine siemensSG5 = entityTestData.saveWithDefaults(new TurbineData(siemens, windtech), it -> {
            it.setTurbineId("#892920");
            it.setModel("SG 5.8-155");
            it.setStatus(TurbineStatus.MAINTENANCE);
            it.setLocation("Palm Springs, California");
        });
        addInspectionsForTurbine(siemensSG5);
        Turbine vestasV162 = entityTestData.saveWithDefaults(new TurbineData(vestas, ecopower), it -> {
            it.setTurbineId("#132003");
            it.setModel("V162-6.0 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Amarillo, Texas");
        });
        addInspectionsForTurbine(vestasV162);
        Turbine vestasV160 = entityTestData.saveWithDefaults(new TurbineData(vestas, skywind), it -> {
            it.setTurbineId("#738902");
            it.setModel("V160-4.2 MW");
            it.setStatus(TurbineStatus.IDLE);
            it.setLocation("Santa Barbara, California");
        });
        addInspectionsForTurbine(vestasV160);
        Turbine geHaliade = entityTestData.saveWithDefaults(new TurbineData(ge, renewableDynamics), it -> {
            it.setTurbineId("#236601");
            it.setModel("Haliade-X 12 MW");
            it.setStatus(TurbineStatus.OPERATING);
            it.setLocation("Cheyenne, Wyoming");
        });
        addInspectionsForTurbine(geHaliade);
    }

    private void addInspectionsForTurbine(Turbine turbine) {

        List<User> users = entityTestData.loadAll(User.class);

        IntStream.range(0, faker().number().numberBetween(3, 10))
                .forEach(i -> {
                            User technican = withLikelihoodOf(0.2, () -> randomOfList(users)).orElse(null);
                            withLikelihoodOf(0.3, () -> completedInspection(turbine, technican))
                                    .orElseGet(() -> scheduledInspection(turbine, technican));
                        }
                );
    }

    private Inspection completedInspection(Turbine turbine, User technican) {
        Inspection inspection = entityTestData.saveWithDefaults(
                new InspectionData(turbine, technican), it -> it.setMaintenanceTaskDate(randomPastLocalDate(2 * 365))
        );
        IntStream.range(0, faker().number().numberBetween(0, 5))
                .forEach(i -> entityTestData.saveWithDefaults(new InspectionFindingData(inspection)));

        return inspection;
    }

    private Faker faker() {
        return new Faker();
    }

    private Inspection scheduledInspection(Turbine turbine, User technican) {
        return entityTestData.saveWithDefaults(new ScheduledInspectionData(turbine, technican), it -> it.setMaintenanceTaskDate(randomFutureLocalDateTime(365).toLocalDate())
        );
    }
}
