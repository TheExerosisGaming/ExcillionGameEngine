package me.exerosis.game.engine.componentgame.component.core.world;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WorldComponent extends Component {
    @Depend
    private GameFolderManager _gameFolderManager;
    private File _worldTemplate;
    private World _gameWorld;
    private World _lobbyWorld;
    private String _backupURL;

    public WorldComponent(String backupURL) {
        _backupURL = backupURL;
    }

    @EventHandler
    public void onLoad(WorldLoadEvent event) {
        if (!event.getWorld().getName().equals(getName()))
            return;
        formatedPrint("Loaded world!");

        _gameWorld = event.getWorld();
        _gameWorld.setDifficulty(Difficulty.NORMAL);
        _gameWorld.setTime(0);
        _gameWorld.setAutoSave(false);
        _gameWorld.setGameRuleValue("doDaylightCycle", "false");
        _gameWorld.setGameRuleValue("showDeathMessages", "false");
        _gameWorld.setGameRuleValue("showDeathMessages", "false");
        _gameWorld.setGameRuleValue("doMobSpawning", "false");

        getArena().setGameState(GameState.LOBBY);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(GameState.RESTARTING))
            tryUnload(() -> {
                getGame().disableGame();
                formatedPrint("Game ended, starting next game.");
                getArena().nextGame();
            });
    }

    private void tryLoad() {
        formatedPrint("Trying to load world!");
        if (_worldTemplate == null && !_worldTemplate.exists())
            _worldTemplate = _gameFolderManager.hardGetFile(_backupURL, "region");

        File worldFolder = new File(getName());
        worldFolder.mkdirs();
        try {
            FileUtils.copyDirectory(_worldTemplate, worldFolder, false);
        } catch (IOException e) {
            formatedPrint("Failed to copy template to the server directory, shutting down server!", true);
            Bukkit.shutdown();
            return;
        }
        WorldCreator worldCreator = new WorldCreator(getName());
        worldCreator.type(WorldType.FLAT);
        Bukkit.createWorld(worldCreator);
    }

    private void tryUnload(Runnable runWhenUnloaded) {
        formatedPrint("Trying to unload world!");
        _gameWorld = Bukkit.getWorld(getName());

        if (_gameWorld == null) {
            if (runWhenUnloaded != null)
                runWhenUnloaded.run();
            return;
        }

        //Remove players
        for (Player player : _gameWorld.getPlayers())
            player.teleport(getLobbyWorld().getSpawnLocation().add(0, 4, 0));

        //Wait 1 second before unloading the world
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            if (!Bukkit.unloadWorld(_gameWorld, false)) {
                formatedPrint("Unable to unload world, please fix the problem!");
                getGame().disableGame();
                Bukkit.shutdown();
                return;
            }
            //Wait 4 seconds before trying to remove the world folder
            Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
                final File toDelete = new File(getName());

                @Override
                public void run() {
                    formatedPrint("World unloaded.");
                    try {
                        formatedPrint("Removing world folder.");
                        FileUtils.deleteDirectory(toDelete);
                    } catch (IOException e) {
                        e.printStackTrace();
                        formatedPrint("Failed to remove the world directory, shutting down server!", true);
                        Bukkit.shutdown();
                        return;
                    }

                    formatedPrint("Folder removed.");
                    if (runWhenUnloaded != null)
                        runWhenUnloaded.run();
                }
            }, 40);
        }, 20);
    }


    @Override
    public void onEnable() {
        registerListener(this);
        _worldTemplate = _gameFolderManager.hardGetFile(_backupURL, "region");
        tryLoad();
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    public String getMapDsc() {
        StringBuilder mapDsc = new StringBuilder();
        try {
            StringBuilder pathBuilder = new StringBuilder(getGameWorldFolder().getPath());
            File file = new File(pathBuilder.append("/mapDsc.txt").toString());
            for (String line : Files.readAllLines(Paths.get(file.toURI()))) {
                mapDsc.append(ChatColor.translateAlternateColorCodes('&', line));
                mapDsc.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapDsc.toString();
    }

    public YamlConfiguration getConfig(String fileName) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append(_worldTemplate.getPath());
        pathBuilder.append('/');
        pathBuilder.append(fileName);
        File config = new File(pathBuilder.toString());
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