package me.exerosis.game.engine.implementation.trialtwo.components.world;

import me.exerosis.io.util.ArchiveUtils;
import me.exerosis.io.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameFolderManager {
    private YamlConfiguration _gamesConfig;

    public GameFolderManager() {
    }

    public YamlConfiguration getGamesConfig() {
        if (_gamesConfig != null)
            return _gamesConfig;
        File gamesConfigFile = new File("games.yml");
        if (!gamesConfigFile.isFile())
            try {
                if (gamesConfigFile.createNewFile())
                    System.err.println("Failed to locate 'games.yml', creating file!");
            } catch (IOException e) {
                System.err.println("Failed to create 'games.yml', shutting down server!");
                Bukkit.shutdown();
            }
        System.out.println("Created 'games.yml', loading configuration!");
        _gamesConfig = YamlConfiguration.loadConfiguration(gamesConfigFile);
        return _gamesConfig;
    }

    public YamlConfiguration saveGamesConfig() {
        try {
            _gamesConfig.save(new File("games"));
        } catch (IOException e) {
            System.err.println("Failed to save 'games.yml'!");
        }
        return _gamesConfig;
    }

    public YamlConfiguration reloadGamesConfig() {
        saveGamesConfig();
        _gamesConfig = null;
        return getGamesConfig();
    }

    public GameStats getGameStats(String name) {
        return new GameStats(name);
    }


    public class GameStats {
        private String name;
        private GameFileManager _gameFileManager;

        private GameStats(String name) {
            this.name = name;
            _gamesConfig.addDefault(name + ".backupURL", "aURL.com");
            _gamesConfig.addDefault(name + ".folderPath", "Games/" + name);
            save();
        }

        public GameStats reload() {
            reloadGamesConfig();
            return this;
        }

        public GameStats save() {
            saveGamesConfig();
            return this;
        }

        public GameStats setBackupURL(String URL) {
            _gamesConfig.set(name + ".backupURL", URL);
            return this;
        }

        public GameStats setFolderPath(String path) {
            _gamesConfig.set(name + ".folderPath", path);
            return this;
        }

        public String getBackupURL() {
            return _gamesConfig.getString(name + ".backupURL");
        }

        public String getFolderPath() {
            return _gamesConfig.getString(name + ".folderPath");
        }

        public String getName() {
            return name;
        }

        public GameFileManager getFileManager() {
            if (_gameFileManager == null)
                _gameFileManager = new GameFileManager();
            return _gameFileManager;
        }

        public class GameFileManager {
            private GameFileManager() {
            }

            public File getFolder() {
                File folder = new File(getFolderPath());

                if (folder.mkdirs())
                    System.err.println("Could not find this folder! Created directory: '" + folder.getPath() + "'");
                System.out.println("Searching for file.");

                File file = FileUtil.searchFolder(folder, "level.dat");
                if (file != null) {
                    System.out.println("Found file!");
                    return file;
                }

                System.err.println("Failed, file found, downloading new file from backup URL!");
                download();
                System.out.println("Downloaded file!");

                System.out.println("Checking download integrity!");
                file = FileUtil.searchFolder(folder, "level.dat");
                if (file == null) {
                    System.err.println("Download integrity check failed! Shutting down server!");
                    Bukkit.shutdown();
                    return null;
                }
                System.out.println("Found file!");
                return file;
            }

            public void download() {
                ArchiveUtils.downloadFile(getBackupURL(), new File(getFolderPath()));
            }

            public File getFile(String fileName) {
                return FileUtil.searchFolder(getFolder(), fileName);
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
        }
    }
}