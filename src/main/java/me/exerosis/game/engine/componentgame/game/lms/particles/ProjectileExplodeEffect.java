package me.exerosis.game.engine.componentgame.game.lms.particles;

import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.PlayParticle;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

public class ProjectileExplodeEffect extends AnimatedEffect {
    public ProjectileExplodeEffect(int size, Location location) {
        addEffect(new Effect(1) {
            public void effect() {
                for (int a = 0; a < size; a++)
                    PlayerHandler.sendGlobalPacket(new PlayParticle(1, location, new Vector(), 0, 10));
                for (int b = 0; b < size; b++)
                    PlayerHandler.sendGlobalPacket(new PlayParticle(0, location, new Vector(), 0, 10));
                for (int c = 0; c < size; c++)
                    PlayerHandler.sendGlobalPacket(new PlayParticle(27, location, new Vector(), 0, 10));
            }
        });
        addEffect(new Effect(2) {
            public void effect() {
                for (int a = 0; a < size; a++)
                    PlayerHandler.sendGlobalPacket(new PlayParticle(11, location, new Vector(), 0, 10));
                for (int b = 0; b < size; b++)
                    PlayerHandler.sendGlobalPacket(new PlayParticle(12, location, new Vector(), 0, 10));
                location.getWorld().playSound(location, Sound.EXPLODE, 2F, 0.4F);
            }
        });
    }
}
