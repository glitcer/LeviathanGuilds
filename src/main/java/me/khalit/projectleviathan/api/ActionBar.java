package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;

public interface ActionBar {

    void send(Player player, String content);
    void send(Player player, String content, int ticks);
    void send(String content);
    void send(String content, int ticks);
    Object getPacket(String content);
}
