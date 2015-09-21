package me.exerosis.game.engine.implementation.old.game.spleef.abilities.ground;

import me.exerosis.packet.utils.location.LocationUtils;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Ground implements TickListener {
    int x = 0;
    int y = 0;
    int z = 0;
    private ArrayList<GroundLayer> _layers = new ArrayList<GroundLayer>();

    public Ground(Location midpoint, int radius) {
        List<Set<Location>> layers = LocationUtils.layeredCircle(midpoint, radius);
        for (int x = 0; x < layers.size(); x++)
            _layers.add(new GroundLayer(this, x, layers.get(x)));
        Ticker.registerListener(this);
    }

    public GroundLayer getLayer(int layer) {
        if (_layers.size() > layer && layer > 0)
            return _layers.get(layer);
        return null;
    }

    @Override
    public void tick() {
        x++;
        if (x % 4 != 0)
            return;
        GroundLayer layer = getLayer(y);
        if (layer != null)
            layer.lower(0.9);
        y++;
        if (y == z + 1) {
            y = 0;
            z++;
        }
        if (z == 3)
            Ticker.unregisterListener(this);
    }
}
