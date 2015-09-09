package me.exerosis.game.engine.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullUtil {
    public static ItemStack getSkullItem(String playerName) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(playerName);
        meta.setDisplayName(ChatColor.DARK_BLUE + playerName);
        skull.setItemMeta(meta);
        return skull;
    }
}
