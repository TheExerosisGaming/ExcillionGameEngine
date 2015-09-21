package me.exerosis.game.engine.implementation.old.event.game.playerdata;

import me.exerosis.game.engine.implementation.old.core.player.PlayerData;
import me.exerosis.game.engine.implementation.old.event.game.GameEvent;
import org.bukkit.entity.Player;

public class PlayerDataLoadEvent extends GameEvent {

    private PlayerData _data;
    private Player _player;

    public PlayerDataLoadEvent(Player player, PlayerData data) {
        _player = player;
        _data = data;
    }

    public PlayerData getData() {
        return _data;
    }

    public Player getPlayer() {
        return _player;
    }
}
