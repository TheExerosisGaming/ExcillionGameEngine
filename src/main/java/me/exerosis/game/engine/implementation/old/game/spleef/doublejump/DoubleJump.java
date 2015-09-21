package me.exerosis.game.engine.implementation.old.game.spleef.doublejump;

import me.exerosis.game.engine.implementation.old.game.spleef.abilities.EntityLastingEffect;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class DoubleJump implements TickListener {

    private Player _player;
    private double _forwardMult;
    private double _hight;
    private double _distance;

    public DoubleJump(Player player, double forwardMult, double hight, double distance) {
        _forwardMult = forwardMult;
        _hight = hight;
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
                    Ticker.registerListener(this);

                    new EntityLastingEffect(_player, 13, 26, new Vector(), 0, 3);
                    new EntityLastingEffect(_player, 13, 0, new Vector(), 0, 2);
                    new EntityLastingEffect(_player, 13, 12, new Vector(), 0, 2);
                }
            }
        };

        _player.playSound(_player.getLocation(), Sound.ENDERDRAGON_WINGS, 10, 1);
        Vector vector = _player.getLocation().getDirection().multiply(_forwardMult).setY(_hight);
        _player.setVelocity(vector);
    }


    @SuppressWarnings("deprecation")
    @Override
    public void tick() {
        if (!_player.isOnGround())
            return;
        Ticker.unregisterListener(this);
    }
}
