package me.khalit.projectleviathan.utils.runnables;

import me.khalit.projectleviathan.utils.element.TabManager;
import org.bukkit.Bukkit;

public class AsyncTabLightRefreshTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(TabManager::updateLight);
    }

}
