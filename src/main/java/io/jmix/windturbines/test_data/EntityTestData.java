package io.jmix.windturbines.test_data;

import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.core.validation.EntityValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Component
public class EntityTestData {

    private final DataManager dataManager;
    private final Validator validator;

    public EntityTestData(DataManager dataManager, Validator validator) {
        this.dataManager = dataManager;
        this.validator = validator;
    }

    /**
     * creates a new entity instance with the provided entity provisioning instance
     *
     * @param testDataProvider the entity provisioning to use
     * @param <Entity>           the type of the entity
     * @return the created entity
     */
    public <Entity> Entity createWithDefaults(TestDataProvider<Entity> testDataProvider) {
        return createWithDefaults(testDataProvider.getEntityClass(), testDataProvider);
    }

    /**
     * creates a new entity instance with the provided entity provisioning.
     * The consumer allows to further adjust the instance on a case-by-case basis.
     *
     * @param testDataProvider         the entity provisioning to use
     * @param entityConsumer the entity provisioning consumer
     * @param <Entity>                   the type of the entity
     * @return the created entity
     */
    public <Entity> Entity createWithDefaults(TestDataProvider<Entity> testDataProvider, Consumer<Entity> entityConsumer) {
        Entity entity = createWithDefaults(testDataProvider.getEntityClass(), testDataProvider);
        entityConsumer.accept(entity);
        return entity;
    }

    /**
     * creates a new entity instance with the provided entity provisioning
     *
     * @param entityType                 the type of the entity
     * @param entityConsumer the entity provisioning consumer
     * @param <Entity>                   the type of the entity
     * @return the created entity
     */
    public <Entity> Entity createWithDefaults(Class<Entity> entityType, Consumer<Entity> entityConsumer) {
        Entity entity = create(entityType);
        entityConsumer.accept(entity);
        return entity;
    }

    /**
     * creates a new entity instance
     *
     * @param entityType the type of the entity
     * @param <Entity>   the entity type
     * @return the created entity
     */
    public <Entity> Entity create(Class<Entity> entityType) {
        return dataManager.create(entityType);
    }


    /**
     * saves a given entity
     *
     * @param entity   the entity to save
     * @param <Entity> the entity type
     * @return the saved entity
     */
    public <Entity> Entity save(Entity entity) {
        ensureIsValid(entity);
        return dataManager.save(entity);
    }

    /**
     * creates and saves an entity instance with the provided entity provisioning
     *
     * @param testDataProvider the entity provisioning to use
     * @param <Entity>           the type of the entity
     * @return the saved entity
     */
    public <Entity> Entity saveWithDefaults(TestDataProvider<Entity> testDataProvider) {
        return save(createWithDefaults(testDataProvider));
    }

    /**
     * creates and saves an entity instance with the provided entity provisioning.
     * The consumer allows to further adjust the instance on a case-by-case basis
     *
     * @param testDataProvider         the entity provisioning to use
     * @param entityConsumer the entity provisioning consumer
     * @param <Entity>                   the type of the entity
     * @return the saved entity
     */
    public <Entity> Entity saveWithDefaults(TestDataProvider<Entity> testDataProvider, Consumer<Entity> entityConsumer) {
        return save(createWithDefaults(testDataProvider, entityConsumer));
    }

    /**
     * creates and saves an entity instance with the provided entity provisioning
     *
     * @param entityType                 the type of the entity
     * @param entityConsumer the entity provisioning consumer
     * @param <Entity>                   the type of the entity
     * @return the saved entity
     */
    public <Entity> Entity saveWithDefaults(Class<Entity> entityType, Consumer<Entity> entityConsumer) {
        Entity entity = create(entityType);
        entityConsumer.accept(entity);
        return save(entity);
    }

    /**
     * loads all entities of a given entity type
     *
     * @param entityType the type of the entity
     * @param <Entity>   the type of the entity
     * @return list of entities from the DB
     */
    public <Entity> List<Entity> loadAll(Class<Entity> entityType) {
        return dataManager.load(entityType).all().list();
    }


    /**
     * loads an entity by its ID
     *
     * @param entityId the ID of the entity
     * @return the entity from the DB
     * @param <Entity> the type of the entity
     */
    public <Entity> Entity reload(Id<Entity> entityId) {
        return dataManager.load(entityId).one();
    }

    private <T> void ensureIsValid(T entity) {
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (!violations.isEmpty()) {
            throw new EntityValidationException("Entity validation failed: ", violations);
        }
    }

    private <T> Set<ConstraintViolation<T>> validate(T entity) {
        return validator.validate(entity, Default.class);
    }
}
