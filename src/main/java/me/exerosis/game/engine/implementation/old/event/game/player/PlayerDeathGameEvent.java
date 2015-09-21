package me.exerosis.game.engine.implementation.old.event.game.player;

import me.exerosis.game.engine.implementation.old.event.game.GameEvent;
import org.bukkit.entity.Player;

public class PlayerDeathGameEvent extends GameEvent {

    private Player _killer;
    private Player _killed;

    public PlayerDeathGameEvent(Player killer, Player killed) {
        _killer = killer;
        _killed = killed;
    }

    public Player getKilled() {
        return _killed;
    }

    public Player getKiller() {
        return _killer;
    }

}
