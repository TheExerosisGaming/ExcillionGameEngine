package me.exerosis.game.engine.core.factory;

import me.exerosis.component.factory.SystemFactory;
import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.util.FileGetter;
import me.exerosis.io.Remote.RemoteFolder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class ConfigurationGameFactory implements SystemFactory {
    private YamlConfiguration _gamesConfig;
    private File _gamesConfigFile;
    private List<Game> games = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public ConfigurationGameFactory(Plugin plugin, Arena arena, Class<? extends Game>... gameClasses) {
        _gamesConfigFile = new File("games.yml");
        getGamesConfig();

        for (Class<?> clazz : gameClasses) {
            String classType = clazz.getSimpleName();
            String namePath = classType + ".name";
            String URLPath = classType + ".backupURL";
            String folderPath = classType + ".folderPath";

            _gamesConfig.addDefault(namePath, classType);
            _gamesConfig.addDefault(URLPath, "aURL.com");
            _gamesConfig.addDefault(folderPath, "Games/" + classType);
            _gamesConfig.options().copyDefaults(true);
            saveGamesConfig();

            try {
                Constructor<Game> constructor = (Constructor<Game>) clazz.getConstructor(Plugin.class, Arena.class, RemoteFolder.class, String.class);
                RemoteFolder remoteFolder = new RemoteFolder(new File(_gamesConfig.getString(folderPath)), _gamesConfig.getString(URLPath));
                remoteFolder.sync();
                Object object = constructor.newInstance(plugin, arena, remoteFolder, _gamesConfig.getString(namePath));
                games.add((Game) object);

            } catch (Exception e) {
                e.printStackTrace();
                /*catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                System.err.println("Failed to instantiate class '" + classType + "'!");
            } catch (NoSuchMethodException e) {
                System.err.println("Failed to locate constructor with params: Plugin, Arena, RemoteFolder!");
            } catch (MalformedURLException e) {
                System.err.println("Failed to connect to that URL, make sure it is correct!");
            }*/
            }
        }
    }

    public List<Game> getGames() {
        return games;
    }

    public YamlConfiguration getGamesConfig() {
        if (_gamesConfig == null)
            _gamesConfig = FileGetter.getConfig(_gamesConfigFile);
        return _gamesConfig;
    }

    public void saveGamesConfig() {
        try {
            _gamesConfig.save(_gamesConfigFile);
        } catch (IOException e) {
            System.err.println("Failed to save config 'games.yml'!");
        }
    }
}