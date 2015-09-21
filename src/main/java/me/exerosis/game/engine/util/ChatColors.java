package me.exerosis.game.engine.util;

import org.bukkit.ChatColor;

public interface ChatColors {
    default String darkGray() {
        return ChatColor.DARK_GRAY.toString();
    }

    default String gray() {
        return ChatColor.GRAY.toString();
    }

    default String darkBlue() {
        return ChatColor.DARK_BLUE.toString();
    }

    default String reset() {
        return ChatColor.RESET.toString();
    }

    default String bold() {
        return ChatColor.BOLD.toString();
    }

    default String boldDarkGray() {
        return ChatColor.BOLD.toString() + ChatColor.DARK_GRAY;
    }
    default String grayBold() {
        return ChatColor.BOLD.toString() + ChatColor.GRAY;
    }
}
