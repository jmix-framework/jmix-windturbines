package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.GearboxOilLevelAnswer;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.RotorBladesAnswer;
import io.jmix.windturbines.entity.inspection.YesNoAnswer;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import java.time.LocalDate;

import static io.jmix.windturbines.test_data.RandomValues.*;

public class InspectionData implements TestDataProvider<Inspection> {

    private final Turbine turbine;
    private final User technician;

    public InspectionData(Turbine turbine, User technician) {
        this.turbine = turbine;
        this.technician = technician;
    }

    @Override
    public Class<Inspection> getEntityClass() {
        return Inspection.class;
    }

    @Override
    public void accept(Inspection inspection) {
        Faker faker = new Faker();
        inspection.setTurbine(turbine);
        inspection.setTechnician(technician);
        inspection.setInspectionDate(LocalDate.now());
        inspection.setTaskStatus(faker.options().option(TaskStatus.values()));

        inspection.setRotorBladesAnswer(faker.options().option(RotorBladesAnswer.values()));
        inspection.setRotorBladesComment(faker.lorem().sentence());

        inspection.setGearboxOilLevelAnswer(faker.options().option(GearboxOilLevelAnswer.values()));
        inspection.setGearboxOilLevelComment(faker.lorem().sentence());

        inspection.setGeneratorCheckAnswer(randomPositiveNumber(1, 10));
        inspection.setGeneratorCheckComment(faker.lorem().sentence());

        inspection.setControlSystemStatus(faker.options().option(YesNoAnswer.values()));
        inspection.setControlSystemComment(faker.lorem().sentence());
    }
}
