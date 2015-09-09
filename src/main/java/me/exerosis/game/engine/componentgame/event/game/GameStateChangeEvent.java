package me.exerosis.game.engine.componentgame.event.game;

import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.apache.commons.lang.Validate;

public class GameStateChangeEvent extends GameEvent {

    private GameState _newGameState;
    private GameState _oldGameState;

    public GameStateChangeEvent(GameState oldGameState, GameState newGameState) {
        Validate.notNull(oldGameState, "The old GameState cannot be null.");
        Validate.notNull(newGameState, "The new GameState cannot be null.");
        this._oldGameState = oldGameState;
        this._newGameState = newGameState;
    }

    public GameState getNewGameState() {
        return _newGameState;
    }

    public void setNewGameState(GameState newGameState) {
        Validate.notNull(newGameState, "The new GameState cannot be null.");
        this._newGameState = newGameState;
    }

    public GameState getOldGameState() {
        return _oldGameState;
    }
}
