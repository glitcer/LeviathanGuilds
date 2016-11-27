package me.khalit.projectleviathan.utils;

import me.khalit.projectleviathan.data.Guild;
import org.bukkit.entity.Player;

public class FreeCam {

    public static boolean isHacker(Player player, Guild victim) {
        return victim.getRegion().getCenter()
                .distance(player.getLocation()) >= 6;
    }

}
