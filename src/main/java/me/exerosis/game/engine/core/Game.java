package me.exerosis.game.engine.core;

import me.exerosis.component.ComponentSystem;
import me.exerosis.game.engine.implementation.trialtwo.components.world.GameFolderManager;
import org.bukkit.plugin.Plugin;

public class Game implements ComponentSystem {
    private final Plugin plugin;
    private final Arena arena;
    private String _name;

    public Game(Plugin plugin, Arena arena, String name) {
        this.plugin = plugin;
        this.arena = arena;
        _name = name;
    }

    public GameFolderManager.GameStats getStats() {
        return arena.getManager().getGameStats(_name);
    }

    public GameFolderManager.GameStats.GameFileManager getFileManager() {
        return getStats().getFileManager();
    }

    public String getName() {
        return _name;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Arena getArena() {
        return arena;
    }
}