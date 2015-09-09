package me.exerosis.game.engine.componentgame.event.game.pause;

import me.exerosis.game.engine.componentgame.event.game.GameEvent;

public class PauseStateChangeEvent extends GameEvent {
    private PauseState _pauseState;

    public PauseStateChangeEvent(PauseState pauseState) {
        _pauseState = pauseState;
    }

    public PauseState getPauseState() {
        return _pauseState;
    }
}