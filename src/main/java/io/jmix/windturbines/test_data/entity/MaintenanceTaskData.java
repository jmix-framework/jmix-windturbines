package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.GearboxOilLevelAnswer;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.RotorBladesAnswer;
import io.jmix.windturbines.entity.inspection.YesNoAnswer;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import java.time.LocalDate;

import static io.jmix.windturbines.test_data.RandomValues.randomPositiveNumber;

public class MaintenanceTaskData implements TestDataProvider<MaintenanceTask> {

    private final Turbine turbine;
    private final User technican;

    public MaintenanceTaskData(Turbine turbine, User technican) {
        this.turbine = turbine;
        this.technican = technican;
    }

    @Override
    public Class<MaintenanceTask> getEntityClass() {
        return MaintenanceTask.class;
    }

    @Override
    public void accept(MaintenanceTask maintenanceTask) {
        Faker faker = new Faker();

        maintenanceTask.setType(TaskType.INSPECTION);
        maintenanceTask.setTurbine(turbine);
        maintenanceTask.setTechnican(technican);
        maintenanceTask.setMaintenanceTaskDate(LocalDate.now());
        maintenanceTask.setTaskStatus(faker.options().option(TaskStatus.values()));
    }
}
