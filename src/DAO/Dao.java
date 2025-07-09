package DAO;

import java.util.List;

public interface Dao<T> {
    void create(T t) throws Exception;
    T findById(long id) throws Exception;
    List<T> findAll() throws Exception;
    void update(T t) throws Exception;
    void delete(long id) throws Exception;
}
