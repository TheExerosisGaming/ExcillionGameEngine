package me.exerosis.game.engine.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class FileGetter {
    private File _location;

    public FileGetter() {
        _location = getDirectory();
    }

    public abstract File getDirectory();

    public String getPath() {
        return _location.getPath();
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
}
