package me.exerosis.game.engine.componentgame.game.lms.corpse;

import me.exerosis.packet.player.injection.events.in.PacketEventInUseEntity;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.EntityEquipment;
import me.exerosis.packet.player.injection.packets.entities.PacketEntityPlayer;
import me.exerosis.packet.utils.location.AdvancedLocation;
import me.exerosis.reflection.Reflect;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("NonSerializableFieldInSerializableClass")
public class CorpseOld extends PacketEntityPlayer {
    //ID
    private static final long serialVersionUID = -9208368020756023036L;
    private int _id = (int) (Math.random() * 10000);

    //Player
    private Player _deadPlayer;
    private PacketPlayer _deadPacketPlayer;

    //Packets
    private PacketPlayOutBed _bedPacket;
    private PacketPlayOutEntityTeleport _teleportPacket;

    private ItemStack[] _armorContents;

    public CorpseOld(Player deadPlayer) {
        super(deadPlayer.getName(), deadPlayer.getLocation(), deadPlayer.getUniqueId());
        _deadPlayer = deadPlayer;
        _armorContents = _deadPlayer.getInventory().getArmorContents().clone();
        _deadPacketPlayer = PlayerHandler.getPlayer(deadPlayer);
        createNPC();
        create();
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            sendModCommand(getSpawnCommand(player));


        StringBuilder builder = new StringBuilder();
        builder.append(deadPlayer.getName());
        builder.append("'s Loot");
    }

    public void create() {
        EntityPlayer craftPlayer = getCraftPlayer();
        Reflect.Field(craftPlayer, int.class, "id").setValue(_id);
        setCraftPlayer(craftPlayer);

        Location location = _deadPlayer.getLocation();

        int x = (int) (location.getX() * 32.0D);
        double y = (int) (location.getY() + 0.1);
        if (new AdvancedLocation(location).isSlab())
            y += 0.5;
        y *= 32.0D;
        int z = (int) (location.getZ() * 32.0D);

        _teleportPacket = new PacketPlayOutEntityTeleport(_id, x, (int) y, z, (byte) 0, (byte) 0, false);
        _bedPacket = new PacketPlayOutBed();

        Reflect.Field(_bedPacket, int.class, "a").setValue(_id);
        Reflect.Field(_bedPacket, BlockPosition.class, "b").setValue(new BlockPosition(location.getBlockX(), 1, location.getBlockZ()));
    }

    @Override
    public DataWatcher getDataWatcher() {
        DataWatcher watcher = emptyPlayerDataWatcher(_deadPlayer, _id);
        watcher.watch(10, _deadPacketPlayer.getCraftPlayer().getDataWatcher().getByte(10));
        watcher.watch(6, 20.0F);
        return watcher;
    }

    //Packets
    @SuppressWarnings("deprecation")
    @Override
    public Packet getSpawnPacket() {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers()) {
            Location location = _deadPlayer.getLocation();
            location.setY(1);
            player.getPlayer().sendBlockChange(location, Material.BED_BLOCK.getId(), getBedByte());
        }
        return new PacketPlayOutNamedEntitySpawn(getCraftPlayer());
    }

    public Packet getBedPacket() {
        return _bedPacket;
    }

    public Packet getTeleportPacket() {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            for (int x = 0; x < 4; x++)
                new EntityEquipment(getEID()[0], x + 1, _armorContents[x]).send(player);
        return _teleportPacket;
    }

    private byte getBedByte() {
        switch (AdvancedLocation.getCardinalDirection(_deadPlayer.getLocation())) {
            case NORTH:
                return 2;
            case SOUTH:
                return 0;
            case EAST:
                return 3;
            case WEST:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public String getSpawnCommand(PacketPlayer player) {
        return "AddTab, Spawn, Bed, Teleport";
    }

    @Override
    public void onClick(PacketEventInUseEntity event) {
    }
}
