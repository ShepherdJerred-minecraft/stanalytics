
package com.shepherdjerred.stanalytics.files;

import com.shepherdjerred.stanalytics.Main;
import com.shepherdjerred.stanalytics.mysql.HikariManager;
import org.bukkit.configuration.file.YamlConfiguration;


public class ConfigHelper {

    public static boolean debug;

    @SuppressWarnings("deprecation")
    public static void loadConfigs() {

        Main.getInstance().saveDefaultConfig();
        Main.getInstance().getConfig().setDefaults(YamlConfiguration.loadConfiguration(Main.getInstance().getResource("config.yml")));
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveConfig();

        debug = Main.getInstance().getConfig().getBoolean("debug");

        FileManager.getInstance().loadFiles();

        ConfigHelper.setMysqlVariables();

    }

    public static void setMysqlVariables() {

        HikariManager.getInstance().host = Main.getInstance().getConfig().getString("mysql.hostname");
        HikariManager.getInstance().port = Main.getInstance().getConfig().getString("mysql.port");
        HikariManager.getInstance().database = Main.getInstance().getConfig().getString("mysql.database");
        HikariManager.getInstance().username = Main.getInstance().getConfig().getString("mysql.username");
        HikariManager.getInstance().password = Main.getInstance().getConfig().getString("mysql.password");
        HikariManager.getInstance().prefix = Main.getInstance().getConfig().getString("mysql.prefix");

        HikariManager.getInstance().connectionTimeout = Main.getInstance().getConfig().getLong("mysql.pool.connectionTimeout");
        HikariManager.getInstance().idleTimeout = Main.getInstance().getConfig().getLong("mysql.pool.idleTimeout");
        HikariManager.getInstance().min = Main.getInstance().getConfig().getInt("mysql.pool.minIdleConnections");
        HikariManager.getInstance().max = Main.getInstance().getConfig().getInt("mysql.pool.maxConnections");

    }

}
