package me.exerosis.game.engine.implementation.old.event.game.player;

import me.exerosis.game.engine.implementation.old.event.game.GameEvent;
import org.bukkit.entity.Player;

public class PlayerSpectateEvent extends GameEvent {
    private Player _player;

    public PlayerSpectateEvent(Player player) {
        _player = player;
    }

    public Player getPlayer() {
        return _player;
    }
}
