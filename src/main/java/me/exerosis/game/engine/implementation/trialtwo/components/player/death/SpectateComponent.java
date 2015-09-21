package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.StateComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.implementation.trialtwo.components.player.PlayerComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerSpectateEvent;
import me.exerosis.reflection.event.EventListener;
import me.exerosis.reflection.event.Priority;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SpectateComponent extends StateComponent {
    private List<Player> _spectatingPlayers = new ArrayList<>();
    private PlayerComponent _playerComponent;

    public SpectateComponent(Game game, PlayerComponent playerComponent) {
        super(game, GameLocation.GAME_WORLD);
        _playerComponent = playerComponent;
    }

    @EventListener(priority = Priority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        setSpectating(event.getPlayer());
    }

    @EventListener
    public void onLeave(PlayerQuitEvent event) {
        if (_spectatingPlayers.contains(event.getPlayer()))
            _spectatingPlayers.remove(event.getPlayer());
    }

    @EventListener(priority = Priority.HIGHEST)
    public void onDeath(PlayerKilledEvent event) {
        setSpectating(event.getPlayer());
    }

    //Primary Methods
    public boolean isSpectating(Player player) {
        return _spectatingPlayers.contains(player);
    }

    public void setSpectating(Player player) {
        if (!isEnabled() || isSpectating(player))
            return;

        callEvent(new PlayerSpectateEvent(player), event -> {
            if (event.isCancelled())
                return;
            player.setGameMode(GameMode.SPECTATOR);
            _spectatingPlayers.add(player);
            player.setVelocity(new Vector());
            _playerComponent.clearPlayer(player);
        });
    }

    public void removeSpectating(Player player) {
        if (isSpectating(player))
            _spectatingPlayers.remove(player);
    }

    public void removeAll() {
        getPlayers().forEach(_playerComponent::clearPlayer);
        _spectatingPlayers.clear();
    }

    @Override
    public void onDisable() {
        removeAll();
        super.onDisable();
    }

    //Getters.
    public List<Player> getSpectatingPlayers() {
        return _spectatingPlayers;
    }

    public int getNumberOfSpectators() {
        return _spectatingPlayers.size();
    }

    public List<Player> getGamePlayers() {
        List<Player> gamePlayers = getPlayers();
        gamePlayers.removeAll(_spectatingPlayers);
        return gamePlayers;
    }

    public int getNumberPlayers() {
        return getGamePlayers().size();
    }
}