package me.khalit.projectleviathan.listeners;

import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.Rank;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.thread.WorkThread;
import me.khalit.projectleviathan.utils.thread.WorkType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        User user = UserManager.getFreshUser(player);

        WorkThread.work(WorkType.NETTY_REGISTER, player);
        WorkThread.work(WorkType.TAB_LIST_SEND, player);

        if (!user.hasPlayedBefore()) {
            user.setLocale(Locale.fileByLocale("en"));
            user.setGuild(null);
            user.setRank(new Rank(user));

            WorkThread.work(WorkType.USER_INSERT, user);
            UserManager.getUsers().add(user);
        }
    }
}
