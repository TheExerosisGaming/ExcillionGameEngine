package me.exerosis.game.engine.implementation.trialtwo.games.runner;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AntiCheat extends GameComponent {
    private final BlockDropComponent _blockDropComponent;
    private Map<Player, Location> _locations = new HashMap<>();
    private SpectateComponent _spectateComponent;

    public AntiCheat(Game game, SpectateComponent spectateComponent, BlockDropComponent blockDropComponent) {
        super(game);
        _spectateComponent = spectateComponent;
        _blockDropComponent = blockDropComponent;
    }

    @Override
    public void run() {
        for (Player player : _spectateComponent.getGamePlayers()) {
            Location oldLocation = _locations.get(player);
            if (oldLocation != null)
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

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        for (Player player : _spectateComponent.getGamePlayers())
            _locations.put(player, player.getLocation());
    }

    @Override
    public void onEnable() {
        registerListener();
        startTask(80, 80);
        super.onEnable();
    }
}