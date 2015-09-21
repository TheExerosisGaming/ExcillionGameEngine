package me.exerosis.game.engine.implementation.old.game.lms.weapons.staff;

import org.bukkit.ChatColor;

public class FireJavelinCooldown extends Cooldown {
    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public String getCompleteMessage() {
        return ChatColor.GREEN + "Flame Javelin Ready!";
    }

    @Override
    public double getTime() {
        return 4;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}