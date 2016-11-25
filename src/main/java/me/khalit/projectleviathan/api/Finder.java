package me.khalit.projectleviathan.api;

import lombok.NonNull;
import me.khalit.projectleviathan.api.commands.Command;
import me.khalit.projectleviathan.api.commands.annotations.Aliases;
import me.khalit.projectleviathan.api.commands.annotations.Name;
import me.khalit.projectleviathan.api.commands.annotations.Permission;
import me.khalit.projectleviathan.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;

public class Finder {

    private final Reflections reflections;

    private static CommandMap commandMap;

    static {
        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Finder(@NonNull JavaPlugin javaPlugin) throws Exception {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setClassLoaders(new ClassLoader[]{ javaPlugin.getClass().getClassLoader() });
        config.addUrls(javaPlugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toURL());
        this.reflections = new Reflections(config);
    }

    public void findCommands() throws Exception {
        if (commandMap == null) {
            return;
        }
        Set<Class<? extends Command>> classes = this.reflections.getSubTypesOf(Command.class);
        if (classes.size() == 0) {
            return;
        }
        for (Class<? extends Command> aClass : classes) {
            Object instance = aClass.newInstance();
            Command command = (Command) instance;
            for (Annotation annotation : command.getClass().getAnnotations()) {
                if (annotation instanceof Name) {
                    Name name = (Name) annotation;
                    command.setName(name.value());
                    continue;
                }
                if (annotation instanceof Permission) {
                    Permission permission = (Permission) annotation;
                    command.setPermission(permission.permission());
                    command.setPermissionMessage(Util.fixColors(permission.message()));
                    continue;
                }
                if (annotation instanceof Aliases) {
                    Aliases aliases = (Aliases) annotation;
                    command.setAliases(Arrays.asList(aliases.value()));
                }
            }
            if (command.getName() == null) {
                return;
            }
            if (command.getPermission() != null && command.getPermissionMessage() == null) {
                return;
            }
            commandMap.register("leviathanguilds", command);
        }
    }

}
