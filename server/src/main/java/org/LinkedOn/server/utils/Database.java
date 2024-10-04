package org.LinkedOn.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Database {
    private static final String DB_URL = Configuration.getValue("db", "url");
    private static final String DB_USERNAME = Configuration.getValue("db", "username");
    private static final String DB_PASSWORD = Configuration.getValue("db", "password");
    private static final String DB_DRIVER = Configuration.getValue("db", "driver");
    private static Connection connection = null;

    public static synchronized Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DB_DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                registerShutdownHook();
            }
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    private static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(Database::closeConnection));
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close the connection of database", e);
        }
    }

    public static <T> List<T> mapResultSetToList(ResultSet rs, Function<ResultSet, T> mapper) throws SQLException {
        List<T> resultList = new ArrayList<>();
        while (rs.next()) {
            resultList.add(mapper.apply(rs));
        }
        return resultList;
    }
}
