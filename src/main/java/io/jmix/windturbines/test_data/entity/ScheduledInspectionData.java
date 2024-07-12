package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.TaskStatus;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.User;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

import java.time.LocalDate;


public class ScheduledInspectionData implements TestDataProvider<Inspection> {

    private final Turbine turbine;
    private final User technician;

    public ScheduledInspectionData(Turbine turbine, User technician) {
        this.turbine = turbine;
        this.technician = technician;
    }

    @Override
    public Class<Inspection> getEntityClass() {
        return Inspection.class;
    }

    @Override
    public void accept(Inspection inspection) {
        inspection.setTurbine(turbine);
        inspection.setTechnician(technician);
        inspection.setInspectionDate(LocalDate.now());
        inspection.setTaskStatus(new Faker().options().option(TaskStatus.SCHEDULED, TaskStatus.DEFERRED));
    }
}
