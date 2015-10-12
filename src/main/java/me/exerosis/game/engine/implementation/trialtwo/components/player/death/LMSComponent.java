package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreGameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerSpectateEvent;

public class LMSComponent extends GameComponent {
    private final SpectateComponent _spectateComponent;
    private final CoreGameComponent _coreComponent;
    private final SpawnpointComponent _spawnpointComponent;

    public LMSComponent(Game game, SpectateComponent spectateComponent, CoreGameComponent coreComponent, SpawnpointComponent spawnpointComponent) {
        super(game);
        _spectateComponent = spectateComponent;
        _coreComponent = coreComponent;
        _spawnpointComponent = spawnpointComponent;
    }


    @EventListener
    public void onSpectate(PlayerSpectateEvent event) {
        int index = _spawnpointComponent.getIndex();
        _spawnpointComponent.setIndex(0);
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        _spawnpointComponent.setIndex(index);
    }

    @EventListener
    public void onDeath(PlayerKilledEvent event) {
        if (_coreComponent.getEndPlayers() >= _spectateComponent.getNumberPlayers())
            setGameState(GameState.POST_GAME);
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }
}