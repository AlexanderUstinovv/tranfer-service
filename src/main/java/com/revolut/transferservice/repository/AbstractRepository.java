package com.revolut.transferservice.repository;

import com.revolut.transferservice.config.DerbyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class AbstractRepository {

    List<HashMap<String, Object>> executeQuery(String sqlQuery) throws SQLException {
        try {
            initializeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
        return resultSetToArrayList(resultSet);
        }
        finally {
            connection.commit();
            closeConnection();
        }
    }

    void executeUpdate(String sqlQuery) throws SQLException {
        try {
            initializeConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
        }
        finally {
            connection.commit();
            closeConnection();
        }
    }

    private List<HashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        ArrayList<HashMap<String, Object>> list = new ArrayList<>(50);
        while (rs.next()){
            HashMap<String, Object> row = new HashMap<>(columns);
            for(int i=1; i<=columns; ++i){
                row.put(md.getColumnName(i),rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    private void initializeConnection() throws SQLException {
        connection = DerbyDataSource.INSTANCE.getConnection();
        connection.setAutoCommit(false);
    }

    private void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private Connection connection;
}
