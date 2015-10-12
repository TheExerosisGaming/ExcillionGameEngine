package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.core.state.GameState;

public class SetGameStateExtension extends CountdownExtension {
    private GameState _nextGameState;

    public SetGameStateExtension(Countdown countdown, Game game, GameState nextGameState) {
        super(game, countdown);
        _nextGameState = nextGameState;
    }

    @Override
    public void stop(int index) {
        if (index == 0)
            setGameState(_nextGameState);
    }

    public GameState getNextGameState() {
        return _nextGameState;
    }

    public void setNextGameState(GameState nextGameState) {
        _nextGameState = nextGameState;
    }
}