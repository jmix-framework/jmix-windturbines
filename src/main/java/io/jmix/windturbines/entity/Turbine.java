package io.jmix.windturbines.entity;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.windturbines.entity.inspection.Inspection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@JmixEntity
@Table(name = "TURBINE", indexes = {
        @Index(name = "IDX_TURBINE_MANUFACTURER", columnList = "MANUFACTURER_ID"),
        @Index(name = "IDX_TURBINE_OPERATOR", columnList = "OPERATOR_ID")
})
@Entity
public class Turbine extends StandardEntity {

    @InstanceName
    @NotNull
    @Column(name = "TURBINE_ID", length = 10)
    private String turbineId;

    @OnDelete(DeletePolicy.CASCADE)
    @JoinColumn(name = "MANUFACTURER_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Manufacturer manufacturer;

    @Column(name = "MODEL", nullable = false)
    @NotNull
    private String model;

    @Column(name = "LOCATION", nullable = false)
    @NotNull
    private String location;

    @Column(name = "HEIGHT")
    private Integer height;

    @Column(name = "ROTOR_DIAMETER")
    private Integer rotorDiameter;

    @Column(name = "INSTALLATION_DATE", nullable = false)
    @NotNull
    private LocalDate installationDate;

    @Column(name = "LAST_MAINTENANCE_DATE")
    private LocalDate lastMaintenanceDate;

    @Column(name = "STATUS")
    private String status;

    @JoinColumn(name = "OPERATOR_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Operator operator;

    @OrderBy("inspectionDate DESC")
    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "turbine")
    private List<Inspection> inspections;

    public List<Inspection> getInspections() {
        return inspections;
    }

    public void setInspections(List<Inspection> inspections) {
        this.inspections = inspections;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public TurbineStatus getStatus() {
        return status == null ? null : TurbineStatus.fromId(status);
    }

    public void setStatus(TurbineStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public Integer getRotorDiameter() {
        return rotorDiameter;
    }

    public void setRotorDiameter(Integer rotorDiameter) {
        this.rotorDiameter = rotorDiameter;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getTurbineId() {
        return turbineId;
    }

    public void setTurbineId(String turbineId) {
        this.turbineId = turbineId;
    }
}
