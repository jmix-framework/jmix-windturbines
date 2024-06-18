package io.jmix.windturbines.test_data.entity;

import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import io.jmix.windturbines.entity.inspection.Priority;
import io.jmix.windturbines.test_data.TestDataProvider;
import net.datafaker.Faker;

public class InspectionRecommendationData implements TestDataProvider<InspectionRecommendation> {

    private final Inspection inspection;
    private final InspectionFinding relatedFinding;

    public InspectionRecommendationData(Inspection inspection, InspectionFinding relatedFinding) {
        this.inspection = inspection;
        this.relatedFinding = relatedFinding;
    }

    @Override
    public Class<InspectionRecommendation> getEntityClass() {
        return InspectionRecommendation.class;
    }

    @Override
    public void accept(InspectionRecommendation recommendation) {
        Faker faker = new Faker();

        recommendation.setInspection(inspection);
        recommendation.setRelatedFinding(relatedFinding);
        recommendation.setDescription(faker.lorem().paragraph());
        recommendation.setPriority(faker.options().option(Priority.values()));
    }
}
