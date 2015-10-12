package me.exerosis.game.engine.implementation.trialtwo.components.countdown.extensions;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreGameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyExtension extends CountdownExtension {
    private final CoreGameComponent _gameComponent;
    private final SpawnpointComponent _spawnpointComponent;

    public LobbyExtension(Game game, Countdown countdown, CoreGameComponent gameComponent, SpawnpointComponent spawnpointComponent) {
        super(game, countdown);
        _gameComponent = gameComponent;
        _spawnpointComponent = spawnpointComponent;
    }

    @Override
    public void onEnable() {
        if (getGameState().equals(GameState.LOBBY))
            if (getPlayers().size() >= _gameComponent.getStartPlayers())
                getCountdown().start();
        super.onEnable();
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.LOBBY))
            if (getPlayers().size() >= _gameComponent.getStartPlayers())
                getCountdown().start();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (getCountdown().isRunning())
            getCountdown().restart();
        if (getPlayers().size() >= _gameComponent.getStartPlayers())
            getCountdown().start();
    }

    @Override
    public void stop(int index) {
        if (index == 0) {
            _spawnpointComponent.setIndex(0);
            getPlayers().forEach(_spawnpointComponent::sendToSpawn);
        }
    }
}