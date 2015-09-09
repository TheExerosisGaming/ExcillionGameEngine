package me.exerosis.game.engine.componentgame.game.lms.corpse;

import net.minecraft.server.v1_8_R1.EntityArmorStand;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

public class CustomArmorStand extends EntityArmorStand {
    public CustomArmorStand(World world) {
        super(((CraftWorld) world).getHandle());
        a(0, 0);
    }

    @Override
    protected void a(float f, float f1) {
        super.a(f, f1);
    }

    @Override
    public void move(double d0, double d1, double d2) {
        super.move(d0, d1, d2);
    }

    @Override
    protected void checkBlockCollisions() {

    }
}
