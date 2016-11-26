package me.khalit.projectleviathan.api;

import org.bukkit.entity.Player;

public interface Title {

    Title setTitle(String title);
    Title setSubtitle(String subtitle);
    Title setFadeIn(int ticks);
    Title setStay(int ticks);
    Title setFadeOut(int ticks);
    void show(Player player);

}
