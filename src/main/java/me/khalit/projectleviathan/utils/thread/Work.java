package me.khalit.projectleviathan.utils.thread;

import lombok.Data;
import me.khalit.projectleviathan.Main;
import me.khalit.projectleviathan.api.CustomWork;
import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.utils.reflection.PrefixManager;
import me.khalit.projectleviathan.utils.reflection.packet.NettyManager;
import me.khalit.projectleviathan.utils.reflection.TabManager;
import me.khalit.projectleviathan.utils.exceptions.NullSpecifiedException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Data
public class Work {

    private final WorkType type;
    private Object[] params;
    private CustomWork customWork;

    public Work(WorkType type, Object... params) {
        this.type = type;
        this.params = params;
    }

    public Work(CustomWork customWork) {
        this.type = WorkType.CUSTOM_WORK;
        this.customWork = customWork;
    }

    public void execute() throws NullSpecifiedException {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            switch (type) {
                case USER_INSERT: {
                    ((User)params[0]).insert();
                    return;
                }
                case USER_UPDATE: {
                    ((User)params[0]).save();
                    return;
                }
                case NETTY_REGISTER: {
                    try {
                        NettyManager.register((Player) params[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                case CUSTOM_WORK: {
                    customWork.work();
                    return;
                }
                case PREFIX_REGISTER: {
                    PrefixManager.register((Player) params[0]);
                    return;
                }
                case PREFIX_JOIN: {
                    PrefixManager.join((Guild) params[1], (Player) params[0]);
                    return;
                }
                case PREFIX_LEAVE: {
                    PrefixManager.leave((Guild) params[1], (Player) params[0]);
                    return;
                }
                case PREFIX_ADD_GUILD: {
                    PrefixManager.createGuild((Guild) params[1], (Player) params[0]);
                    return;
                }
                case PREFIX_REMOVE_GUILD: {
                    PrefixManager.removeGuild((Guild) params[0]);
                    return;
                }
                case PREFIX_CREATE_ALLIANCE: {
                    PrefixManager.createAlliance((Guild) params[1], (Guild) params[0]);
                    return;
                }
                case PREFIX_REMOVE_ALLIANCE: {
                    PrefixManager.removeAlliance((Guild) params[1], (Guild) params[0]);
                    return;
                }
                case TAB_LIST_SEND: {
                    TabManager.show((Player)params[0]);
                }
            }
        });
    }

}
