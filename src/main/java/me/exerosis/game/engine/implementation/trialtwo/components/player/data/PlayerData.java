package me.exerosis.game.engine.implementation.trialtwo.components.player.data;

import me.exerosis.component.event.EventManager;
import me.exerosis.game.engine.implementation.trialtwo.event.player.data.PlayerDataUpdateEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    public static final int ADD_DATA = 0;
    public static final int REMOVE_DATA = 1;
    public static final int SET_DATA = 2;

    private Map<String, Object> _data = new HashMap<>();
    private Player _player;
    private EventManager _eventManager;

    public PlayerData(Player player, EventManager eventManager) {
        _player = player;
        _eventManager = eventManager;
    }

    public void addData(String id, Object data) {
        _eventManager.callEvent(new PlayerDataUpdateEvent(id, data, _player, ADD_DATA), event -> {
            if (!event.isCancelled())
                _data.put(event.getId(), event.getData());
        });
    }

    public Object getData(String id) {
        return _data.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String id, Class<T> type) {
        return (T) _data.get(id);
    }

    public void setData(String id, Object data) {
        _eventManager.callEvent(new PlayerDataUpdateEvent(id, data, _player, SET_DATA), event -> {
            if (!event.isCancelled())
                _data.replace(event.getId(), event.getData());
        });
    }

    public void removeData(String id) {
        _eventManager.callEvent(new PlayerDataUpdateEvent(id, getData(id), _player, REMOVE_DATA), event -> {
            if (!event.isCancelled())
                _data.remove(event.getId());
        });
    }

    public void increment(String id) {
        incrementIntBy(id, 1);
    }

    public void decrement(String id) {
        decrementIntBy(id, 1);
    }

    public void incrementIntBy(String id, int amount) {
        Integer num = (Integer) _data.remove(id);
        num += amount;
        setData(id, num);
    }

    public void decrementIntBy(String id, int amount) {
        Integer num = (Integer) _data.remove(id);
        num -= amount;
        setData(id, num);
    }

    public Map<String, Object> getData() {
        return _data;
    }
}