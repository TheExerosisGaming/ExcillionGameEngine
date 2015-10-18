package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class LMSComponent extends GameComponent {
    private final SpectateComponent _spectateComponent;
    private final int _endPlayers;

    public LMSComponent(Game game, SpectateComponent spectateComponent) {
        super(game);
        _spectateComponent = spectateComponent;
        _endPlayers = getGame().getGameConfigValue("endPlayers", Integer.class);
    }

    @EventListener
    public void onDeath(PlayerKilledEvent event) {
        if (_endPlayers >= _spectateComponent.getNumberPlayers())
            setGameState(GameState.POST_GAME);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (getPlayers().size() - 1 <= _endPlayers)
            setGameState(GameState.POST_GAME);
        if (getPlayers().size() - 1 == 0)
            setGameState(GameState.RESTARTING);
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    public int getEndPlayers() {
        return _endPlayers;
    }
}