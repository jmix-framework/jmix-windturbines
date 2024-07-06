package io.jmix.windturbines.entity.inspection;

import io.jmix.core.FileRef;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.windturbines.entity.StandardEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@JmixEntity
@Table(name = "INSPECTION_FINDING_EVIDENCE", indexes = {
        @Index(name = "IDX_INSPECTION_FINDING_EVIDENCE_INSPECTION_FINDING", columnList = "INSPECTION_FINDING_ID")
})
@Entity
public class InspectionFindingEvidence extends StandardEntity {
    @JoinColumn(name = "INSPECTION_FINDING_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private InspectionFinding inspectionFinding;

    @NotNull
    @Column(name = "FILE_", nullable = false, length = 1024)
    private FileRef file;



    public FileRef getFile() {
        return file;
    }

    public void setFile(FileRef file) {
        this.file = file;
    }

    public InspectionFinding getInspectionFinding() {
        return inspectionFinding;
    }

    public void setInspectionFinding(InspectionFinding inspectionFinding) {
        this.inspectionFinding = inspectionFinding;
    }
}
