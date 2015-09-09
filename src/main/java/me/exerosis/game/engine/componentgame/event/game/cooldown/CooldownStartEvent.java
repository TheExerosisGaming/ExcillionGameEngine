package me.exerosis.game.engine.componentgame.event.game.cooldown;

import me.exerosis.game.engine.componentgame.component.core.cooldown.Cooldown;
import me.exerosis.game.engine.componentgame.event.game.GameEvent;
import org.bukkit.entity.Player;

public class CooldownStartEvent extends GameEvent {
    private Cooldown _cooldown;
    private Player _player;

    public CooldownStartEvent(Cooldown cooldown, Player player) {
        _cooldown = cooldown;
        _player = player;
    }

    public Cooldown getCooldown() {
        return _cooldown;
    }

    public Player getPlayer() {
        return _player;
    }
}
