
package com.shepherdjerred.stanalytics.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HikariManager {

    private static HikariManager instance;
    public String host, port, database, username, password, prefix;
    public int min, max;
    public long connectionTimeout, idleTimeout;
    private HikariDataSource dataSource = new HikariDataSource();

    public HikariManager() {
        instance = this;
    }

    public static HikariManager getInstance() {
        if (instance == null)
            instance = new HikariManager();
        return instance;
    }

    public void setupPool() {

        dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);

        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(min);
        dataSource.setMaximumPoolSize(max);
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setIdleTimeout(idleTimeout);

        TableManager.checkConnection();

    }

    public Connection getConnection() throws SQLException {

        return dataSource.getConnection();

    }

    public void closeSource() {

        dataSource.close();

    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {

        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if (ps != null)
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        if (res != null)
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
}
