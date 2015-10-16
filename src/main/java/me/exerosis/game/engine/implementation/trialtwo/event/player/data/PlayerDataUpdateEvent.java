package me.exerosis.game.engine.implementation.trialtwo.event.player.data;

import me.exerosis.component.event.Cancellable;
import me.exerosis.game.engine.implementation.trialtwo.event.GameEvent;
import org.bukkit.entity.Player;

public class PlayerDataUpdateEvent extends GameEvent implements Cancellable {
    private final int _type;
    private Object _data;
    private String _id;
    private Player _player;

    public PlayerDataUpdateEvent(String id, Object data, Player player, int type) {
        _id = id;
        _data = data;
        _type = type;
        setPlayer(player);
    }

    public int getType() {
        return _type;
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
