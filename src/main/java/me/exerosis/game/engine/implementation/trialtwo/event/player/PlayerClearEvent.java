package me.exerosis.game.engine.implementation.trialtwo.event.player;

import me.exerosis.game.engine.implementation.trialtwo.event.player.GamePlayerEvent;
import me.exerosis.reflection.event.Cancellable;
import org.bukkit.entity.Player;

public class PlayerClearEvent extends GamePlayerEvent implements Cancellable {
    private boolean _full;

    public PlayerClearEvent(Player player, boolean full) {
        super(player);
        _full = full;
    }

    public boolean isFull() {
        return _full;
    }

    public void setFull(boolean full) {
        _full = full;
    }
}
