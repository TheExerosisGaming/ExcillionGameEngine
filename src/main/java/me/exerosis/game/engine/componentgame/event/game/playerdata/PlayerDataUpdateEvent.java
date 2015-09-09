package me.exerosis.game.engine.componentgame.event.game.playerdata;

import me.exerosis.game.engine.componentgame.event.game.GameEvent;
import org.bukkit.entity.Player;

public class PlayerDataUpdateEvent extends GameEvent {

    private Object _data;
    private String _id;
    private Player _player;

    public PlayerDataUpdateEvent(String id, Object data, Player player) {
        _id = id;
        _data = data;
        setPlayer(player);
    }

    public Object getData() {
        return _data;
    }

    public void setData(Object data) {
        _data = data;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public Player getPlayer() {
        return _player;
    }

    public void setPlayer(Player player) {
        _player = player;
    }
}
