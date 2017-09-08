package com.shepherdjerred.stanalytics;

import com.shepherdjerred.stanalytics.commands.MainCommand;
import com.shepherdjerred.stanalytics.files.ConfigHelper;
import com.shepherdjerred.stanalytics.listeners.JoinListener;
import com.shepherdjerred.stanalytics.metrics.MetricsLite;
import com.shepherdjerred.stanalytics.mysql.HikariManager;
import com.shepherdjerred.stanalytics.mysql.QueryHelper;
import com.shepherdjerred.stanalytics.mysql.TableManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class Main extends JavaPlugin {

    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        ConfigHelper.loadConfigs();

        HikariManager.getInstance().setupPool();

        TableManager.checkTables();

        getCommand("stAnalytics").setExecutor(new MainCommand());

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        // Start metrics
        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {

        }

        startInterval();

    }

    @Override
    public void onDisable() {

        HikariManager.getInstance().closeSource();

    }

    void startInterval() {

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(this, () -> {

            try {

                Connection connection = HikariManager.getInstance().getConnection();

                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + HikariManager.getInstance().prefix + "periodic VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");

                statement.setString(1, Main.getInstance().getConfig().getString("stats.server"));
                statement.setInt(2, Calendar.getInstance().get(Calendar.SECOND));
                statement.setInt(3, Calendar.getInstance().get(Calendar.MINUTE));
                statement.setInt(4, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                statement.setInt(5, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                statement.setInt(6, Calendar.getInstance().get(Calendar.MONTH) + 1);
                statement.setInt(7, Calendar.getInstance().get(Calendar.YEAR));
                statement.setInt(8, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                statement.setInt(9, Bukkit.getServer().getOnlinePlayers().size());
                // TODO Get real TPS data
                statement.setDouble(10, 20.0);
                statement.setDouble(11, Runtime.getRuntime().freeMemory() / 1024L / 1024L);
                statement.setDouble(12, Runtime.getRuntime().maxMemory() / 1024L / 1024L);

                QueryHelper.getInstance().runAsyncUpdate(connection, statement);

                if (ConfigHelper.debug)
                    Main.getInstance().getLogger().info("Creating periodic record in database");

            } catch (SQLException e) {

                e.printStackTrace();

            }

        }, Main.getInstance().getConfig().getInt("stats.periodic.interval") * 20, Main.getInstance().getConfig().getInt("stats.periodic.interval") * 20);

    }

}
