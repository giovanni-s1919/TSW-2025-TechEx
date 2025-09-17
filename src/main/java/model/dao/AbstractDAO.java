package model.dao;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDAO<T, ID> implements GenericDAO<T, ID> {

    protected final DataSource dataSource;

    public AbstractDAO(DataSource ds) {
        if (ds == null) throw new IllegalArgumentException("DataSource cannot be null.");
        this.dataSource = ds;
    }


    protected abstract void validate(T entity);
    protected abstract T extract(ResultSet rs) throws SQLException;
}
