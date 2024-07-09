package io.jmix.windturbines.test_support;

import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.MetadataTools;
import io.jmix.core.SaveContext;
import io.jmix.core.security.SystemAuthenticator;
import io.jmix.windturbines.entity.*;
import io.jmix.windturbines.entity.inspection.Inspection;
import io.jmix.windturbines.entity.inspection.InspectionFinding;
import io.jmix.windturbines.entity.inspection.InspectionFindingEvidence;
import io.jmix.windturbines.entity.inspection.InspectionRecommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class DatabaseCleanup {

    @Autowired
    DataManager dataManager;

    @Autowired
    Metadata metadata;
    @Autowired
    MetadataTools metadataTools;
    @Autowired
    DataSource dataSource;
    @Autowired
    SystemAuthenticator systemAuthenticator;

    public <T> void removeAllEntities(Class<T> entityClass) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        performDeletion(entityClass, jdbcTemplate);
    }

    private <T> void performDeletion(Class<T> entityClass, JdbcTemplate jdbcTemplate) {
        performDeletion(tableName(entityClass), jdbcTemplate);
    }

    private <T> void performDeletion(String tableName, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("DELETE FROM " + tableName);
    }

    private <T> void performDeletionWhere(Class<T> entityClass, String whereCondition, JdbcTemplate jdbcTemplate) {
        performDeletionWhere(tableName(entityClass), whereCondition, jdbcTemplate);
    }

    private void performDeletionWhere(String tableName, String whereCondition, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("DELETE FROM %s WHERE %s".formatted(tableName, whereCondition));
    }

    private <T> String tableName(Class<T> entityClass) {
        return metadataTools.getDatabaseTable(metadata.getClass(entityClass));
    }

    public void removeAllEntities() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        performDeletion(InspectionRecommendation.class, jdbcTemplate);
        performDeletion(InspectionFindingEvidence.class, jdbcTemplate);
        performDeletion(InspectionFinding.class, jdbcTemplate);
        performDeletion(Inspection.class, jdbcTemplate);
        performDeletion(Inspection.class, jdbcTemplate);
        performDeletion(Turbine.class, jdbcTemplate);
        performDeletion(Manufacturer.class, jdbcTemplate);
        performDeletion(Operator.class, jdbcTemplate);
        performDeletionWhere(User.class, "username != 'admin'", jdbcTemplate);
    }


    public void removeAllEntities(List<?> entities) {
        SaveContext entitiesToRemove = new SaveContext();
        entities.forEach(entitiesToRemove::removing);
        dataManager.save(entitiesToRemove);
    }
}
