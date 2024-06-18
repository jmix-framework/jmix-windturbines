package io.jmix.windturbines.test_data;

import java.util.function.Consumer;

/**
 * interface to implement for provision an entity with default data
 * @param <Entity> the type of the entity
 */
public interface TestDataProvider<Entity> extends Consumer<Entity> {

    Class<Entity> getEntityClass();

}
