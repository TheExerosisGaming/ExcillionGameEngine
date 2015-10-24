package me.exerosis.game.engine.util.particles;

import me.exerosis.game.engine.util.EntityUtil;
import me.exerosis.game.engine.util.VectorUtil;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.PlayParticle;
import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Shockwave implements Runnable {
    private double _radius;
    private double _distance;
    private Location _centerLocation;
    private Player _player;

    public Shockwave(double radius, Player player) {
        _radius = radius;
        _player = player;
        _centerLocation = player.getLocation();
        ExTask.startTask(this, 1, 1);
        _centerLocation.getWorld().playSound(_centerLocation, Sound.IRONGOLEM_THROW, 1.5F, 0.9F);
    }

    @Override
    public void run() {
        for (double degree = 0; degree < 360; degree += 5) {
            double x = _distance * Math.cos(Math.toRadians(degree)) + _centerLocation.getX();
            double z = _distance * Math.sin(Math.toRadians(degree)) + _centerLocation.getZ();
            Location location = new Location(_player.getWorld(), x, _centerLocation.getY(), z);
            for (Entity entity : _player.getWorld().getEntities())
                if (!entity.equals(_player))
                    if (entity.getLocation().distance(location) < 1.5) {
                        EntityUtil.damage((LivingEntity) entity, _player, 1);
                        VectorUtil.knockback(entity, _centerLocation, 2 / _distance + 0.01);
                    }

            PlayerHandler.sendGlobalPacket(new PlayParticle(27, location, new Vector(), (float) 0.01, 1));
        }
        _distance += 0.25;
        if (_distance == _radius)
            ExTask.startTask(this, 1, 1);
    }
}