package me.exerosis.game.engine.implementation.trialtwo.games.spleef.doublejump;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.cooldown.Cooldown;
import org.bukkit.ChatColor;

public class DoubleJumpCooldown extends Cooldown {
    public DoubleJumpCooldown(Game game) {
        super(game);
    }

    @Override
    public double getTime() {
        return 5;
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