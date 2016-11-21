package me.khalit.projectleviathan.utils.thread;

import lombok.Data;
import me.khalit.projectleviathan.data.User;
import me.khalit.projectleviathan.utils.exceptions.NullSpecifiedException;

@Data
public class Work {

    private final WorkType type;
    private final WorkMethod method;
    private Object[] params;

    public Work(WorkType type, WorkMethod method, Object... params) {
        this.type = type;
        this.method = method;
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
            default: {
                throw new NullSpecifiedException("Work type can not be empty");
            }
        }
    }

}
