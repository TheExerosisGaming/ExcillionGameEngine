package me.exerosis.game.engine.core;

import me.exerosis.component.ComponentSystem;
import me.exerosis.component.systemstate.ComponentSystemHolder;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.game.engine.util.FileGetter;
import me.exerosis.io.Remote.RemoteFolder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class Game implements ComponentSystem {
    private final Plugin plugin;
    private final Arena arena;
    private RemoteFolder _gameFolder;
    private String _name;
    private YamlConfiguration _gameConfig;
    //https://www.dropbox.com/s/7hnr2hgos4xa910/Runner.zip?dl=0

    public Game(Plugin plugin, Arena arena, RemoteFolder gameFolder, String name) {
        this.plugin = plugin;
        this.arena = arena;
        _gameFolder = gameFolder;
        _name = name;
        _gameConfig = getConfig("gameConfig.yml");
        setSystemState(GameState.LOBBY);
    }

    public File getFile(String fileName) {
        File file = new File(getFolder().getPath() + "/" + fileName);
        if (file.exists())
            return file;
        System.err.println("Failed to locate file '" + fileName + "', re-downloading online files.");
        updateFolder();
        if (file.exists())
            return file;
        System.err.println("Failed to locate file '" + fileName + "', re-downloading online files.");
        Bukkit.shutdown();
        return null;
    }

    @Override
    public void setSystemState(Enum systemState) {
        if (systemState instanceof GameState)
            if (!systemState.equals(getSystemState()))
                getEventManager().callEvent(new GameStateChangeEvent((GameState) getSystemState(), (GameState) systemState), event -> ComponentSystemHolder.setSystemState(this, event.getNewGameState()));
    }

    public <T> T getGameConfigValue(String index, Class<T> clazz) {
        return clazz.cast(getGameConfigValue(index));
    }

    public Object getGameConfigValue(String index) {
        return getGameConfig().get(index);
    }

    public YamlConfiguration getGameConfig() {
        return _gameConfig;
    }

    public YamlConfiguration getConfig(String fileName) {
        return FileGetter.getConfig(getFile(fileName));
    }

    public RemoteFolder getRemoteFolder() {
        return _gameFolder;
    }

    public File getFolder() {
        return _gameFolder.getFile();
    }

    public void updateFolder() {
        _gameFolder.sync();
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Arena getArena() {
        return arena;
    }

    @Override
    public String toString() {
        return _name;
    }
}