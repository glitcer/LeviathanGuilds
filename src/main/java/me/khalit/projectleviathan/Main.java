package me.khalit.projectleviathan;

import lombok.Data;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Data
public class Main extends JavaPlugin {

    private static Main instance;
    private static Logger logger;

    public void onLoad() {
        instance = this;
        logger = getLogger();
    }

    public void onEnable() {
        logger.info("Loading resources...");
    }

}
