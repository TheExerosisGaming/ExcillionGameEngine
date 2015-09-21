package me.exerosis.game.engine.implementation.old.core.player.death.spectate;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.event.PlayerLeaveEvent;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.old.event.game.player.PlayerSpectateEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class SpectateComponent extends Component {
    private ArrayList<Player> _spectatingPlayers = new ArrayList<Player>();
    @Depend
    private PlayerComponent _playerComponent;
    @Depend
    private SpawnpointComponent _spawnpointComponent;

    public SpectateComponent() {
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING))
            _playerComponent.sendToSpawn(event.getPlayer());
        else
            setSpectating(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerLeaveEvent event) {
        if (_spectatingPlayers.contains(event.getPlayer()))
            _spectatingPlayers.remove(event.getPlayer());
    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.RESTARTING))
            removeAll();
    }

    //Primary Methods
    public boolean isSpectating(Player player) {
        return _spectatingPlayers.contains(player);
    }

    public void setSpectating(Player player) {
        player.teleport(_spawnpointComponent.getSpectateLocation(player));

        if (getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING))
            return;

        if (isSpectating(player))
            return;
        player.setGameMode(GameMode.SPECTATOR);
        _spectatingPlayers.add(player);
        player.setVelocity(new Vector());
        Bukkit.getPluginManager().callEvent(new PlayerSpectateEvent(player));
    }

    public void removeSpectating(Player player) {
        if (!isSpectating(player))
            return;
        _spectatingPlayers.remove(player);
        _playerComponent.sendToSpawn(player);
    }

    public void removeAll() {
        for (Player player : _spectatingPlayers)
            _playerComponent.sendToSpawn(player);
        _spectatingPlayers.clear();
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        removeAll();
    }

    //Getters.
    public ArrayList<Player> getSpectatingPlayers() {
        return _spectatingPlayers;
    }

    public int getNumberOfSpectators() {
        return _spectatingPlayers.size();
    }

    public ArrayList<Player> getGamePlayers() {
        ArrayList<Player> gamePlayers = new ArrayList<Player>(Arena.getPlayers());
        gamePlayers.removeAll(_spectatingPlayers);
        return gamePlayers;
    }

    public int getNumberPlayers() {
        return getGamePlayers().size();
    }
}
