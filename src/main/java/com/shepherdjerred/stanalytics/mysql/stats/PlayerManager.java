package com.shepherdjerred.stanalytics.mysql.stats;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.mysql.HikariManager;
import com.shepherdjerred.stanalytics.mysql.QueryHelper;
import com.shepherdjerred.stanalytics.mysql.QueryManager;
import com.shepherdjerred.stanalytics.mysql.QueryManager.consumer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class PlayerManager {

    public static void hasJoinedToday(Player player) {

        consumer<Boolean> hasJoinedToday = result -> {

            if (!result) {
                updatePlayer(player, PlayerOperations.CREATE);
            }

        };

        QueryManager.getInstance().hasJoinedToday(hasJoinedToday, player.getUniqueId());

    }

    public static void hasJoinedBefore(Player player) {

        consumer<Boolean> hasJoinedBefore = result -> {

            if (result) {
                hasJoinedToday(player);
            } else {
                updatePlayer(player, PlayerOperations.CREATE_FIRST);
            }

        };

        QueryManager.getInstance().hasJoinedBefore(hasJoinedBefore, player.getUniqueId());

    }

    public static void updatePlayer(Player player, PlayerOperations operation) {

        Bukkit.getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {

            try {

                Connection connection = HikariManager.getInstance().getConnection();

                PreparedStatement statement = null;

                String query;

                switch (operation) {

                    case CREATE:
                        query = "INSERT INTO " + HikariManager.getInstance().prefix + "players VALUES (?,?,?,?,?,?,?,?);";

                        statement = connection.prepareStatement(query);

                        statement.setString(1, Main.getInstance().getConfig().getString("stats.server"));
                        statement.setInt(2, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        statement.setInt(3, Calendar.getInstance().get(Calendar.MONTH) + 1);
                        statement.setInt(4, Calendar.getInstance().get(Calendar.YEAR));
                        statement.setInt(5, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                        statement.setString(6, player.getName());
                        statement.setString(7, String.valueOf(player.getUniqueId()));
                        statement.setBoolean(8, false);

                        break;

                    case CREATE_FIRST:
                        query = "INSERT INTO " + HikariManager.getInstance().prefix + "players VALUES (?,?,?,?,?,?,?,?);";

                        statement = connection.prepareStatement(query);

                        statement.setString(1, Main.getInstance().getConfig().getString("stats.server"));
                        statement.setInt(2, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        statement.setInt(3, Calendar.getInstance().get(Calendar.MONTH) + 1);
                        statement.setInt(4, Calendar.getInstance().get(Calendar.YEAR));
                        statement.setInt(5, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                        statement.setString(6, player.getName());
                        statement.setString(7, String.valueOf(player.getUniqueId()));
                        statement.setBoolean(8, true);

                        break;

                }

                QueryHelper.getInstance().runAsyncUpdate(connection, statement);

            } catch (SQLException e) {

                e.printStackTrace();

            }

        });

    }

    public enum PlayerOperations {

        CREATE, CREATE_FIRST

    }

}
