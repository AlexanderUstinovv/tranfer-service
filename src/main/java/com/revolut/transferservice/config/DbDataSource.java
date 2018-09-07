package com.revolut.transferservice.config;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbDataSource {
    Connection getConnection() throws SQLException;
    void closePooledConnection() throws SQLException;
}
