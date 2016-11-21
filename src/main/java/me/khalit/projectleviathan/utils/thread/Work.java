package me.khalit.projectleviathan.utils.thread;

import lombok.Data;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.utils.element.TabManager;
import me.khalit.projectleviathan.utils.exceptions.NullSpecifiedException;
import org.bukkit.entity.Player;

@Data
public class Work {

    private final WorkType type;
    private Object[] params;

    public Work(WorkType type, Object... params) {
        this.type = type;
        this.params = params;
    }

    public void execute() throws NullSpecifiedException {
        switch (type) {
            case USER_INSERT: {
                ((User)params[0]).insert();
                return;
            }
            case USER_UPDATE: {
                ((User)params[0]).save();
                return;
            }
            case TAB_LIST_SEND: {
                TabManager.show((Player)params[0]);
                return;
            }
            default: {
                throw new NullSpecifiedException("Work type can not be empty");
            }
        }
    }

}
