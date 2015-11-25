package dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Implements main methods for dao.
 * @param <T> entity class
 */
public class BaseDAOImpl<T> implements BaseDAO<T> {
    /**
     * Get entity manager from TransactionManager
     */
    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();

    /**
     * Merge entity in database
     * @param entity merging entity
     */
    public final T merge(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Get list of all entities with asked type.
     * @param type entity class
     * @return list of objects
     */
    public final List<T> getAll(final Class<T> type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(type);

        Root<T> table = query.from(type);

        query.select(table);

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Get entity by id from database
     * @param type entity class
     * @param id id of object in database
     * @return found object
     */
    public final T getById(final Class<T> type, final long id) {
        T entity = entityManager.find(type, id);
        return entity;
    }
}
