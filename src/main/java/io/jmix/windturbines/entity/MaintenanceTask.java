package io.jmix.windturbines.entity;

import io.jmix.core.MetadataTools;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@JmixEntity
@Table(name = "MAINTENANCE_TASK", indexes = {
        @Index(name = "IDX_MAINTENANCE_TASK_TECHNICAN", columnList = "TECHNICAN_ID"),
        @Index(name = "IDX_MAINTENANCE_TASK_TURBINE", columnList = "TURBINE_ID")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public class MaintenanceTask extends StandardEntity {
    @NotNull
    @Column(name = "MAINTENANCE_TASK_DATE", nullable = false)
    private LocalDate maintenanceTaskDate;

    @Column(name = "TYPE_", nullable = false)
    @NotNull
    private String type;

    @JoinColumn(name = "TECHNICAN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User technican;

    @JoinColumn(name = "TURBINE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Turbine turbine;

    @NotNull
    @Column(name = "TASK_STATUS", nullable = false)
    private String taskStatus;

    public TaskType getType() {
        return type == null ? null : TaskType.fromId(type);
    }

    public void setType(TaskType type) {
        this.type = type == null ? null : type.getId();
    }

    public TaskStatus getTaskStatus() {
        return taskStatus == null ? null : TaskStatus.fromId(taskStatus);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.getId();
    }

    public void setMaintenanceTaskDate(LocalDate maintenanceTaskDate) {
        this.maintenanceTaskDate = maintenanceTaskDate;
    }

    public LocalDate getMaintenanceTaskDate() {
        return maintenanceTaskDate;
    }

    public Turbine getTurbine() {
        return turbine;
    }

    public void setTurbine(Turbine turbine) {
        this.turbine = turbine;
    }

    public User getTechnican() {
        return technican;
    }

    public void setTechnican(User technican) {
        this.technican = technican;
    }

    @InstanceName
    @DependsOnProperties({"maintenanceTaskDate", "turbine"})
    public String getInstanceName(MetadataTools metadataTools, DatatypeFormatter datatypeFormatter) {
        return String.format("%s - %s",
                datatypeFormatter.formatLocalDate(maintenanceTaskDate),
                metadataTools.format(turbine));
    }
}
