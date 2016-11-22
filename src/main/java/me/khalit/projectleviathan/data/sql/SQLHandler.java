package me.khalit.projectleviathan.data.sql;

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
    }

    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = hikariConnector.getDataSource().getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public void createTables() throws SQLException {
        PreparedStatement preparedStatement = getConnection()
                .prepareStatement(
                        "CREATE TABLE IF NOT EXISTS users (" +
                                "uuid VARCHAR(36) NOT NULL," +
                                "name TEXT NOT NULL," +
                                "locale VARCHAR(3) NOT NULL)");
        preparedStatement.executeUpdate();
        preparedStatement.close();

        PreparedStatement stmtRegions = getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS regions (" +
                        "guild TINYTEXT NOT NULL," +
                        "center TEXT NOT NULL," +
                        "size INT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "parent BOOLEAN NOT NULL)"
        );
        stmtRegions.executeUpdate();
        stmtRegions.close();

        PreparedStatement stmtGuilds = getConnection().prepareStatement(
                "CREATE TABLE IF NOT EXISTS guilds (" +
                        "tag TINYTEXT NOT NULL," +
                        "name TEXT NOT NULL," +
                        "members TEXT NOT NULL," +
                        "kills INT NOT NULL," +
                        "deaths INT NOT NULL," +
                        "validity BIGINT NOT NULL," +
                        "protection BIGINT NOT NULL," +
                        "born BIGINT NOT NULL," +
                        "allies TEXT NOT NULL," +
                        "home TEXT NOT NULL," +
                        "treasury TEXT NOT NULL," +
                        "money INT NOT NULL," +
                        "pvp BOOLEAN NOT NULL," +
                        "occupied BOOLEAN NOT NULL)"
        );
        stmtGuilds.executeUpdate();
        stmtGuilds.close();
    }

}
