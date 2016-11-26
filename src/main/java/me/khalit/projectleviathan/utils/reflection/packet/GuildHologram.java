package me.khalit.projectleviathan.utils.reflection.packet;

import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.utils.reflection.ProtocolManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GuildHologram {

    private final Map<Guild, Hologram> hologramMap = new HashMap<>();

    public void spawn(Player... players) {
        GuildManager.getGuilds().values().forEach(this::spawn);
    }

    public void spawn(Guild guild) {
        getHologram(guild).show(
                guild.getRegion().getCenter().add(0, 1.5, 0));
    }

    public void destroy(Guild guild) {
        Hologram hologram = getHologram(guild);
        hologramMap.remove(guild);
        hologram.destroy();
    }

    private Hologram getHologram(Guild guild) {
        Object packet;
        if (!hologramMap.containsKey(guild)) {
            Hologram hologram = ProtocolManager.getHologram();
            hologram.set("&6" + guild.getTag(), "&4Health: &c1000 &lHP"); // test

            hologramMap.put(guild, hologram);
            return hologram;
        }
        return hologramMap.get(guild);
    }

}
