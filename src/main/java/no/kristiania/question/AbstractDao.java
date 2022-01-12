package no.kristiania.question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public abstract class AbstractDao<T> {
    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected AbstractDao() {
    }

    protected abstract void save(T question) throws SQLException;

    protected abstract T retrieve(long id) throws SQLException;

    public abstract List<T> listAll() throws SQLException;

    protected abstract T readFromResultSet(ResultSet rs) throws SQLException;

}