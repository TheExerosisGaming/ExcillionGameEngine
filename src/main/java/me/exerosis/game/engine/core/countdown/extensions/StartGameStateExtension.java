package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;

public class StartGameStateExtension extends CountdownExtension {
    private final GameState _startGameState;

    public StartGameStateExtension(Countdown countdown, Game game, GameState startGameState) {
        super(game, countdown);
        _startGameState = startGameState;
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(_startGameState))
            getCountdown().start();
    }

    public GameState getStartGameState() {
        return _startGameState;
    }

    @Override
    public void onEnable() {
        if (getGameState().equals(_startGameState))
            getCountdown().start();
        super.onEnable();
    }
}