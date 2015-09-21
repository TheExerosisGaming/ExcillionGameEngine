package me.exerosis.game.engine.implementation.old.core.world;

import me.exerosis.io.util.ArchiveUtils;
import me.exerosis.io.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CopyOfGameFolderManager extends Component {
    private File _gameFolder;
    private String _folderName;
    private YamlConfiguration _backupURLS;

    public CopyOfGameFolderManager(String folderName) {
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

        pathBuilder.append("safetyURLs.yml");
        File safteyURLs = new File(pathBuilder.toString());
        if (safteyURLs.exists()) {
            formatedPrint("Found saftyURL file at " + pathBuilder.toString() + "!");
            _backupURLS = getConfig("safetyURLs.yml");
        } else {
            formatedPrint("Could not find saftyURL file at " + pathBuilder.toString() + "! Creating a new file for you. Unprovided file lookups will be fatal.", true);
            try {
                safteyURLs.createNewFile();
            } catch (IOException e) {
                formatedPrint("Could not create a saftyURLs.yml file for you!", true);
            }
        }
    }

    //Map Dsc getters.
    public String safeGetMapDsc(String backupURL, String uniqueContent) {
        StringBuilder mapDsc = new StringBuilder();
        for (String line : safeGetText(backupURL, uniqueContent)) {
            mapDsc.append(ChatColor.translateAlternateColorCodes('&', line));
            mapDsc.append("\n");
        }
        return mapDsc.toString();
    }

    public String hardGetMapDsc(String uniqueContent) {
        return safeGetMapDsc("", uniqueContent);
    }

    //Text getters.
    public List<String> safeGetText(String backupURL, String uniqueContent) {
        File file = safeGetFile(backupURL, uniqueContent);
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> hardGetText(String uniqueContent) {
        return safeGetText("", uniqueContent);
    }

    public List<String> getText(String uniqueContent) {
        File file = getFile(uniqueContent);
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Config getters.
    public YamlConfiguration safeGetConfig(String backupURL, String uniqueContent) {
        File config = safeGetFile(backupURL, uniqueContent);
        if (config.exists())
            return YamlConfiguration.loadConfiguration(config);
        return null;
    }

    public YamlConfiguration hardGetConfig(String uniqueContent) {
        return safeGetConfig("", uniqueContent);
    }

    public YamlConfiguration getConfig(String uniqueContent) {
        File config = getFile(uniqueContent);
        if (config.exists())
            return YamlConfiguration.loadConfiguration(config);
        return null;
    }

    //File getters.
    public File safeGetFile(String backupURL, String uniqueContent) {
        formatedPrint("Searching for file.");
        File file = FileUtil.searchFolder(_gameFolder, uniqueContent);

        if (file != null) {
            formatedPrint("Found file!");
            return file;
        } else if (!backupURL.equals("")) {
            formatedPrint("Failed to find file, no backupURL provided! Checking safetyURLs.yml!", true);
            if (_backupURLS.contains(uniqueContent))

                formatedPrint("Failed to find file, no backupURL provided! Shutting down server!", true);
            Bukkit.shutdown();
            return null;
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

    public File hardGetFile(String fileName) {
        return safeGetFile("", fileName);
    }

    public File getFile(String uniqueContent) {
        return FileUtil.searchFolder(_gameFolder, uniqueContent);
    }

    //Getters.
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
