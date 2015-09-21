package me.exerosis.game.engine.implementation.trialtwo.components.world;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.reflection.event.EventListener;
import me.exerosis.reflection.event.Priority;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WorldComponent extends GameComponent {
    private File _worldTemplate;
    private World _gameWorld;
    private World _lobbyWorld;

    public WorldComponent(Game game) {
        super(game);
    }

    private void shutdown() {
        getArena().disableSystem();
        Bukkit.shutdown();
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event) {
        if (!event.getWorld().getName().equals(getName()))
            return;
        print("Loaded world!");

        _gameWorld = event.getWorld();
        _gameWorld.setDifficulty(Difficulty.NORMAL);
        _gameWorld.setTime(0);
        _gameWorld.setAutoSave(false);
        _gameWorld.setGameRuleValue("doDaylightCycle", "false");
        _gameWorld.setGameRuleValue("showDeathMessages", "false");
        _gameWorld.setGameRuleValue("showDeathMessages", "false");
        _gameWorld.setGameRuleValue("doMobSpawning", "false");

        setGameState(GameState.LOBBY);
    }

    @EventListener(postEvent = true, priority = Priority.HIGHEST)
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.RESTARTING))
            tryUnload(() -> {
                getArena().nextSystem();
                print("Game ended, starting next game.");
            });
    }

    private void tryLoad() {
        print("Trying to load world!");
        if (_worldTemplate == null || !_worldTemplate.exists())
            _worldTemplate = getArena().getManager().getGameStats(getGame().getName()).getFileManager().getFile("level.dat");

        File worldFolder = new File(getName());
        if (worldFolder.mkdirs())
            print("Failed to locate world folder, creating one!", true);
        try {
            FileUtils.copyDirectory(_worldTemplate, worldFolder, false);
        } catch (IOException e) {
            print("Failed to copy template to the server directory, shutting down server!", true);
            shutdown();
            return;
        }
        WorldCreator worldCreator = new WorldCreator(getName());
        worldCreator.type(WorldType.FLAT);
        Bukkit.createWorld(worldCreator);
    }

    private void tryUnload(Runnable runWhenUnloaded) {
        print("Trying to unload world!");
        _gameWorld = Bukkit.getWorld(getName());

        if (_gameWorld == null) {
            runWhenUnloaded.run();
            return;
        }

        //Remove players
        for (Player player : _gameWorld.getPlayers())
            player.teleport(getLobbyWorld().getSpawnLocation().add(0, 4, 0));

        //Wait 1 second before unloading the world
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Bukkit.unloadWorld(_gameWorld, false)) {
                print("Unable to unload world, please fix the problem!");
                shutdown();
                return;
            }
            //Wait 4 seconds before trying to remove the world folder
            Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
                final File toDelete = new File(getName());

                @Override
                public void run() {
                    print("World unloaded.");
                    try {
                        print("Removing world folder.");
                        FileUtils.deleteDirectory(toDelete);
                    } catch (IOException e) {
                        e.printStackTrace();
                        print("Failed to remove the world directory, shutting down server!", true);
                        shutdown();
                        return;
                    }

                    print("Folder removed.");
                    if (runWhenUnloaded != null)
                        runWhenUnloaded.run();
                }
            }, 40);
        }, 20);
    }


    @Override
    public void onEnable() {
        registerListener();
        _worldTemplate = getArena().getManager().getGameStats(getGame().getName()).getFileManager().getFile("level.dat");
        tryLoad();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        super.onDisable();
    }

    public String getMapDsc() {
        StringBuilder mapDsc = new StringBuilder();
        try {
            File file = new File(getGameWorldFolder().getPath() + "/mapDsc.txt");
            for (String line : Files.readAllLines(Paths.get(file.toURI())))
                mapDsc.append(ChatColor.translateAlternateColorCodes('&', line)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapDsc.toString();
    }

    public YamlConfiguration getConfig(String fileName) {
        File config = new File(_worldTemplate.getPath() + '/' + fileName);
        if (config.exists())
            return YamlConfiguration.loadConfiguration(config);
        return null;
    }

    public <T> T getMapDataValue(String index, Class<T> clazz) {
        return clazz.cast(getMapDataValue(index));
    }

    public Object getMapDataValue(String index) {
        return getConfig("mapData.yml").get(index);
    }

    public World getLobbyWorld() {
        if (_lobbyWorld == null)
            _lobbyWorld = Bukkit.getWorld("lobby");
        if (_lobbyWorld == null)
            _lobbyWorld = Bukkit.createWorld(new WorldCreator("lobby"));
        return _lobbyWorld;
    }

    public World getGameWorld() {
        return _gameWorld;
    }

    public File getGameWorldFolder() {
        return new File(_worldTemplate.getName());
    }

    public String getName() {
        return _worldTemplate.getName();
    }

    public File getWorldTemplate() {
        return _worldTemplate;
    }
}