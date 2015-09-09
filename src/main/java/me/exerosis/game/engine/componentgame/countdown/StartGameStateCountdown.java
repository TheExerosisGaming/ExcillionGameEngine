package me.exerosis.game.engine.componentgame.countdown;

import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.event.EventHandler;

public abstract class StartGameStateCountdown extends GameStateCountdown {

    public StartGameStateCountdown() {
    }

    public abstract GameState getStartGameState();

    @Override
    public void onEnable() {
        if (getArena().getGameState().equals(getStartGameState()))
            start();
        super.onEnable();
    }

    //Listeners
    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(getStartGameState()))
            start();
    }
}