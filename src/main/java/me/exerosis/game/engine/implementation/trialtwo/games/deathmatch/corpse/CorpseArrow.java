package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.corpse;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Created by The Exerosis on 8/12/2015.
 */
public class CorpseArrow {
    private final Entity entity;

    public CorpseArrow(Location location) {
        entity = location.getWorld().spawnEntity(location, EntityType.ARROW);
    }



    public int getID() {
        return entity.getEntityId();
    }
}