package dao;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

public class BaseDAOImpl<T> implements BaseDAO<T> {
    private EntityManager entityManager = TransactionManager.getInstance().getEntityManager();

    public T merge(T entity) {
        return entityManager.merge(entity);
    }

    public List<T> getAll(Class<T> type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(type);

        Root<T> table = query.from(type);

        query.select(table);

        return entityManager.createQuery(query).getResultList();
    }

    public T getById(Class<T> type, long id) {
        T entity = entityManager.find(type, id);
        return entity;
    }
}
