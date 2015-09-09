package me.exerosis.game.engine.util;

import me.exerosis.reflection.Reflect;
import me.exerosis.reflection.ReflectClass;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

public class EntityUtil {
    private EntityUtil() {
    }

    public static void damage(LivingEntity damaged, LivingEntity damager, double amount) {
        ReflectClass<Object> entity = Reflect.Class("{nms}.EntityLiving");
        entity.setInstance(damaged);
        entity.getField(Object.class, "killer").setValue(damager);
        entity.getField(Object.class, "lastDamager").setValue(damager);
        entity.getField(float.class, "lastDamage").setValue((float) amount);
        damaged.damage(amount);
    }

    public static void kill(LivingEntity killed, HumanEntity killer) {
        ReflectClass<Object> entity = Reflect.Class("{nms}.EntityLiving");
        entity.setInstance(killed);
        entity.getField(Object.class, "killer").setValue(killer);
        entity.getField(Object.class, "lastDamager").setValue(killer);
        entity.getField(float.class, "lastDamage").setValue(20F);
        killed.remove();
    }
}
