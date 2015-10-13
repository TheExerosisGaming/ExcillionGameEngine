package me.exerosis.game.engine.implementation.trialtwo.games.spleef.doublejump;

import me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.EntityLastingEffect;
import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DoubleJump implements Runnable {
    private Player _player;
    private double _forwardForce;
    private double _height;
    private double _distance;

    public DoubleJump(Player player, double forwardForce, double height, double distance) {
        _forwardForce = forwardForce;
        _height = height;
        _distance = distance;
        _player = player;
        launch();
        //new LandEffect(5, player.getLocation().add(0, 1, 0));
    }

    private void launch() {
        new EntityLastingEffect(_player, 3, 3, new Vector(), 0, 1) {
            public void mod() {
                if (getEffectTime() == 0) {
                    getEntity().setVelocity(new Vector());
                    _player.playSound(_player.getLocation(), Sound.WITHER_SHOOT, 10, 1);
                    Location location = _player.getEyeLocation();
                    location.setPitch(0);
                    getEntity().setVelocity(location.getDirection().multiply(_distance).setY(0.4));
                    ExTask.startTask(this, 1, 1);

                    new EntityLastingEffect(_player, 13, 26, new Vector(), 0, 3);
                    new EntityLastingEffect(_player, 13, 0, new Vector(), 0, 2);
                    new EntityLastingEffect(_player, 13, 12, new Vector(), 0, 2);
                }
            }
        };

        _player.playSound(_player.getLocation(), Sound.ENDERDRAGON_WINGS, 10, 1);
        Vector vector = _player.getLocation().getDirection().multiply(_forwardForce).setY(_height);
        _player.setVelocity(vector);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        if (_player.isOnGround())
            ExTask.stopTask(this);
    }
}