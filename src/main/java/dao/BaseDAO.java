package dao;

import java.util.List;

/**
 * Created by Лена on 08.11.2015.
 */
public interface BaseDAO<T> {
    T add(T entity);

    List<T> getAll(Class<T> type);

    T getById(Class<T> type, Long id);
}
