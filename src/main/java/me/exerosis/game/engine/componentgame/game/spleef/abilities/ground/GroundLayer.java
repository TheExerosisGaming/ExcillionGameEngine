package me.exerosis.game.engine.componentgame.game.spleef.abilities.ground;

import me.exerosis.game.engine.componentgame.game.spleef.abilities.FallingBlockPacketEntity;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Set;


public class GroundLayer {
    private ArrayList<FallingBlockPacketEntity> _blocks = new ArrayList<FallingBlockPacketEntity>();
    private Ground _ground;
    private int _level;

    public GroundLayer(Ground ground, int level, Set<Location> set) {
        for (Location location : set)
            _blocks.add(new FallingBlockPacketEntity(location, Material.STONE));
        _ground = ground;
        _level = level;
    }

    public void lower(double amount) {
        GroundLayer layerUp = _ground.getLayer(_level + 1);
        if (layerUp != null)
            layerUp.lower(amount / 2);

        for (FallingBlockPacketEntity block : _blocks) {
            block.sendModCommand("Teleport", block.getLocation().clone().add(0, -amount, 0));
        }
    }

    public ArrayList<FallingBlockPacketEntity> getBlocks() {
        return _blocks;
    }

    public int getLevel() {
        return _level;
    }
}
