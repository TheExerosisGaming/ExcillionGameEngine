package me.exerosis.game.engine.implementation.old.event.game.pause;

public enum PauseState {
    PAUSED, RESUMING, RESUMED;

    private static PauseState[] pauseStates = values();

    public boolean equals(PauseState... states) {
        for (PauseState state : states)
            if (equals(state))
                return true;
        return false;
    }

    public PauseState getLast() {
        int index = (this.ordinal() - 1);
        return pauseStates[index < 0 ? pauseStates.length - 1 : index];
    }

    public PauseState getNext() {
        int index = (this.ordinal() + 1);
        return pauseStates[index >= pauseStates.length ? 0 : index];
    }
}
