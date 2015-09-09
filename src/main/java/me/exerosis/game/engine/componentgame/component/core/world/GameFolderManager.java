package me.exerosis.game.engine.componentgame.component.core.world;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.io.util.ArchiveUtils;
import me.exerosis.io.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class GameFolderManager extends Component {
    private File _gameFolder;
    private String _folderName;

    public GameFolderManager(String folderName) {
        _folderName = folderName;
        File gamesFolder = new File("Games");
        if (!gamesFolder.exists()) {
            gamesFolder.mkdirs();
            formatedPrint("Could not find games folder! Created directory 'Games'", true);
        }

        StringBuilder pathBuilder = new StringBuilder("Games/");
        pathBuilder.append(_folderName);

        _gameFolder = new File(pathBuilder.toString());

        if (!_gameFolder.exists()) {
            formatedPrint("Could not find game folder at " + _gameFolder.getAbsolutePath() + "! Creating a new directory for you :)", true);
            _gameFolder.mkdirs();
        } else
            formatedPrint("Found game folder at " + pathBuilder.toString() + "!");
    }

    public File getFile(String fileName) {
        return new File(new StringBuilder(getPath()).append('/').append(fileName).toString());
    }

    public YamlConfiguration getConfig(String fileName) {
        File config = getFile(fileName);
        if (config.exists())
            return YamlConfiguration.loadConfiguration(config);
        return null;
    }

    public <T> T getGameConfigValue(String index, Class<T> clazz) {
        return clazz.cast(getGameConfigValue(index));
    }

    public Object getGameConfigValue(String index) {
        return getConfig("gameConfig.yml").get(index);
    }

    public File hardGetFile(String backupURL, String uniqueContent) {
        formatedPrint("Searching for file.");
        File file = FileUtil.searchFolder(_gameFolder, uniqueContent);

        if (file != null) {
            formatedPrint("Found file!");
            return file;
        }

        formatedPrint("No file found, downloading new file from backup URL!", true);
        ArchiveUtils.downloadFile(backupURL, _gameFolder);
        formatedPrint("Downloaded file!");

        formatedPrint("Checking download integrity!");
        file = FileUtil.searchFolder(_gameFolder, uniqueContent);
        if (file == null) {
            formatedPrint("Download integrity check failed! Shutting down server!", true);
            Bukkit.shutdown();
            return null;
        }
        formatedPrint("Found file!");
        return file;
    }


    public String getPath() {
        return _gameFolder.getPath();
    }

    public File getGameFolder() {
        return _gameFolder;
    }

    public String getName() {
        return _folderName;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
