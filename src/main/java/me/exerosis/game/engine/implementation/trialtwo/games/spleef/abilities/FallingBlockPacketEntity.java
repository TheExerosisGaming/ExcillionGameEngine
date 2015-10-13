package me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities;

import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.entities.Entity;
import me.exerosis.packet.utils.packet.PacketPlay;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;

public class FallingBlockPacketEntity extends Entity {
    private static final long serialVersionUID = 3687581431204350636L;
    private EntityFallingBlock _block;
    private Material _material;
    private byte _data;
    private EntityArmorStand _stand;

    @SuppressWarnings("deprecation")
    public FallingBlockPacketEntity(Block block) {
        this(block.getLocation(), block.getType(), block.getData());
    }

    public FallingBlockPacketEntity(Location location, Material material) {
        this(location, material, (byte) 0);
    }

    public FallingBlockPacketEntity(Location location, Material material, byte data) {
        super("", location);
        _material = material;
        _data = data;
        createBlock();
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            sendModCommand(getSpawnCommand(player));
    }

    private void createBlock() {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double z = getLocation().getZ();
        WorldServer world = ((CraftWorld) getLocation().getWorld()).getHandle();

        _block = new EntityFallingBlock(world);
        _block.setLocation(x + 0.5, y - 1.49, z + 0.5, 0, 0);

        _stand = new EntityArmorStand(world);
        _stand.setLocation(x + 0.5, y - 1.49, z + 0.5, 0, 0);
        _stand.setInvisible(true);
        _stand.setGravity(false);
    }

    @SuppressWarnings("deprecation")
    public Packet getSpawnBlockPacket() {
        return new PacketPlayOutSpawnEntity(_block, 70, _material.getId() + (_data << 12));
    }

    public Packet getSpawnStandPacket() {
        return new PacketPlayOutSpawnEntityLiving(_stand);
    }

    public Packet getAttachPacket() {
        return new PacketPlayOutAttachEntity(0, _block, _stand);
    }

    public Object getTeleportPacket(Location location) {
        return PacketPlay.Out.EntityTeleport(getEID()[1], location, false);
    }

    @Override
    public String getSpawnCommand(PacketPlayer player) {
        return "SpawnBlock, SpawnStand, Attach";
    }

    @Override
    public int[] getEID() {
        return new int[]{_block.getId(), _stand.getId()};
    }
}
