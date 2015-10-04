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

    @EventListener
    public void onRestart(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.RESTARTING))
            if (getPlayers().size() >= _gameComponent.getStartPlayers())
                getCountdown().start();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (getCountdown().isRunning())
            getCountdown().restart();
        if (getPlayers().size() >= _gameComponent.getStartPlayers())
            getCountdown().start();
    }

    @Override
    public void tick(int timeLeft) {
        System.out.println(timeLeft);
    }

    @Override
    public void done() {
        getPlayers().forEach(_spawnpointComponent::sendToSpawn);
    }
}
