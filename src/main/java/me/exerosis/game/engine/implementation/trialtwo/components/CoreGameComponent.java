package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.component.event.EventListener;
import me.exerosis.component.event.Priority;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerSpectateEvent;

public class CoreGameComponent extends GameComponent {
    private final SpectateComponent _spectateComponent;
    private final SpawnpointComponent _spawnpointComponent;

    public CoreGameComponent(Game game, SpectateComponent spectateComponent, SpawnpointComponent spawnpointComponent) {
        super(game);
        _spectateComponent = spectateComponent;
        _spawnpointComponent = spawnpointComponent;
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

 /*   @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent event) {
        _spectateComponent.setSpectating(event.getPlayer());
    }*/

    @EventListener
    public void onSpectate(PlayerSpectateEvent event) {
        int index = _spawnpointComponent.getIndex();
        _spawnpointComponent.setIndex(0);
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        _spawnpointComponent.setIndex(index);
    }

    @EventListener(priority = Priority.HIGHEST)
    public void onDeath(PlayerKilledEvent event) {
        _spectateComponent.setSpectating(event.getPlayer());
    }
}