package me.exerosis.game.engine;

import me.exerosis.game.engine.util.particles.PointMatrix;
import me.exerosis.packet.injection.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.PlayParticle;
import me.exerosis.packet.utils.math.Circle;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SonarEffect implements TickListener {
    double angle = 0;
    int y = 0;
    private Location _start;
    private Circle _circle;

    public SonarEffect(Location start) {
        _start = start;
        _circle = new Circle.Builder(start.toVector()).radius(3).build();
        Ticker.registerListener(this);
    }

    @Override
    public void tick() {
        y++;

        angle += 1.2;
        if (angle == 361) {
            angle = 0;
            return;
        }
        if (y % 25 == 0)
            for (int x = 0; x < 361; x += 6)
                for (Vector point1 : new PointMatrix(3, _start.toVector(), _circle.getPointInCircle(x, false)))
                    PlayerHandler.sendGlobalPacket(new PlayParticle(18, point1.toLocation(_start.getWorld()), new Vector(), 0, 10));
        if (y % 2 == 0)
            for (Vector point1 : new PointMatrix(17, _start.toVector(), _circle.getPointInCircle(angle, false)))
                PlayerHandler.sendGlobalPacket(new PlayParticle(19, point1.toLocation(_start.getWorld()).add(0, 0.1, 0), new Vector(), 0, 10));
    }
}
