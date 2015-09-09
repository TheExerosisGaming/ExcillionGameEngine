package me.exerosis.game.engine.componentgame.component.core.player;

import me.exerosis.game.engine.componentgame.event.game.playerdata.PlayerDataUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private Map<String, Object> _data = new HashMap<String, Object>();
    private Player _player;

    public PlayerData(Player player) {
        _player = player;
    }

    public void addData(String id, Object data) {
        PlayerDataUpdateEvent event = new PlayerDataUpdateEvent(id, data, _player);
        Bukkit.getPluginManager().callEvent(event);
        _data.put(id, data);
    }

    public Object getData(String id) {
        return _data.get(id);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(String id, Class<T> type) {
        return (T) _data.get(id);
    }

    public void setData(String id, Object data) {
        PlayerDataUpdateEvent event = new PlayerDataUpdateEvent(id, data, _player);
        Bukkit.getPluginManager().callEvent(event);
        _data.replace(event.getId(), event.getData());
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
        addData(id, num);
    }

    public void decrementIntBy(String id, int amount) {
        Integer num = (Integer) _data.remove(id);
        num -= amount;
        addData(id, num);
    }

    public Map<String, Object> getData() {
        return _data;
    }
}
