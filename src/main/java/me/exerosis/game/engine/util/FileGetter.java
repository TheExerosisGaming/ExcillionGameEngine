package me.exerosis.game.engine.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileGetter {
    public static YamlConfiguration getConfig(File configFile) {
        String name = configFile.getName();
        if (!configFile.isFile()) {
            System.err.println("Failed to locate '" + name + "', creating file!");
            try {
                if (configFile.createNewFile())
                    System.out.println("Created '" + name + "'!");
            } catch (IOException e) {
                System.err.println("Failed to create '" + name + "', shutting down server!");
                Bukkit.shutdown();
                System.exit(1);
            }
        }
        System.out.println("Loading configuration '" + name + "'!");
        return YamlConfiguration.loadConfiguration(configFile);
    }
}