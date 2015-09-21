package me.exerosis.game.engine.implementation.trialtwo.event.player;

import org.bukkit.entity.Player;

public class GamePlayerEvent {
    private final Player _player;

    public GamePlayerEvent(Player player) {
        _player = player;
    }

    public Player getPlayer() {
        return _player;
    }
}