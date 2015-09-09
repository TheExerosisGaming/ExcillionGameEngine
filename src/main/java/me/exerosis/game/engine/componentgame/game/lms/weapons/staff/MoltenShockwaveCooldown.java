package me.exerosis.game.engine.componentgame.game.lms.weapons.staff;

import me.exerosis.game.engine.componentgame.component.core.cooldown.Cooldown;
import org.bukkit.ChatColor;

public class MoltenShockwaveCooldown extends Cooldown {
    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public String getCompleteMessage() {
        return ChatColor.GREEN + "Molten Shockwave Ready!";
    }

    @Override
    public double getTime() {
        return 20;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
