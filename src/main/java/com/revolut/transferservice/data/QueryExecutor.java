package com.revolut.transferservice.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class QueryExecutor {

    protected List<HashMap<String, Object>> executeQuery(String sqlQuery) throws SQLException {
        try (Connection connection = DerbyDataSource.INSTANCE.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            List<HashMap<String, Object>> resultList = resultSetToArrayList(preparedStatement.executeQuery());
            connection.commit();
            return resultList;
        }
    }

    protected void executeUpdate(String sqlQuery) throws SQLException {
        try(Connection connection = DerbyDataSource.INSTANCE.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    private List<HashMap<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
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
}
