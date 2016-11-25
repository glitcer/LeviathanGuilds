package me.khalit.projectleviathan.utils.runnables;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class TPSMonitor implements Runnable {

    public static final LinkedList<Double> history = new LinkedList<>();

    private static DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private static String result = "20.0";
    private transient long poll = System.nanoTime();

    public static void initialize() {
        history.add(20.0);
        decimalFormat.setRoundingMode(RoundingMode.HALF_DOWN);
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        long spent = (start - poll) / 1000L;
        if (spent == 0L) {
            spent = 1L;
        }
        if (history.size() > 10) {
            history.remove();
        }
        double tps = 50000000.0D / spent;
        if (tps <= 21.0D) {
            history.add(tps);
        }
        poll = start;
        double average = 0.0D;
        for (Double f : history) {
            if (f != null) {
                average += f;
            }
        }
        result = decimalFormat.format(average / history.size());
    }

    public static String getTPS() {
        return result;
    }

}
