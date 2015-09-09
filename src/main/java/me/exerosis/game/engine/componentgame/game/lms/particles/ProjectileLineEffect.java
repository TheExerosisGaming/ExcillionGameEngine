package me.exerosis.game.engine.componentgame.game.lms.particles;

import me.exerosis.game.engine.util.PointMatrix;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.PlayParticle;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.List;

public class ProjectileLineEffect implements TickListener {
    double tillNextParticle = 0;
    int total = 0;
    private Location _start;
    private List<Vector> _points;
    private double _particlesPerTick;
    private int _particleID;

    public ProjectileLineEffect(Location start, Location end, int numberOfParticles, int particleID, double time) {
        start.getWorld().playSound(start, Sound.FIREWORK_LAUNCH, 1, 0.5F);
        _particleID = particleID;
        _start = start;
        _particlesPerTick = _start.distance(end) / time;
        _points = new PointMatrix(numberOfParticles, _start.toVector(), end.add(0, 0.5, 0).toVector()).getPoints();
        Ticker.registerListener(this);
        for (int x = 0; x < 10; x++)
            PlayerHandler.sendGlobalPacket(new PlayParticle(_particleID, _start, end.toVector().normalize(), (float) _particlesPerTick, 0));
    }

    @Override
    public void tick() {
        tillNextParticle += _particlesPerTick;
        for (int x = 0; x < Math.floor(tillNextParticle); x++) {
            if (total + 2 == _points.size()) {
                Location location = _points.get(x).toLocation(_start.getWorld());
                new ProjectileExplodeEffect(3, location);
            } else if (total >= _points.size()) {
                Ticker.unregisterListener(this);
                return;
            }
            playParticle(total);
            total++;
            tillNextParticle--;
        }
    }

    private void playParticle(int index) {
        Location location = _points.get(index).toLocation(_start.getWorld());
        PlayerHandler.sendGlobalPacket(new PlayParticle(_particleID, location, new Vector(), 0, 10));
    }
}