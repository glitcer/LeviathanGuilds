package me.khalit.projectleviathan.data.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

public class HikariConnector {

    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USER;
    private final String PASSWORD;

    @Getter
    private HikariDataSource dataSource;

    public HikariConnector(String host, int port, String user, String password, String database) {
        this.DATABASE = database;
        this.HOST = host;
        this.PORT = port;
        this.USER = user;
        this.PASSWORD = password;
    }

    public void connect() {
        dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("serverName", HOST);
        dataSource.addDataSourceProperty("port", PORT);
        dataSource.addDataSourceProperty("databaseName", DATABASE);
        dataSource.addDataSourceProperty("user", USER);
        dataSource.addDataSourceProperty("password", PASSWORD);
    }

}
