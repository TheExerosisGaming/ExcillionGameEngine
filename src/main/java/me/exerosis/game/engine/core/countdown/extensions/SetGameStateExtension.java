package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;

public class SetGameStateExtension extends CountdownExtension {
    private GameState _nextGameState;

    public SetGameStateExtension(Countdown countdown, Game game, GameState nextGameState) {
        super(game, countdown);
        _nextGameState = nextGameState;
    }

    @Override
    public void done() {
        getGame().setSystemState(_nextGameState);
    }

    @Override
    public void setGameState(GameState state) {
        if (getGame().getSystemState().equals(_nextGameState))
            getCountdown().stop();
    }

    public void setNextGameState(GameState nextGameState) {
        _nextGameState = nextGameState;
    }

    public GameState getNextGameState() {
        return _nextGameState;
    }
}