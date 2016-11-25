package me.khalit.projectleviathan.api.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Command extends org.bukkit.command.Command {

    public Command() {
        super("");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        this.handle((Player) sender, args);
        return true;
    }

    public abstract void handle(Player player, String[] args);
}
