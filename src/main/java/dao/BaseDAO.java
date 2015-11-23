package dao;

import java.util.List;

public interface BaseDAO<T> {
    T merge(T entity);

    List<T> getAll(Class<T> type);

    T getById(Class<T> type, long id);
}
