package me.exerosis.game.engine.implementation.old.core.cooldown;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class Cooldown extends Component {
    private Map<Player, LoadingBar> _players = new HashMap<Player, LoadingBar>();

    public Cooldown() {
    }

    public boolean isCooling(Player player) {
        return _players.containsKey(player);
    }

    public void addPlayer(Player player) {
        if (isCooling(player))
            return;
        LoadingBar bar = new LoadingBar(getPriority(), player, getCompleteMessage(), getTime()) {
            public void done() {
                _players.remove(player, this);
            }
        };
        bar.start();
        _players.put(player, bar);
    }

    public void removePlayer(Player player) {

    }

    public void pause(boolean pause) {
        for (LoadingBar bar : _players.values())
            bar.pause(pause);
    }

    public abstract int getSlot();

    public abstract String getCompleteMessage();

    public abstract double getTime();

    public abstract int getPriority();

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        for (LoadingBar bar : _players.values())
            bar.stop();
        unregisterListener(this);
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!_players.containsKey(player))
            return;
        if (event.getNewSlot() == getSlot())
            _players.get(player).pause(false);
        else
            _players.get(player).pause(true);
    }
}
