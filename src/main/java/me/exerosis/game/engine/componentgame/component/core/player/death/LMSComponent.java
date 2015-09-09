package me.exerosis.game.engine.componentgame.component.core.player.death;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.CoreGameComponent;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.event.PlayerLeaveEvent;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerSpectateEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.event.EventHandler;

public class LMSComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private LivesComponent _livesComponent;
    @Depend
    private CoreGameComponent _coreComponent;
    @Depend
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