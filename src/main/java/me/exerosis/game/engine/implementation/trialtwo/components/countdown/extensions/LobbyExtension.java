package me.exerosis.game.engine.implementation.trialtwo.components.countdown.extensions;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyExtension extends CountdownExtension {
    private final SpawnpointComponent _spawnpointComponent;
    private final int _startPlayers;

    public LobbyExtension(Game game, Countdown countdown, SpawnpointComponent spawnpointComponent) {
        super(game, countdown);
        _spawnpointComponent = spawnpointComponent;
        _startPlayers = getGame().getGameConfigValue("startPlayers", Integer.class);
    }

    @Override
    public void onEnable() {
        if (getGameState().equals(GameState.LOBBY))
            if (getPlayers().size() >= _startPlayers)
                getCountdown().start();
        super.onEnable();
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.LOBBY))
            if (getPlayers().size() >= _startPlayers)
                getCountdown().start();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (getCountdown().isRunning())
            getCountdown().restart();
        if (getPlayers().size() >= _startPlayers)
            getCountdown().start();
    }

    @Override
    public void stop(int index) {
        if (index == 0) {
            _spawnpointComponent.setIndex(0);
            getPlayers().forEach(_spawnpointComponent::sendToSpawn);
        }
    }

    public int getStartPlayers() {
        return _startPlayers;
    }
}