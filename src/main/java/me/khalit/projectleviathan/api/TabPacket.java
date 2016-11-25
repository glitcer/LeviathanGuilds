package me.khalit.projectleviathan.api;

import com.mojang.authlib.GameProfile;
import org.bukkit.entity.Player;

public interface TabPacket {

    void sendPacket(Player player, GameProfile gp, String slot, String mode);
    void sendPacketHeaderFooter(Player player, String header, String footer);
    Object getPlayerInfo(GameProfile gp, String slot);
    Object build(String content);

}
