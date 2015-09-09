package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerComponent;
import me.exerosis.game.engine.componentgame.component.core.world.WorldComponent;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import me.exerosis.packet.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SpawnpointComponent extends Component {
    List<Location> _locations = new ArrayList<Location>();
    int index = 0;
    @Depend
    private WorldComponent _worldComponent;
    @Depend
    private PlayerComponent _playerComponent;
    private Vector _defualtSpawn;

    public SpawnpointComponent(Vector defualtSpawn) {
        _defualtSpawn = defualtSpawn;
    }

    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (!event.getGameState().equals(GameState.PRE_GAME))
            return;
        for (Player player : Arena.getPlayers()) {
            player.teleport(getNextSpawn());
            _playerComponent.clearPlayer(player);
        }
    }

    public Location getNextSpawn() {
        if (index >= _locations.size() - 1)
            index = 0;
        if (_defualtSpawn != null)
            return _defualtSpawn.toLocation(_worldComponent.getGameWorld());
        return _locations.get(index++);
    }

    public Location getSpectateLocation(Player player) {
        String specSpawn = _worldComponent.getConfig("mapData.yml").getString("specSpawn");
        if (specSpawn != null)
            return LocationUtils.fromString(specSpawn, _worldComponent.getGameWorld());
        else
            return player.getLocation().add(0, 4, 0);
    }

    private void addSpawnPoints() {
        List<String> mapDataValue = _worldComponent.getConfig("mapData.yml").getStringList("spawnPoints");

        for (String stringLocation : mapDataValue) {
            Location location = LocationUtils.fromString(stringLocation, _worldComponent.getGameWorld());
            _locations.add(location);
        }
    }

    @Override
    public void onEnable() {
        registerListener(this);
        if (_defualtSpawn == null)
            addSpawnPoints();
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        _locations.clear();
    }
}