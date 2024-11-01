package io.jmix.windturbines.view.inspectionfinding;

import io.jmix.core.DataManager;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.upload.FileStorageUploadField;
import io.jmix.flowui.component.upload.receiver.FileTemporaryStorageBuffer;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.testassist.FlowuiTestAssistConfiguration;
import io.jmix.flowui.testassist.UiTest;
import io.jmix.flowui.testassist.UiTestUtils;
import io.jmix.flowui.upload.TemporaryStorage;
import io.jmix.flowui.view.View;
import io.jmix.windturbines.JmixWindturbinesApplication;
import io.jmix.windturbines.entity.Manufacturer;
import io.jmix.windturbines.entity.Operator;
import io.jmix.windturbines.entity.Turbine;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import io.jmix.windturbines.test_data.EntityTestData;
import io.jmix.windturbines.test_data.entity.*;
import io.jmix.windturbines.test_support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UiTest
@SpringBootTest(
        properties = {"spring.main.allow-bean-definition-overriding=true"},
        classes = {
                JmixWindturbinesApplication.class,
                FlowuiTestAssistConfiguration.class
        })
class InspectionFindingDetailViewTest {

    @Autowired
    DataManager dataManager;
    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    EntityTestData entityTestData;
    @Autowired
    ViewNavigators viewNavigators;
    @Autowired
    FileStorage fileStorage;
    @Autowired
    TemporaryStorage temporaryStorage;
    @Value("classpath:images/evidence-1.jpg")
    private Resource evidence1Image;
    @Value("classpath:images/evidence-2.jpg")
    private Resource evidence2Image;
    private InspectionFinding inspectionFinding;
    private InspectionFindingEvidence evidence1;
    private InspectionFindingEvidence evidence2;

    private static List<InspectionFindingEvidence> evidences(InspectionFindingDetailView detailView) {
        return detailView.getEvidencesDc().getDisconnectedItems();
    }

    @BeforeEach
    void setUp() {
        databaseCleanup.removeAllEntities();
        Manufacturer manufacturer = entityTestData.saveWithDefaults(new ManufacturerData("Vestas"));
        Operator operator = entityTestData.saveWithDefaults(new OperatorData());
        Turbine turbine = entityTestData.saveWithDefaults(new TurbineData(manufacturer, operator));
        Inspection inspection = entityTestData.saveWithDefaults(new ScheduledInspectionData(turbine, null));

        inspectionFinding = entityTestData.saveWithDefaults(new InspectionFindingData(inspection), it -> it.setTitle("Test Finding"));

        evidence1 = saveEvidence(inspectionFinding, evidence1Image);
        evidence2 = saveEvidence(inspectionFinding, evidence2Image);
    }

    @Test
    void when_openDetail_then_evidenceImagesAreRendered() {
        // given
        InspectionFindingDetailView detailView = navigateTo(InspectionFindingDetailView.class, inspectionFinding, InspectionFinding.class);

        // when
        List<InspectionFindingEvidence> evidences = evidences(detailView);

        // then
        assertThat(evidences).hasSize(2);

        // and
        assertThat(evidences.getFirst().getFile().getFileName()).isEqualTo(evidence1.getFile().getFileName());
        assertThat(evidences.getLast().getFile().getFileName()).isEqualTo(evidence2.getFile().getFileName());
    }

    @Test
    void when_performUpload_then_evidenceImagesAreReRendered() {
        // given
        InspectionFindingDetailView detailView = navigateTo(InspectionFindingDetailView.class, inspectionFinding, InspectionFinding.class);

        // when
        FileStorageUploadField upload = UiTestUtils.getComponent(detailView, "upload");

        simulateUploadFor(detailView, upload, "evidence-3.jpg", "image/jpeg");
        List<InspectionFindingEvidence> evidences = evidences(detailView);

        // then
        assertThat(evidences).hasSize(3);

        // and
        assertThat(evidences.getLast().getFile().getFileName()).isEqualTo("evidence-3.jpg");
    }

    private void simulateUploadFor(InspectionFindingDetailView detailView, FileStorageUploadField upload, String fileName, String mimeType) {
        FileTemporaryStorageBuffer fileTemporaryStorageBuffer = new FileTemporaryStorageBuffer(temporaryStorage);
        fileTemporaryStorageBuffer.receiveUpload(fileName, mimeType);
        detailView.onUploadFileUploadSucceeded(new FileUploadSucceededEvent<>(upload, fileName, mimeType, 0, fileTemporaryStorageBuffer));
    }

    private FileRef storeTestImage(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return fileStorage.saveStream(resource.getFilename(), inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store test image", e);
        }
    }

    private <V extends View<?>, E> V navigateTo(Class<V> viewClass, E entity, Class<E> entityClass) {
        viewNavigators.detailView(UiTestUtils.getCurrentView(), entityClass)
                .withViewClass(viewClass)
                .editEntity(entity)
                .navigate();
        return UiTestUtils.getCurrentView();
    }

    private InspectionFindingEvidence saveEvidence(InspectionFinding inspectionFinding, Resource image) {
        var evidence = dataManager.create(InspectionFindingEvidence.class);
        evidence.setFile(storeTestImage(image));
        evidence.setInspectionFinding(inspectionFinding);

        return dataManager.save(evidence);
    }
}
