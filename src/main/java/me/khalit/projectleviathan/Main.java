package me.khalit.projectleviathan;

import lombok.Data;
import lombok.Getter;
import me.khalit.projectleviathan.configuration.ConfigurableFile;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.configuration.Messages;
import me.khalit.projectleviathan.configuration.Settings;
import me.khalit.projectleviathan.data.sql.SQLHandler;
import me.khalit.projectleviathan.listeners.PlayerJoinListener;
import me.khalit.projectleviathan.utils.thread.WorkThread;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static SQLHandler sqlHandler;

    private Logger logger;

    public void onLoad() {
        instance = this;
        logger = getLogger();
        logger.info("Initializing work thread...");
        new WorkThread().start();
    }

    public void onEnable() {
        logger.info("Loading resources...");
        logger.info("Loading configurations...");
        logger.info("   Loading locales....");
        Locale.initializeLocaleFiles();
        logger.info("   Loading settings...");
        saveDefaultConfig();
        new Settings();
        new Messages();
        logger.info("Connecting to MySQL...");
        try {
            sqlHandler = new SQLHandler(
                    Settings.getString("mysql.host"),
                    Settings.getInt("mysql.port"),
                    Settings.getString("mysql.user"),
                    Settings.getString("mysql.pass"),
                    Settings.getString("mysql.base"));
            sqlHandler.getHikariConnector().connect();
            sqlHandler.createTables();
        } catch (Exception e) {
            logger.severe("[MySQL] Server is not responding or credentials are wrong." +
                    " Check your MySQL credentials in config.yml file!");
            return;
        }
        logger.info("Registering listeners...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), this);
    }

}
