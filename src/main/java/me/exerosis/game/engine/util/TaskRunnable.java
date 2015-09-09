package me.exerosis.game.engine.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class TaskRunnable implements Runnable {
    private int _id;

    public TaskRunnable() {
    }

    public abstract Plugin getPlugin();

    public abstract void run();

    public void startTask(long delay, long time) {
        if (!isRunning())
            _id = Bukkit.getScheduler().runTaskTimer(getPlugin(), this, delay, time).getTaskId();
    }

    public void stopTask() {
        if (isRunning())
            Bukkit.getScheduler().cancelTask(_id);
    }

    public boolean isRunning() {
        return Bukkit.getScheduler().isQueued(_id);
    }
}
