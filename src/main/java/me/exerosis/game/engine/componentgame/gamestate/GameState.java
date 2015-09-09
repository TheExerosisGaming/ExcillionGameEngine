package me.exerosis.game.engine.componentgame.gamestate;

public enum GameState {
    STARTING,

    LOBBY,

    PRE_GAME,

    IN_GAME,

    POST_GAME,

    RESTARTING;

    private static GameState[] gamestates = values();

    public boolean equals(GameState... states) {
        for (GameState state : states)
            if (equals(state))
                return true;
        return false;
    }

    public GameState getLast() {
        int index = (this.ordinal() - 1);
        return gamestates[index < 0 ? gamestates.length - 1 : index];
    }

    public GameState getNext() {
        int index = (this.ordinal() + 1);
        return gamestates[index >= gamestates.length ? 0 : index];
    }
}