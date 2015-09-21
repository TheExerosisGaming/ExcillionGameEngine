package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.reflection.event.EventListener;
import org.bukkit.plugin.Plugin;

public abstract class GameStateCountdown extends Countdownasdf {

    public GameStateCountdown(Plugin plugin, Game game) {
        super(plugin, game);
    }

    public abstract GameState getNextGameState();

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        super.onDisable();
    }

    @EventListener(postEvent = true)
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(getNextGameState()))
            stopTask();
    }

    @Override
    public void done() {
        setGameState(getNextGameState());
    }
}