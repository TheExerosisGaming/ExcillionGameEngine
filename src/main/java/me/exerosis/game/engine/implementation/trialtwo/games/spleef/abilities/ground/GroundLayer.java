package me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.ground;

import me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.FallingBlockPacketEntity;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class GroundLayer {
    private List<FallingBlockPacketEntity> _blocks = new ArrayList<>();
    private Ground _ground;
    private int _level;

    public GroundLayer(Ground ground, int level, Set<Location> set) {
        _blocks.addAll(set.stream().map(l -> new FallingBlockPacketEntity(l, Material.STONE)).collect(Collectors.toList()));
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

    public List<FallingBlockPacketEntity> getBlocks() {
        return _blocks;
    }

    public int getLevel() {
        return _level;
    }
}
