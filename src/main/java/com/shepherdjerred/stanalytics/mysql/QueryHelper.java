
package com.shepherdjerred.stanalytics.mysql;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.files.ConfigHelper;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class QueryHelper {

    private static QueryHelper instance;

    private QueryHelper() {
        instance = this;
    }

    public static QueryHelper getInstance() {
        if (instance == null) {
            instance = new QueryHelper();
        }
        return instance;
    }

    public void runAsyncUpdate(Connection connection, PreparedStatement input) {

        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            runUpdate(connection, input);

        });

    }

    public void runUpdate(Connection connection, PreparedStatement input) {

        try {

            if (ConfigHelper.debug)
                Main.getInstance().getLogger().info(input.toString());

            input.executeUpdate();

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            HikariManager.getInstance().close(connection, input, null);

        }

    }

    public void massRunUpdates(List<String> updates) {
        try {

            for (String query : updates) {

                Connection connection = HikariManager.getInstance().getConnection();

                PreparedStatement statement;

                statement = connection.prepareStatement(query);

                runUpdate(connection, statement);

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

}
