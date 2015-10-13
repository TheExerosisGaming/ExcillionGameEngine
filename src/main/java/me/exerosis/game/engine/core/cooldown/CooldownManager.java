package me.exerosis.game.engine.core.cooldown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CooldownManager extends GameComponent {
    private List<Cooldown> _cooldowns = new ArrayList<>();

    public CooldownManager(Game game) {
        super(game);
    }

    public boolean isCooling(Player player) {
        for (Cooldown cooldown : _cooldowns)
            if (cooldown.isCooling(player))
                return true;
        return false;
    }

    public boolean isCooling(Player player, Class<? extends Cooldown> cooldownClass) {
        for (Cooldown cooldown : _cooldowns)
            if (cooldownClass.isInstance(cooldown))
                if (cooldown.isCooling(player))
                    return true;
        return false;
    }

    public List<Cooldown> getCooldowns() {
        return _cooldowns;
    }

    @Override
    public void onEnable() {
        _cooldowns = getGame().getInstancePool().getInstances(Cooldown.class);
    }

    @Override
    public void onDisable() {
    }
}
