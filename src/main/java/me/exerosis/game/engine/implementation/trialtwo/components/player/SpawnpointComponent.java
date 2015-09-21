package me.exerosis.game.engine.implementation.trialtwo.components.player;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.packet.utils.location.LocationUtils;
import me.exerosis.reflection.event.EventListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//TODO add spawnpoint factory
public class SpawnpointComponent extends GameComponent {
    List<Vector> _spawns = new ArrayList<>();
    int index = 0;
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
        return _spawns.get(index++);
    }

    private List<Vector> getConfigSpawns() {
        Collection<String> spawnPoints = _worldComponent.getConfig("mapData.yml").getStringList("spawnPoints");
        return spawnPoints.stream().map(LocationUtils::vectorFromString).collect(Collectors.toList());
    }

    public Location getLobbySpawn() {
        return _worldComponent.getLobbyWorld().getSpawnLocation().add(0, 4, 0);
    }

    public void sendToSpawn(Player player) {
        player.teleport(getGameState().equals(GameState.LOBBY, GameState.RESTARTING) ? getLobbySpawn() : getNextSpawn().toLocation(player.getWorld()));
    }

    @Override
    public void onEnable() {
        registerListener();
        _spawns = getConfigSpawns();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        super.onDisable();
    }
}