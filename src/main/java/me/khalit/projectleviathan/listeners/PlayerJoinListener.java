package me.khalit.projectleviathan.listeners;

import me.khalit.projectleviathan.configuration.Locale;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.data.managers.UserManager;
import me.khalit.projectleviathan.utils.thread.WorkMethod;
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

        if (!user.hasPlayedBefore()) {
            user.setLocale(Locale.fileByLocale("en"));

            // not sure if it works yet xD
            WorkThread.work(WorkType.USER_INSERT, WorkMethod.ASYNC, user);
        }

        if (!UserManager.getUsers().contains(user)) {
            UserManager.loadUser(player);
        }
    }
}
