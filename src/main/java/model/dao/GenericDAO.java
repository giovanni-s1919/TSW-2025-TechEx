package model.dao;

import java.sql.SQLException;
import java.util.List;

public interface GenericDAO<T, ID> {
    void save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    boolean delete(ID id) throws SQLException;
    T findById(ID id) throws SQLException;
    List<T> findAll(String order) throws SQLException;
}
