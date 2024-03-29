package me.khalit.projectleviathan.utils.thread;

import lombok.Getter;
import me.khalit.projectleviathan.api.CustomWork;
import me.khalit.projectleviathan.utils.exceptions.WorkException;

import java.util.ArrayList;
import java.util.List;

public class WorkThread extends Thread {

    private final Object locker = new Object();

    private static List<Work> temporary = new ArrayList<>();
    private List<Work> works = new ArrayList<>();

    @Getter
    private static WorkThread thread;

    public WorkThread() {
        thread = this;
        setName("WorkThread");
    }

    public void run() {
        while (!isInterrupted()) {
            try {
                List<Work> current = new ArrayList<>(works);
                works.clear();

                execute(current);

                synchronized (locker) {
                    locker.wait();
                }
            } catch (InterruptedException | WorkException e) {
                // e.printStackTrace();
            }
        }
    }

    private void execute(List<Work> actions) throws WorkException {
        for (Work work : actions) {
            try {
                work.execute();
            } catch (Exception e) {
                e.printStackTrace();
                throw new WorkException("An error occured with executing specified worker");
            }
        }
    }

    public static void work(CustomWork customWork) {
        work(new Work(customWork));
    }

    public static void work(WorkType workType, Object... params) {
        work(new Work(workType, params));
    }

    public static void work(Work work) {
        final WorkThread thread = getThread();

        temporary.stream()
                .filter(tempWork -> !thread.works.contains(tempWork))
                .forEach(tempWork -> thread.works.add(tempWork));
        if (!thread.works.contains(work)) {
            thread.works.add(work);
        }
        temporary.clear();

        synchronized (thread.locker) {
            thread.locker.notify();
        }
    }
}
