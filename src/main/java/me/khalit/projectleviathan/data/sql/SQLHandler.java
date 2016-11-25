package me.khalit.projectleviathan.data.sql;

import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLHandler {

    public static SQLHandler createMySQL(String host, int port, String user, String password, String database) {
        return new MySQL(host, port, user, password, database);
    }

    public abstract void execute(@NonNull String query) throws Exception;

    public abstract void query(@NonNull String query, @NonNull Callback<ResultSet> result) throws Exception;

    public void createTables() throws Exception {
        execute("CREATE TABLE IF NOT EXISTS users (" +
                "uuid VARCHAR(36) NOT NULL," +
                "name TEXT NOT NULL," +
                "locale VARCHAR(3) NOT NULL," +
                "honor INT NOT NULL," +
                "points INT NOT NULL," +
                "kills INT NOT NULL," +
                "deaths INT NOT NULL)");
        execute("CREATE TABLE IF NOT EXISTS regions (" +
                "guild TINYTEXT NOT NULL," +
                "center TEXT NOT NULL," +
                "size INT NOT NULL," +
                "world TEXT NOT NULL," +
                "parent BOOLEAN NOT NULL)");
        execute("CREATE TABLE IF NOT EXISTS guilds (" +
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
                "occupied BOOLEAN NOT NULL)");
    }
}
