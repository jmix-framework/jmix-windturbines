package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.TaskType;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import java.time.LocalDate;

import static io.jmix.windturbines.test_data.RandomValues.*;

public class ScheduledInspectionData implements TestDataProvider<Inspection> {

    private final Turbine turbine;
    private final User technican;

    public ScheduledInspectionData(Turbine turbine, User technican) {
        this.turbine = turbine;
        this.technican = technican;
    }

    @Override
    public Class<Inspection> getEntityClass() {
        return Inspection.class;
    }

    @Override
    public void accept(Inspection inspection) {
        inspection.setType(TaskType.INSPECTION);
        inspection.setTurbine(turbine);
        inspection.setTechnican(technican);
        inspection.setMaintenanceTaskDate(LocalDate.now());
        inspection.setTaskStatus(new Faker().options().option(TaskStatus.SCHEDULED, TaskStatus.DEFERRED));
    }
}
