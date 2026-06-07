package net.napthex.utils;

import net.napthex.NapTheX;
import com.tcoded.folialib.FoliaLib;
import java.util.concurrent.CompletableFuture;

public class Task {
    private static FoliaLib foliaLib;

    private static void initFoliaLib() {
        if (foliaLib == null) {
            foliaLib = new FoliaLib(NapTheX.getInstance());
        }
    }

    public static void sync(Runnable runnable) {
        initFoliaLib();
        foliaLib.getScheduler().runNextTick(task -> runnable.run());
    }

    public static void sync(Runnable runnable, int delay) {
        initFoliaLib();
        foliaLib.getScheduler().runLater(runnable, delay);
    }

    public static void sync(Runnable runnable, int delay, int period) {
        initFoliaLib();
        foliaLib.getScheduler().runTimer(runnable, delay, period);
    }

    public static void async(Runnable runnable) {
        initFoliaLib();
        foliaLib.getScheduler().runAsync(task -> runnable.run());
    }

    public static void async(Runnable runnable, int delay) {
        initFoliaLib();
        foliaLib.getScheduler().runLaterAsync(runnable, delay);
    }

    public static void async(Runnable runnable, int delay, int period) {
        initFoliaLib();
        foliaLib.getScheduler().runTimerAsync(runnable, delay, period);
    }
}
