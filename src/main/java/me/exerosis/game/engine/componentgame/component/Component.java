package me.exerosis.game.engine.componentgame.component;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.Game;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.util.TaskRunnable;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Component extends TaskRunnable implements Listener {
    @Depend
    private Game _game;
    @Depend
    private Plugin _plugin;
    @Depend
    private Arena _arena;

    public Component() {
    }

    public static void unregisterListener(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    public abstract void onEnable();

    public abstract void onDisable();

    // Bukkit listener management
    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, _plugin);
    }

    public void formatedPrint(String string) {
        formatedPrint(string, false);
    }

    public void formatedPrint(String string, boolean error) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append("]: ");
        stringBuilder.append(string);
        String printLine = stringBuilder.toString();

        if (error)
            Log.error(printLine);
        else
            System.out.println(printLine);
    }

    @Override
    public void run() {
    }

    public Game getGame() {
        return _game;
    }

    public Plugin getPlugin() {
        return _plugin;
    }

    public Arena getArena() {
        return _arena;
    }
}
