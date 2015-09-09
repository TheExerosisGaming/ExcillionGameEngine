package me.exerosis.game.engine.componentgame.event.game.player;

import me.exerosis.game.engine.componentgame.event.game.GameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlayerKilledEvent extends GameEvent implements Cancellable {
    private Player _player;
    private boolean _cancelled;
    private Player _killed;

    public PlayerKilledEvent(Player player, Player killed) {
        _player = player;
        _killed = killed;
    }

    public Player getPlayer() {
        return _player;
    }

    public Player getKiller() {
        return _killed;
    }

    @Override
    public boolean isCancelled() {
        return _cancelled;
    }

    @Override
    public void setCancelled(boolean paramBoolean) {
        _cancelled = paramBoolean;
    }
}