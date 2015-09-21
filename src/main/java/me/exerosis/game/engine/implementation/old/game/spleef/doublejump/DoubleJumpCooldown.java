package me.exerosis.game.engine.implementation.old.game.spleef.doublejump;

import org.bukkit.ChatColor;

public class DoubleJumpCooldown extends Cooldown {
    public DoubleJumpCooldown() {
    }

    @Override
    public double getTime() {
        return 30;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public String getCompleteMessage() {
        return ChatColor.GREEN + "Double Jump Recharged";
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
