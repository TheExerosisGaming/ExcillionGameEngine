package me.exerosis.game.engine.core.state;

public enum GameLocation {
    GAME_WORLD(GameState.PRE_GAME, GameState.IN_GAME, GameState.POST_GAME), LOBBY_WORLD(GameState.LOBBY, GameState.RESTARTING);

    private GameState[] _states;

    GameLocation(GameState... states) {
        _states = states;
    }

    public GameState[] getStates() {
        return _states;
    }
}