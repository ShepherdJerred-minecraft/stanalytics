
package com.shepherdjerred.stanalytics.mysql;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.files.ConfigHelper;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Calendar;
import java.util.UUID;


public class QueryManager {

    private static QueryManager instance;

    public QueryManager() {
        instance = this;
    }

    public static QueryManager getInstance() {
        if (instance == null)
            instance = new QueryManager();
        return instance;
    }

    public void checkTables(consumer<Boolean> consumer) {

        Connection connection = null;
        ResultSet result = null;

        try {

            connection = HikariManager.getInstance().getConnection();

            DatabaseMetaData dbm = connection.getMetaData();

            boolean playersExists = false;
            boolean periodicExists = false;
            boolean allExist = false;

            result = dbm.getTables(null, null, HikariManager.getInstance().prefix + "players", null);
            if (result.next())
                playersExists = true;


            result = dbm.getTables(null, null, HikariManager.getInstance().prefix + "periodic", null);
            if (result.next())
                periodicExists = true;

            if (playersExists && periodicExists)
                allExist = true;

            if (consumer != null)
                consumer.accept(allExist);

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            HikariManager.getInstance().close(connection, null, result);

        }

    }

    public void hasJoinedBefore(consumer<Boolean> consumer, UUID uuid) {

        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;

            try {

                connection = HikariManager.getInstance().getConnection();

                statement = connection.prepareStatement("SELECT * from " + HikariManager.getInstance().prefix + "players WHERE uuid = '" + uuid + "' AND server = '" + Main.getInstance().getConfig().getString("stats.server") + "';");

                if (ConfigHelper.debug)
                    Main.getInstance().getLogger().info(statement.toString());

                result = statement.executeQuery();

                if (consumer != null) {

                    if (result.next()) {
                        if (ConfigHelper.debug)
                            Main.getInstance().getLogger().info("Player has been here before");
                        consumer.accept(true);
                    } else {
                        if (ConfigHelper.debug)
                            Main.getInstance().getLogger().info("Player hasn't been here before");
                        consumer.accept(false);
                    }

                }

            } catch (SQLException e) {

                e.printStackTrace();

            } finally {

                HikariManager.getInstance().close(connection, statement, result);

            }

        });

    }

    public void hasJoinedToday(consumer<Boolean> consumer, UUID uuid) {

        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet result = null;

            try {

                connection = HikariManager.getInstance().getConnection();

                statement = connection.prepareStatement("SELECT * from " + HikariManager.getInstance().prefix + "players WHERE uuid = '" + uuid + "' AND server = '" + Main.getInstance().getConfig().getString("stats.server") + "' AND day = " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " AND month = " + (Calendar.getInstance().get(Calendar.MONTH) + 1) + " AND year = " + Calendar.getInstance().get(Calendar.YEAR) + ";");

                if (ConfigHelper.debug)
                    Main.getInstance().getLogger().info(statement.toString());

                result = statement.executeQuery();

                if (consumer != null) {

                    if (result.next()) {
                        if (ConfigHelper.debug)
                            Main.getInstance().getLogger().info("Player has been here today");
                        consumer.accept(true);
                    } else {
                        if (ConfigHelper.debug)
                            Main.getInstance().getLogger().info("Player hasn't been here today");
                        consumer.accept(false);
                    }

                }

            } catch (SQLException e) {

                e.printStackTrace();

            } finally {

                HikariManager.getInstance().close(connection, statement, result);

            }

        });

    }

    public interface consumer<T> {

        void accept(T result);

    }

}
