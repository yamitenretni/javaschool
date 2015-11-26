package dao;

import java.util.List;

/**
 * Describes main dao methods.
 *
 * @param <T> class of entity
 */
public interface BaseDAO<T> {
    /**
     * Add or update entity in database.
     *
     * @param entity merging entity
     * @return added or updated entity
     */
    T merge(T entity);

    /**
     * Get all entities with given type from database.
     *
     * @param type entity class
     * @return list of entities
     */
    List<T> getAll(Class<T> type);

    /**
     * Get entity with given type and id from database.
     *
     * @param type entity class
     * @param id   id of entity in database
     * @return found entity
     */
    T getById(Class<T> type, long id);
}
