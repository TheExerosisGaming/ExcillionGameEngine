package me.exerosis.game.engine.componentgame;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.event.game.GameDisableEvent;
import me.exerosis.game.engine.componentgame.event.game.GameEnableEvent;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

public abstract class Game {
    private InstancePool _instancePool = new InstancePool();
    private boolean started = false;

    public Game() {
    }

    public abstract boolean doesFollowDependencyInjection();

    public void start() {
    }

    public void end() {
    }

    // Game Management
    public void enableGame() {
        Bukkit.getPluginManager().callEvent(new GameEnableEvent());
        if (!doesFollowDependencyInjection())
            _instancePool.setAllFields();
        start();
        started = true;
        int count = 0;
        for (Object object : _instancePool)
            if (object instanceof Component) {
                count++;
                ((Component) object).onEnable();
            }
        Log.warn("Enabled '" + count + "' components!");
    }

    public void disableGame() {
        Bukkit.getPluginManager().callEvent(new GameDisableEvent());
        end();
        started = false;
        int count = 0;
        for (Object object : _instancePool)
            if (object instanceof Component) {
                ((Component) object).onDisable();
                count++;
            }
        Log.warn("Disabled '" + count + "' components!");
    }

    public void addInstance(Object object) {
        if (object instanceof ComponentBundle)
            for (Component component : ((ComponentBundle) object).getComponents())
                _instancePool.add(component);
        else
            _instancePool.add(object);
    }

    // Getters and setters.
    public InstancePool getInstancePool() {
        return _instancePool;
    }

    public boolean isStarted() {
        return started;
    }
}