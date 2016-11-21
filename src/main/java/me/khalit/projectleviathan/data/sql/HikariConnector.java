package me.khalit.projectleviathan.data.sql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;

@Data
public class HikariConnector {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    private HikariDataSource dataSource;

    public HikariConnector(String host, int port, String user, String password, String database) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void connect() {
        dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        dataSource.addDataSourceProperty("cachePrepStmts", true);
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", user);
        dataSource.addDataSourceProperty("password", password);
    }

}
