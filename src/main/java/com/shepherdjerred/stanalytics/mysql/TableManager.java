
package com.shepherdjerred.stanalytics.mysql;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.files.FileManager;
import com.shepherdjerred.stanalytics.mysql.QueryManager.consumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TableManager {

    public static void checkTables() {

        consumer<Boolean> checkTables = result -> {

            if (result)

                Main.getInstance().getLogger().info("All tables exist, loading data");

            else {

                Main.getInstance().getLogger().info("Some tables don't exist, creating them now");

                List<String> tables = new ArrayList<>();

                tables.add("CREATE TABLE IF NOT EXISTS " + HikariManager.getInstance().prefix + "periodic  (server VARCHAR(36), second TINYINT, minute TINYINT, hour TINYINT, day TINYINT, month TINYINT, year SMALLINT, weekday TINYINT, count BIGINT, tps DOUBLE, freeMem DOUBLE, maxMem DOUBLE, UNIQUE(second, minute, hour, day, month, year));");
                tables.add("CREATE TABLE IF NOT EXISTS " + HikariManager.getInstance().prefix + "players (server VARCHAR(36), day TINYINT, month TINYINT, year SMALLINT, weekday TINYINT, username VARCHAR(16), uuid CHAR(36), first BOOL, UNIQUE(day, month, year, uuid));");

                try {

                    for (String query : tables) {

                        Connection connection = HikariManager.getInstance().getConnection();

                        PreparedStatement statement = null;

                        statement = connection.prepareStatement(query);

                        QueryHelper.getInstance().runUpdate(connection, statement);

                    }

                } catch (SQLException e) {

                    e.printStackTrace();

                }

            }

            updateTables();

        };

        QueryManager.getInstance().checkTables(checkTables);

    }

    // TODO Rewrite this to be more flexible
    private static void updateTables() {

        int tableVersion = FileManager.getInstance().storage.getInt("mysqlTableVersion");
        List<String> updates = new ArrayList<>();

        switch (tableVersion) {

        }

        if (!updates.isEmpty()) {

            QueryHelper.getInstance().massRunUpdates(updates);

            FileManager.getInstance().storage.set("mysqlTableVersion", 3);

        }

    }

    static void checkConnection() {

        try {
            if (HikariManager.getInstance().getConnection().isValid(30))
                Main.getInstance().getLogger().info("Connection to the MySQL database was successful!");

        } catch (SQLException e) {
            Main.getInstance().getLogger().severe("Connection to MySQL database failed!");
            Main.getInstance().getLogger().severe("Disabling stAnalytics due to database failure");
            Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());

            Main.getInstance().getLogger().severe("Connection to the MySQL database failed!");
            e.printStackTrace();
        }

    }
}
