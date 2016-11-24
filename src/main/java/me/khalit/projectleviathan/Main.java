package me.khalit.projectleviathan;

import lombok.Getter;
import me.khalit.projectleviathan.configuration.*;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.data.managers.RegionManager;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.data.sql.SQLHandler;
import me.khalit.projectleviathan.listeners.PlayerJoinListener;
import me.khalit.projectleviathan.utils.element.TabExecutor;
import me.khalit.projectleviathan.utils.exceptions.MetricsException;
import me.khalit.projectleviathan.utils.runnables.AsyncTabHeavyRefreshTask;
import me.khalit.projectleviathan.utils.runnables.AsyncTabLightRefreshTask;
import me.khalit.projectleviathan.utils.runnables.TPSMonitor;
import me.khalit.projectleviathan.utils.thread.WorkThread;
import me.khalit.projectleviathan.utils.thread.WorkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
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
        logger.info("Starting metrics...");
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (Exception e) {
            try {
                throw new MetricsException();
            } catch (MetricsException ignoreStackTrace) {

            }
        }
        logger.info("Loading resources...");
        logger.info("Loading configurations...");
        logger.info("   Loading locales....");
        Locale.initializeLocaleFiles();
        logger.info("   Loading settings...");
        saveDefaultConfig();
        TabReader.loadConfiguration();
        new Settings();
        new Messages();
        logger.info("Scheduling runnables...");
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new AsyncTabLightRefreshTask(), 20L, 20L);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new AsyncTabHeavyRefreshTask(), 20L, 3600L);
        Bukkit.getScheduler().runTaskTimer(this, new TPSMonitor(), 1000L, 50L);
        logger.info("Executing tab...");
        TabExecutor.load();
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
        logger.info("Loading players...");
        UserManager.loadUsers();
        logger.info("Loading guilds...");
        GuildManager.loadGuilds();
        logger.info("Loading regions...");
        RegionManager.loadRegions();
    }

}
