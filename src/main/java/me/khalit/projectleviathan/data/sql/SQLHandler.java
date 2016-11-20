package me.khalit.projectleviathan.data.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Data
public class SQLHandler {

    private final HikariConnector hikariConnector;
    private Connection connection;

    public SQLHandler(String host, int port, String user, String password, String database) {
        hikariConnector = new HikariConnector(host, port, user, password, database);
        hikariConnector.connect();
        try {
            connection = hikariConnector.getDataSource().getConnection();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement(
                        "CREATE TABLE IF NOT EXISTS users (" +
                                "uuid VARCHAR(36) NOT NULL," +
                                "name TEXT NOT NULL," +
                                "locale VARCHAR(3) NOT NULL)");
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

}
