package me.exerosis.game.engine.implementation.old.event;

import me.exerosis.game.engine.implementation.old.event.game.GameEvent;
import org.bukkit.entity.Player;

public class PlayerLeaveEvent extends GameEvent {
    private Player _player;
    private String _quitMessage;

    public PlayerLeaveEvent(Player player, String quitMessage) {
        _player = player;
        _quitMessage = quitMessage;
    }

    public Player getPlayer() {
        return _player;
    }

    public String getQuitMessage() {
        return _quitMessage;
    }
}