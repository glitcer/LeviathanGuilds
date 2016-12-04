package me.khalit.projectleviathan.configuration;

import lombok.Getter;
import me.khalit.projectleviathan.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locale {

    private static final String absolutePath =
            Main.getInstance().getDataFolder() + "/locales/";
    private static final String directory =
            "locales/";

    @Getter
    private static final List<String> availableLocales = new ArrayList<>();
    @Getter
    private static final List<File> localeFiles = new ArrayList<>();
    @Getter
    private static final Map<String, LocaleFile> configurableFiles = new HashMap<>();

    public static boolean exists(String locale) {
        return availableLocales.contains(locale.toUpperCase());
    }

    public static boolean existsFile(String fileName) {
        return localeFiles.contains(new File(absolutePath + fileName));
    }

    public static String localeByConfigurableFile(LocaleFile concurrentConfigurableFile) {
        return localeByName(concurrentConfigurableFile.getFile().getName());
    }

    public static String localeByName(String name) {
        String locale = name;
        locale = locale.substring(locale.indexOf("_") + 1);
        locale = locale.substring(0, locale.indexOf("."));
        return locale;
    }

    public static LocaleFile fileByLocale(String locale) {
        return configurableFiles.get(locale);
    }


    public static void initializeLocaleFiles() {
        File folder = new File(absolutePath);
        File[] files = folder.listFiles();

        if (files == null || files.length <= 0) {
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // creating default en locale
            ConfigurableFile configurableFile = new ConfigurableFile("locales/locale_en.yml", absolutePath, directory);
            configurableFile.saveDefault();
            configurableFile.reload();

            files = folder.listFiles();
        }
        for (File file : files) {
            if (file.getName().endsWith(".yml")) {
                String locale = localeByName(file.getName());

                LocaleFile localeFile =
                        new LocaleFile(file.getName(), absolutePath);
                localeFile.save();
                localeFile.reload();
                localeFile.load();

                if (!exists(locale))
                    availableLocales.add(locale.toUpperCase());

                if (!existsFile(file.getName())) {
                    localeFiles.add(file);

                    configurableFiles.put(locale, localeFile);
                }

                Main.getInstance().getLogger().info("    Loaded " + locale + " language! (" + file.getName() + ")");
                Main.getInstance().getLogger().info("       Author: " + localeFile
                        .getFileConfiguration().get("author"));
            }
        }
    }

}
