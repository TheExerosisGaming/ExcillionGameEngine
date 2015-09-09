package me.exerosis.game.engine.componentgame.countdown;

import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.event.EventHandler;

public abstract class GameStateCountdown extends Countdown {

    public GameStateCountdown() {
    }

    public abstract GameState getNextGameState();

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        super.onDisable();
    }

    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(getNextGameState()))
            stopTask();
    }

    @Override
    public void done() {
        if (getNextGameState() != null)
            getArena().setGameState(getNextGameState());
    }
}