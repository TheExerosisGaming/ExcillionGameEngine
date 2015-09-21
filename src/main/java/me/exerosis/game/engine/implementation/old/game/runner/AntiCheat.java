package me.exerosis.game.engine.implementation.old.game.runner;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class AntiCheat extends Component {
    private Map<Player, Location> _locations = new HashMap<Player, Location>();
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private BlockDropComponent _blockDropComponent;

    public AntiCheat() {
    }

    @Override
    public void run() {
        for (Player player : _spectateComponent.getGamePlayers()) {
            Location oldLocation = _locations.get(player);
            if (player.getLocation().distance(oldLocation) < 1) {
                Location newLocation = player.getLocation().add(0, -1, 0);
                addBlock(newLocation, 0, 1);
                addBlock(newLocation, 1, 1);
                addBlock(newLocation, -1, 1);
                addBlock(newLocation, -1, 0);
                addBlock(newLocation, 1, 0);
                addBlock(newLocation, 0, -1);
                addBlock(newLocation, 1, -1);
                addBlock(newLocation, -1, -1);
            }
        }
    }

    private void addBlock(Location location, int x, int z) {
        Block block = location.clone().add(x, 0, z).getBlock();
        if (block.getType().isSolid())
            _blockDropComponent.addBlock(block);
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.IN_GAME)) {
            startTask(80, 80);
            for (Player player : _spectateComponent.getGamePlayers())
                _locations.put(player, player.getLocation());
        } else
            stopTask();
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
