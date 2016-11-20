package me.khalit.projectleviathan.configuration;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigurableFile {

    @Getter
    private static final List<ConfigurableFile> dataFiles = new ArrayList<>();
    @Getter
    private final File file;
    private String directory;
    private FileConfiguration fileConfiguration;

    public ConfigurableFile(String fileName, String directory, String dirName) {
        this.directory = dirName;
        this.file = new File(directory + fileName);
    }

    public ConfigurableFile(String fileName) {
        this.file = new File(Main.getInstance().getDataFolder()
                + "/" + fileName);
    }

    public FileConfiguration getFileConfiguration() {
        if (fileConfiguration == null) {
            fileConfiguration =
                    YamlConfiguration.loadConfiguration(file);
        }
        return fileConfiguration;
    }

    public void saveDefault() {
        if (!file.exists()) {
            Main.getInstance().saveResource(directory + file.getName(), false);
        }
    }

    public void save() {
        if (fileConfiguration == null || file == null) {
            return;
        }
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
        Reader stream = null;
        try {
            stream = new InputStreamReader(
                    Main.getInstance().getResource(directory + file.getName()), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            YamlConfiguration conf =
                    YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(conf);
        }
    }

}
