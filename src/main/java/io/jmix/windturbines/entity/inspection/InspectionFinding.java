package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.windturbines.entity.StandardEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@JmixEntity
@Table(name = "INSPECTION_FINDING", indexes = {
        @Index(name = "IDX_INSPECTION_FINDING_INSPECTION", columnList = "INSPECTION_ID")
})
@Entity
public class InspectionFinding extends StandardEntity {
    @JoinColumn(name = "INSPECTION_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Inspection inspection;

    @Column(name = "TITLE", nullable = false)
    @NotNull
    private String title;

    @InstanceName
    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @Column(name = "SEVERITY", nullable = false)
    @NotNull
    private Integer severity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Severity getSeverity() {
        return severity == null ? null : Severity.fromId(severity);
    }

    public void setSeverity(Severity severity) {
        this.severity = severity == null ? null : severity.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }
}
