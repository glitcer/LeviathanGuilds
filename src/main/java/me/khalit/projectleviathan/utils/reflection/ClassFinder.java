package me.khalit.projectleviathan.utils.reflection;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class ClassFinder {

    @Getter
    private static Reflections reflections;

    public static void findClasses(@NonNull JavaPlugin javaPlugin) throws Exception {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setClassLoaders(new ClassLoader[]{ javaPlugin.getClass().getClassLoader() });
        config.addUrls(javaPlugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toURL());
        reflections = new Reflections(config);
    }

}
