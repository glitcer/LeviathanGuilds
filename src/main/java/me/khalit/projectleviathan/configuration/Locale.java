package me.khalit.projectleviathan.configuration;

import lombok.Getter;
import me.khalit.projectleviathan.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Locale {

    private static final String directory =
            Main.getInstance().getDataFolder() + "/locales/";
    private static final String dirName =
            "locales/";

    @Getter
    private static final List<String> availableLocales = new ArrayList<>();
    @Getter
    private static final List<File> localeFiles = new ArrayList<>();
    @Getter
    private static final List<ConcurrentConfigurableFile> configurableFiles = new ArrayList<>();

    public static boolean exists(String locale) {
        return availableLocales.contains(locale.toUpperCase());
    }

    public static boolean existsFile(String fileName) {
        return localeFiles.contains(new File(directory + fileName));
    }

    public static String localeByConfigurableFile(ConcurrentConfigurableFile concurrentConfigurableFile) {
        return localeByName(concurrentConfigurableFile.getFile().getName());
    }

    public static String localeByName(String name) {
        String locale = name;
        locale = locale.substring(locale.indexOf("_") + 1);
        locale = locale.substring(0, locale.indexOf("."));
        return locale;
    }

    public static ConcurrentConfigurableFile fileByLocale(String locale) {
        for (ConcurrentConfigurableFile concurrentConfigurableFile : configurableFiles) {
            if (localeByConfigurableFile(concurrentConfigurableFile).equalsIgnoreCase(locale)) {
                return concurrentConfigurableFile;
            }
        }
        return null;
    }


    public static void initializeLocaleFiles() {
        File[] files = new File(directory).listFiles();

        if (files == null || files.length <= 0) {
            if (!new File(directory).exists()) {
                new File(directory).mkdir();
            }

            // creating default en locale
            ConfigurableFile configurableFile = new ConfigurableFile("locales/locale_en.yml", directory, dirName);
            configurableFile.saveDefault();
            configurableFile.reload();

            files = new File(directory).listFiles();
        }
        assert files != null;
        for (File file : files) {
            if (file.getName().endsWith(".yml")) {
                String locale = localeByName(file.getName());

                ConcurrentConfigurableFile concurrentConfigurableFile =
                        new ConcurrentConfigurableFile(file.getName(), directory);
                concurrentConfigurableFile.save();
                concurrentConfigurableFile.reload();

                if (!exists(locale))
                    availableLocales.add(locale.toUpperCase());

                if (!existsFile(file.getName())) {
                    localeFiles.add(file);

                    configurableFiles.add(concurrentConfigurableFile);
                }

                Main.getInstance().getLogger().info("    Loaded " + locale + " language! (" + file.getName() + ")");
                Main.getInstance().getLogger().info("       Author: " + concurrentConfigurableFile
                        .getFileConfiguration().get("author"));
            }
        }
    }

}
