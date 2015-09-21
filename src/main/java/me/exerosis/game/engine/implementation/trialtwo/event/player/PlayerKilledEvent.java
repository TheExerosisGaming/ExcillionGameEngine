package me.exerosis.game.engine.implementation.trialtwo.event.player;

import me.exerosis.reflection.event.Cancellable;
import org.bukkit.entity.Player;

public class PlayerKilledEvent extends GamePlayerEvent implements Cancellable {
    private Player _killer;

    public PlayerKilledEvent(Player player, Player killer) {
        super(player);
        _killer = killer;
    }

    public void setKiller(Player killer) {
        _killer = killer;
    }

    public Player getKiller() {
        return _killer;
    }
}
