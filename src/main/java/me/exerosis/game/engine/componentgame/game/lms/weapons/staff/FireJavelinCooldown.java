package me.exerosis.game.engine.componentgame.game.lms.weapons.staff;

import me.exerosis.game.engine.componentgame.component.core.cooldown.Cooldown;
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