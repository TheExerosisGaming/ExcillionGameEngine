package me.exerosis.game.engine.componentgame.component.core.cooldown;

import me.exerosis.game.engine.componentgame.component.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CooldownManager extends Component {
    private ArrayList<Cooldown> _cooldowns;

    public CooldownManager() {
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

    public ArrayList<Cooldown> getCooldowns() {
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
