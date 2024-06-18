package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.Severity;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

public class InspectionFindingData implements TestDataProvider<InspectionFinding> {

    private final Inspection inspection;

    public InspectionFindingData(Inspection inspection) {
        this.inspection = inspection;
    }

    @Override
    public Class<InspectionFinding> getEntityClass() {
        return InspectionFinding.class;
    }

    @Override
    public void accept(InspectionFinding finding) {
        Faker faker = new Faker();

        finding.setInspection(inspection);
        finding.setTitle(faker.lorem().sentence());
        finding.setDescription(faker.lorem().paragraph());
        finding.setSeverity(faker.options().option(Severity.values()));
    }
}
