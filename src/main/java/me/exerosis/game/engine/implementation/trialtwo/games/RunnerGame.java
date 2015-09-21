package me.exerosis.game.engine.implementation.trialtwo.games;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreGameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.PlayerComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;
import org.bukkit.GameMode;
import org.bukkit.plugin.Plugin;

public class RunnerGame extends Game {
    public RunnerGame(Plugin plugin, Arena arena) {
        super(plugin, arena, "Runner");

        CoreGameComponent coreGameComponent = new CoreGameComponent(this, arena.getManager());
        addInstance(coreGameComponent);
        WorldComponent worldComponent = new WorldComponent(this, arena);
        addInstance(worldComponent);
        SpawnpointComponent spawnpointComponent = new SpawnpointComponent(this, worldComponent);
        addInstance(spawnpointComponent);
        PlayerComponent playerComponent = new PlayerComponent(this, spawnpointComponent, coreGameComponent, GameMode.ADVENTURE);
        addInstance(playerComponent);
        SpectateComponent spectateComponent = new SpectateComponent(this, playerComponent);
        addInstance(spectateComponent);


    }
}
