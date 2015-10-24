package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.staff;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.cooldown.Cooldown;
import org.bukkit.ChatColor;

public class MoltenShockwaveCooldown extends Cooldown {
    public MoltenShockwaveCooldown(Game game) {
        super(game);
    }

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