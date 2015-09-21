package me.exerosis.game.engine.implementation.old.event.game.post;

import me.exerosis.game.engine.implementation.old.event.game.GameEvent;
import me.exerosis.game.engine.core.GameState;
import org.apache.commons.lang.Validate;

public class PostGameStateChangeEvent extends GameEvent {

    private GameState _gameState;

    public PostGameStateChangeEvent(GameState gameState) {
        Validate.notNull(gameState, "The old GameState cannot be null.");
        _gameState = gameState;
    }

    public GameState getGameState() {
        return _gameState;
    }
}
