package me.exerosis.game.engine.implementation.trialtwo.components.player;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.packet.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//TODO add spawnpoint factory
public class SpawnpointComponent extends GameComponent {
    private List<Vector> _spawns = new ArrayList<>();
    private int index = 0;
    private WorldComponent _worldComponent;

    public SpawnpointComponent(Game game, WorldComponent worldComponent) {
        super(game);
        _worldComponent = worldComponent;
    }

    @EventListener(postEvent = true)
    public void onGameStateChange(GameStateChangeEvent event) {
        if (!event.getNewGameState().equals(GameState.PRE_GAME))
            return;
        index = 0;
        getPlayers().forEach(this::sendToSpawn);
    }

    public Vector getNextSpawn() {
        if (index >= _spawns.size() - 1)
            index = 0;
        return _spawns.size() > 0 ? _spawns.get(index++) : null;
    }

    private List<Vector> getConfigSpawns() {
        Collection<String> spawnPoints = _worldComponent.getConfig("mapData.yml").getStringList("spawnPoints");
        return spawnPoints.stream().map(LocationUtils::vectorFromString).collect(Collectors.toList());
    }

    public Location getLobbySpawn() {
        return _worldComponent.getLobbyWorld().getSpawnLocation().add(0, 4, 0);
    }

    public void sendToSpawn(Player player) {
        Vector nextSpawn = getNextSpawn();
        World gameWorld = _worldComponent.getGameWorld();
        player.teleport(getGameState().equals(GameLocation.LOBBY_WORLD.getStates()) ? getLobbySpawn() : nextSpawn == null ? gameWorld.getSpawnLocation() : nextSpawn.toLocation(gameWorld));
    }

    @Override
    public void onEnable() {
        registerListener();
        _spawns = getConfigSpawns();
        super.onEnable();
    }
}