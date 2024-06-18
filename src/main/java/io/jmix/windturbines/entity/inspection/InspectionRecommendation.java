package io.jmix.windturbines.entity.inspection;

import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.windturbines.entity.StandardEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@JmixEntity
@Table(name = "INSPECTION_RECOMMENDATION", indexes = {
        @Index(name = "IDX_INSPECTION_RECOMMENDATION_RELATED_FINDING", columnList = "RELATED_FINDING_ID"),
        @Index(name = "IDX_INSPECTION_RECOMMENDATION_INSPECTION", columnList = "INSPECTION_ID")
})
@Entity
public class InspectionRecommendation extends StandardEntity {
    @JoinColumn(name = "INSPECTION_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Inspection inspection;

    @InstanceName
    @Column(name = "DESCRIPTION")
    @Lob
    private String description;

    @Column(name = "PRIORITY", nullable = false)
    @NotNull
    private Integer priority;

    @JoinColumn(name = "RELATED_FINDING_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private InspectionFinding relatedFinding;

    public Inspection getInspection() {
        return inspection;
    }

    public void setInspection(Inspection inspection) {
        this.inspection = inspection;
    }

    public InspectionFinding getRelatedFinding() {
        return relatedFinding;
    }

    public void setRelatedFinding(InspectionFinding relatedFinding) {
        this.relatedFinding = relatedFinding;
    }

    public Priority getPriority() {
        return priority == null ? null : Priority.fromId(priority);
    }

    public void setPriority(Priority priority) {
        this.priority = priority == null ? null : priority.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
