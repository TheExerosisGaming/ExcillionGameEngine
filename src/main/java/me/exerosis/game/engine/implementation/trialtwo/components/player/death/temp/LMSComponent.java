package me.exerosis.game.engine.implementation.trialtwo.components.player.death.temp;

import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreGameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.WinnersComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerSpectateEvent;
import org.bukkit.event.EventHandler;

public class LMSComponent extends Component {
    private SpectateComponent _spectateComponent;
    private LivesComponent _livesComponent;
    private CoreGameComponent _coreComponent;
    private WinnersComponent _winnersComponent;

    public LMSComponent() {
    }

    @EventHandler
    public void onDeath(PlayerSpectateEvent event) {
        if (_livesComponent.getLives(event.getPlayer()) <= 0)
            if (!getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING))
                if (_coreComponent.getEndPlayers() >= _spectateComponent.getNumberPlayers())
                    getArena().setGameState(GameState.POST_GAME);
    }

    @EventHandler
    public void onLeave(PlayerLeaveEvent event) {
        if (!getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING))
            if (_coreComponent.getEndPlayers() >= _spectateComponent.getNumberPlayers())
                getArena().setGameState(GameState.POST_GAME);
        if (_spectateComponent.getNumberPlayers() == 0)
            getArena().setGameState(GameState.RESTARTING);
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}