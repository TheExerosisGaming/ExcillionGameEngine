package me.exerosis.game.engine.core.cooldown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class Cooldown extends GameComponent {
    private Map<Player, LoadingBar> _players = new HashMap<>();

    public Cooldown(Game game) {
        super(game);
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
        _players.remove(player);
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
        registerListener();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        _players.values().forEach(LoadingBar::stop);
        unregisterListener();
        super.onDisable();
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
