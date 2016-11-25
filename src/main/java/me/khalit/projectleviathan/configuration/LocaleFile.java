package me.khalit.projectleviathan.configuration;

import lombok.Getter;
import me.khalit.projectleviathan.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class LocaleFile {

    @Getter
    private final File file;
    private String directory;
    private FileConfiguration fileConfiguration;

    public LocaleFile(String fileName, String directory) {
        this.directory = directory;
        this.file = new File(directory + fileName);
    }

    public LocaleFile(String fileName) {
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
                    new FileInputStream(directory + "/" + file.getName()), "UTF8");
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
        if (stream != null) {
            YamlConfiguration conf =
                    YamlConfiguration.loadConfiguration(file);
            fileConfiguration.setDefaults(conf);
        }
    }

}
