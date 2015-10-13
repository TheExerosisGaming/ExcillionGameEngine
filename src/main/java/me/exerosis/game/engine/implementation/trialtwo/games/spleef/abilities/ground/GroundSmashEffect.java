package me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.ground;

import me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.FallingBlockPacketEntity;
import me.exerosis.packet.utils.location.LocationUtils;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class GroundSmashEffect implements TickListener {
    private ArrayList<Block> _blocks = new ArrayList<>();
    private ArrayList<FallingBlockPacketEntity> _fakeBlocks = new ArrayList<>();
    private Location _location;

    public GroundSmashEffect(Location location) {
        _location = location;
        gatherBlocks();
        breakBlocks();
    }

    private void gatherBlocks() {
        for (Location location : LocationUtils.circle(_location, 3, 3, false, true, 0)) {
            Block block = location.getBlock();
            if (block.getType().equals(Material.AIR))
                continue;
            if (!block.getType().isSolid() || block.getType().isTransparent()) {
                breakWithEffect(block);
                continue;
            }

            _blocks.add(block);
        }
    }

    private void breakBlocks() {
        for (Block block : _blocks) {
            block.setType(Material.BARRIER);
            _fakeBlocks.add(new FallingBlockPacketEntity(block.getLocation(), Material.STATIONARY_WATER));
        }
        Ticker.registerListener(this);
    }

    @SuppressWarnings({"deprecation", "static-method"})
    private void breakWithEffect(Block block) {
        block.setType(Material.AIR);
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
    }

    @Override
    public void tick() {
        for (FallingBlockPacketEntity block : _fakeBlocks) {
            Location location = block.getLocation().clone().add(0, -1 + Math.random() * 2, 0);
            block.sendModCommand("Teleport", location);
        }
    }
}