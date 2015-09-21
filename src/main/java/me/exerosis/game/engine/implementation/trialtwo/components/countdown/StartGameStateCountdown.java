package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.reflection.event.EventListener;
import org.bukkit.plugin.Plugin;

public abstract class StartGameStateCountdown extends GameStateCountdown {

    public StartGameStateCountdown(Plugin plugin, Game game) {
        super(plugin, game);
    }

    public abstract GameState getStartGameState();

    @Override
    public void onEnable() {
        if (getGameState().equals(getStartGameState()))
            start();
        super.onEnable();
    }

    //Listeners
    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(getStartGameState()))
            start();
    }
}