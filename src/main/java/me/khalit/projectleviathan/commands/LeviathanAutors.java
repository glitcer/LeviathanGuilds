package me.khalit.projectleviathan.commands;

import me.khalit.projectleviathan.api.commands.Command;
import me.khalit.projectleviathan.api.commands.annotations.Aliases;
import me.khalit.projectleviathan.api.commands.annotations.Name;
import org.bukkit.entity.Player;

@Name(value = "leviathan")
@Aliases(value = {"leviathanguilds", "leviathanproject"})
public class LeviathanAutors extends Command {

    @Override
    public void handle(Player player, String[] args) {
        player.sendMessage("sample command");
    }

}
