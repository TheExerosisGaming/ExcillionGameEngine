package me.exerosis.game.engine.util;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class VectorUtil {
    public static void knockback(Entity entity, Location from, double power) {
        Location entityLocation = entity.getLocation();
        double x = entityLocation.getX() - from.getX();
        double y = entityLocation.getY() - from.getY();
        double z = entityLocation.getZ() - from.getZ();
        Vector knockVector = new Vector(x, y, z);
        knockVector.normalize();
        knockVector.multiply(power);
        entity.setVelocity(knockVector);
    }
}
