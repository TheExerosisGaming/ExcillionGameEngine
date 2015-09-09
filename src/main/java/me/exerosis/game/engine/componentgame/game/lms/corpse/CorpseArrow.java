package me.exerosis.game.engine.componentgame.game.lms.corpse;

import me.exerosis.packet.player.injection.events.PacketSendEvent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;

/**
 * Created by The Exerosis on 8/12/2015.
 */
public class CorpseArrow {
    private final Entity entity;

    public CorpseArrow(Location location) {
        entity = location.getWorld().spawnEntity(location, EntityType.ARROW);
    }

    @EventHandler
    public void onPacketSendEvent(PacketSendEvent event) {
        PacketPlayOutRelEntityMoveLook
        Object packet = event.getPacket();
    }


    public int getID() {
        return entity.getEntityId();
    }
}