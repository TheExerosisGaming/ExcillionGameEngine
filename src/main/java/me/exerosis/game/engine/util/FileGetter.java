package me.exerosis.game.engine.util;

import me.exerosis.io.util.ArchiveUtils;
import me.exerosis.io.util.FileUtil;
import org.bukkit.Bukkit;
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

    public File hardGetFile(File folder, String backupURL, String uniqueContent) {
        if (folder.mkdirs())
            System.err.println("Could not find this folder! Created directory: '" + folder.getPath() + "'");
        System.out.println("Searching for file.");
        File file = FileUtil.searchFolder(folder, uniqueContent);

        if (file != null) {
            System.out.println("Found file!");
            return file;
        }

        System.err.println("Failed, file found, downloading new file from backup URL!");
        ArchiveUtils.downloadFile(backupURL, folder);
        System.out.println("Downloaded file!");

        System.out.println("Checking download integrity!");
        file = FileUtil.searchFolder(folder, uniqueContent);
        if (file == null) {
            System.err.println("Download integrity check failed! Shutting down server!");
            Bukkit.shutdown();
            return null;
        }
        System.out.println("Found file!");
        return file;
    }


    public File getFile(String fileName) {
        return new File(getPath() + '/' + fileName);
    }

    public YamlConfiguration getConfig(String fileName) {
        File config = getFile(fileName);
        if (config.exists())
            return YamlConfiguration.loadConfiguration(config);
        return null;
    }
}