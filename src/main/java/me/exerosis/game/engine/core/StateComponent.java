package me.exerosis.game.engine.core;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;

import java.util.Arrays;
import java.util.List;

public class StateComponent extends GameComponent {
    private final List<GameState> _gameStates;

    public StateComponent(Game game, GameLocation location) {
        this(game, location.getStates());
    }

    public StateComponent(Game game, GameState... states) {
        super(game);
        _gameStates = Arrays.asList(states);
        registerListener();
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (_gameStates.contains(event.getNewGameState())) {
            if (!_gameStates.contains(event.getOldGameState()))
                onEnable();
        }
        else
            onDisable();
    }
}