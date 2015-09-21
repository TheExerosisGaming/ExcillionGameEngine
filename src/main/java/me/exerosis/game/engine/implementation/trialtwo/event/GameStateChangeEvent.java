package me.exerosis.game.engine.implementation.trialtwo.event;

import me.exerosis.game.engine.core.state.GameState;

public class GameStateChangeEvent extends GameEvent {
    private GameState _newGameState;
    private GameState _oldGameState;

    public GameStateChangeEvent(GameState oldGameState, GameState newGameState) {
        this._oldGameState = oldGameState;
        this._newGameState = newGameState;
    }

    public GameState getNewGameState() {
        return _newGameState;
    }

    public void setNewGameState(GameState newGameState) {
        this._newGameState = newGameState;
    }

    public GameState getOldGameState() {
        return _oldGameState;
    }
}