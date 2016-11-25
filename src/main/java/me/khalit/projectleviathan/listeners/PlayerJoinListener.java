package me.khalit.projectleviathan.listeners;

import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.reflection.PrefixManager;
import me.khalit.projectleviathan.utils.reflection.ProtocolManager;
import me.khalit.projectleviathan.utils.reflection.packet.NettyManager;
import me.khalit.projectleviathan.utils.thread.WorkThread;
import me.khalit.projectleviathan.utils.thread.WorkType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        User user = UserManager.getFreshUser(player);

        Hologram hologram = ProtocolManager.getHologram();
        hologram.set("&kiefwiefhwiehfiwefhw", "&6Linia 1", "&cLinia 2", "&kiefwiefhwiehfiwefhw");
        hologram.show(player.getLocation(), 150);

        // delegate custom work test
        //WorkThread.work(() -> player.sendMessage("Custom Multi-threaded Work -> Test"));
        WorkThread.work(WorkType.TAB_LIST_SEND, player);
        PrefixManager.register(player);

        try {
            NettyManager.register(player);
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        if (!user.hasPlayedBefore()) {
            user.setLocale(Locale.fileByLocale("en"));
            user.setGuild(null);
            user.setRank(new Rank(user));

            //WorkThread.work(WorkType.USER_INSERT, user);
            //UserManager.getUsers().add(user);
        }
    }
}
