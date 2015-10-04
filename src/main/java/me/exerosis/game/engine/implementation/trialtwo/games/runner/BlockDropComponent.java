package me.exerosis.game.engine.implementation.trialtwo.games.runner;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.StateComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public class BlockDropComponent extends StateComponent implements Runnable {
    public static final MaterialData[] CHANGE = new MaterialData[]{
            new MaterialData(Material.STAINED_CLAY, (byte) 4),
            new MaterialData(Material.STAINED_CLAY, (byte) 1),
            new MaterialData(Material.STAINED_CLAY, (byte) 14)
    };
    private SpectateComponent _spectateComponent;
    private Map<Block, Byte> _blocks = new HashMap<>();
    private long _speed;

    public BlockDropComponent(Game game, SpectateComponent spectateComponent, long speed) {
        super(game, GameState.IN_GAME);
        _spectateComponent = spectateComponent;
        _speed = speed;
    }

    @Override
    public void onEnable() {
        registerListener();
        startTask(_speed, _speed);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopTask();
        super.onDisable();
    }

    @EventHandler
    public void onLand(EntityChangeBlockEvent event) {
        if (event.getEntityType().equals(EntityType.FALLING_BLOCK))
            event.setCancelled(true);
    }

    @Override
    public void run() {
        addBlocks();

        Set<Block> remove = new HashSet<>();
        for (Entry<Block, Byte> entry : _blocks.entrySet()) {
            Byte b = entry.getValue();
            Block block = entry.getKey();
            b++;

            b = (byte) Math.max(b, (byte) 0);

            if (b >= CHANGE.length) {
                block.getWorld().spawnFallingBlock(block.getLocation().add(0, -0.5, 0), block.getType(), block.getData()).setDropItem(false);
                block.setType(Material.AIR);
                remove.add(block);
                continue;
            }
            MaterialData md = CHANGE[b];
            block.setType(md.getItemType());
            block.setData(md.getData());
            entry.setValue(b);
        }
        remove.forEach(_blocks::remove);
    }

    private void addBlocks() {
        for (Player player : _spectateComponent.getGamePlayers()) {
            Location playerLocation = player.getLocation();
            //Temp safeguard cause I keep recking my lobby hahah ;)
            if (playerLocation.getWorld().getName().equals("lobby"))
                return;
            double dist = 0.32;

            World world = playerLocation.getWorld();
            double x = playerLocation.getX();
            double y = playerLocation.getY() - 1;
            double z = playerLocation.getZ();

            Set<Block> blocks = Stream.of(
                    new Location(world, x + dist, y, z + dist).getBlock(),
                    new Location(world, x - dist, y, z + dist).getBlock(),
                    new Location(world, x - dist, y, z - dist).getBlock(),
                    new Location(world, x + dist, y, z - dist).getBlock())
                    .collect(Collectors.toSet());

            for (Block block : blocks) {
                if (block == null || block.getRelative(BlockFace.UP).getType().isSolid() || !block.getType().isSolid())
                    return;
                _blocks.putIfAbsent(block, (byte) -1);
            }
        }
    }

    public void addBlock(Block block) {
        _blocks.putIfAbsent(block, (byte) -1);
    }
}