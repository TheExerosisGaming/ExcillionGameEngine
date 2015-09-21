package me.exerosis.game.engine.implementation.old.game.lms.weapons.throwing.axes;

import me.exerosis.game.engine.implementation.old.core.cooldown.Cooldown;
import org.bukkit.ChatColor;

public class ThrowingAxeCooldown extends Cooldown {

    public ThrowingAxeCooldown() {
    }

    @Override
    public double getTime() {
        return 0.3;
    }

    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public String getCompleteMessage() {
        return ChatColor.GREEN + "You grab another axe!";
    }

    @Override
    public int getPriority() {
        return 0;
    }
}