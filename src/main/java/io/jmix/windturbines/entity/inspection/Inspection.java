package io.jmix.windturbines.entity.inspection;

import io.jmix.core.DeletePolicy;
import io.jmix.core.FileRef;
import io.jmix.core.MetadataTools;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.Composition;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.datatype.DatatypeFormatter;
import io.jmix.windturbines.entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@JmixEntity
@Table(name = "INSPECTION")
@Entity
@PrimaryKeyJoinColumn(name = "ID")
public class Inspection extends StandardEntity {

    @NotNull
    @Column(name = "INSPECTION_DATE", nullable = false)
    private LocalDate inspectionDate;

    @JoinColumn(name = "TECHNICIAN_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User technician;

    @JoinColumn(name = "TURBINE_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Turbine turbine;

    @NotNull
    @Column(name = "TASK_STATUS", nullable = false)
    private String taskStatus;


    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "inspection")
    private List<InspectionFinding> findings;

    @OnDelete(DeletePolicy.CASCADE)
    @Composition
    @OneToMany(mappedBy = "inspection")
    private List<InspectionRecommendation> recommendations;

    @Column(name = "ROTOR_BLADES_ANSWER")
    private String rotorBladesAnswer;

    @Column(name = "ROTOR_BLADES_COMMENT")
    private String rotorBladesComment;

    @Column(name = "GEARBOX_OIL_LEVEL_ANSWER")
    private String gearboxOilLevelAnswer;

    @Column(name = "GEARBOX_OIL_LEVEL_COMMENT")
    private String gearboxOilLevelComment;

    @Column(name = "GENERATOR_CHECK_ANSWER")
    private Integer generatorCheckAnswer;

    @Column(name = "GENERATOR_CHECK_COMMENT")
    private String generatorCheckComment;

    @Column(name = "CONTROL_SYSTEM_STATUS")
    private String controlSystemStatus;

    @Column(name = "CONTROL_SYSTEM_COMMENT")
    private String controlSystemComment;

    @Column(name = "OPERATOR_REP_NAME")
    private String operatorRepName;

    @Column(name = "OPERATOR_REP_SIGNATURE", length = 1024)
    private FileRef operatorRepSignature;

    @Column(name = "OPERATOR_REP_SIGNED_AT")
    private OffsetDateTime operatorRepSignedAt;

    @Column(name = "OPERATOR_CONFIRMATION", nullable = false)
    @NotNull
    private Boolean operatorConfirmation = false;

    public Boolean getOperatorConfirmation() {
        return operatorConfirmation;
    }

    public void setOperatorConfirmation(Boolean operatorConfirmation) {
        this.operatorConfirmation = operatorConfirmation;
    }

    public OffsetDateTime getOperatorRepSignedAt() {
        return operatorRepSignedAt;
    }

    public void setOperatorRepSignedAt(OffsetDateTime operatorRepSignedAt) {
        this.operatorRepSignedAt = operatorRepSignedAt;
    }

    public FileRef getOperatorRepSignature() {
        return operatorRepSignature;
    }

    public void setOperatorRepSignature(FileRef operatorRepSignature) {
        this.operatorRepSignature = operatorRepSignature;
    }

    public String getOperatorRepName() {
        return operatorRepName;
    }

    public void setOperatorRepName(String operatorRepName) {
        this.operatorRepName = operatorRepName;
    }

    public List<InspectionRecommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<InspectionRecommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public String getControlSystemComment() {
        return controlSystemComment;
    }

    public void setControlSystemComment(String controlSystemComment) {
        this.controlSystemComment = controlSystemComment;
    }

    public YesNoAnswer getControlSystemStatus() {
        return controlSystemStatus == null ? null : YesNoAnswer.fromId(controlSystemStatus);
    }

    public void setControlSystemStatus(YesNoAnswer controlSystemStatus) {
        this.controlSystemStatus = controlSystemStatus == null ? null : controlSystemStatus.getId();
    }

    public String getGeneratorCheckComment() {
        return generatorCheckComment;
    }

    public void setGeneratorCheckComment(String generatorCheckComment) {
        this.generatorCheckComment = generatorCheckComment;
    }

    public Integer getGeneratorCheckAnswer() {
        return generatorCheckAnswer;
    }

    public void setGeneratorCheckAnswer(Integer generatorCheckAnswer) {
        this.generatorCheckAnswer = generatorCheckAnswer;
    }

    public String getGearboxOilLevelComment() {
        return gearboxOilLevelComment;
    }

    public void setGearboxOilLevelComment(String gearboxOilLevelComment) {
        this.gearboxOilLevelComment = gearboxOilLevelComment;
    }

    public GearboxOilLevelAnswer getGearboxOilLevelAnswer() {
        return gearboxOilLevelAnswer == null ? null : GearboxOilLevelAnswer.fromId(gearboxOilLevelAnswer);
    }

    public void setGearboxOilLevelAnswer(GearboxOilLevelAnswer gearboxOilLevelAnswer) {
        this.gearboxOilLevelAnswer = gearboxOilLevelAnswer == null ? null : gearboxOilLevelAnswer.getId();
    }

    public RotorBladesAnswer getRotorBladesAnswer() {
        return rotorBladesAnswer == null ? null : RotorBladesAnswer.fromId(rotorBladesAnswer);
    }

    public void setRotorBladesAnswer(RotorBladesAnswer rotorBladesAnswer) {
        this.rotorBladesAnswer = rotorBladesAnswer == null ? null : rotorBladesAnswer.getId();
    }

    public String getRotorBladesComment() {
        return rotorBladesComment;
    }

    public void setRotorBladesComment(String rotorBladesComment) {
        this.rotorBladesComment = rotorBladesComment;
    }

    public List<InspectionFinding> getFindings() {
        return findings;
    }

    public void setFindings(List<InspectionFinding> findings) {
        this.findings = findings;
    }


    public TaskStatus getTaskStatus() {
        return taskStatus == null ? null : TaskStatus.fromId(taskStatus);
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.getId();
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public Turbine getTurbine() {
        return turbine;
    }

    public void setTurbine(Turbine turbine) {
        this.turbine = turbine;
    }

    public User getTechnician() {
        return technician;
    }

    public void setTechnician(User technician) {
        this.technician = technician;
    }

    @InstanceName
    @DependsOnProperties({"inspectionDate", "turbine"})
    public String getInstanceName(MetadataTools metadataTools, DatatypeFormatter datatypeFormatter) {
        return String.format("%s - %s",
                datatypeFormatter.formatLocalDate(inspectionDate),
                metadataTools.format(turbine));
    }

    public String technicianName() {
        return Optional.ofNullable(getTechnician())
                .map(User::getDisplayName)
                .orElse("Not assigned");
    }
}
