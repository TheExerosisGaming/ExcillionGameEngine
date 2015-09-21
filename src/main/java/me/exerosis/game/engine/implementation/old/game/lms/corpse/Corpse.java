package me.exerosis.game.engine.implementation.old.game.lms.corpse;

import me.exerosis.packet.player.injection.events.in.PacketEventInUseEntity;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.entities.PacketEntityPlayer;
import me.exerosis.packet.utils.location.AdvancedLocation;
import me.exerosis.packet.utils.packet.PacketFactory;
import me.exerosis.reflection.Reflect;
import net.minecraft.server.v1_8_R1.DataWatcher;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.Packet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("NonSerializableFieldInSerializableClass")
public class Corpse extends PacketEntityPlayer {
    //ID
    private static final long serialVersionUID = -9208368020756023036L;
    private int _id = (int) (Math.random() * 10000);

    //Player
    private Player _deadPlayer;
    private PacketPlayer _deadPacketPlayer;

    //Packets
    private Object bedPacket;
    private Object teleportPacket;

    private Object[] armorPackets;

    public Corpse(Player deadPlayer) {
        super(deadPlayer.getName(), deadPlayer.getLocation(), deadPlayer.getUniqueId());
        _deadPlayer = deadPlayer;
        _deadPacketPlayer = PlayerHandler.getPlayer(deadPlayer);

        ItemStack[] armor = _deadPlayer.getInventory().getArmorContents().clone();

        for (int x = 0; x < 4; x++)
            armorPackets[x] = PacketFactory.PacketPlayOutEntityEquipment(getEID()[0], x + 1, armor[x]);

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

        AdvancedLocation location = new AdvancedLocation(_deadPlayer.getLocation());

        int x = location.getPointX();
        double y = (int) (location.getY() + 0.1);

        if (location.isSlab())
            y += 0.5;

        y *= 32.0D;

        int z = location.getPointZ();

        teleportPacket = PacketFactory.PacketPlayOutEntityTeleport(_id, x, y, z, 0, 0, false);
        bedPacket = PacketFactory.PacketPlayOutBed(_id, location.getX(), 1, location.getY());
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
            player.getPlayer().sendBlockChange(location, Material.BED_BLOCK.getId(), _deadPacketPlayer.getAdvancedLocation().getBedByte());
        }
        return (Packet) PacketFactory.PacketPlayOutNamedEntitySpawn(getCraftPlayer());
    }


    public Object getHelmetPacket() {
        return armorPackets[0];
    }

    public Object getChestplatePacket() {
        return armorPackets[1];
    }

    public Object getLeggingsPacket() {
        return armorPackets[2];
    }

    public Object getBootsPacket() {
        return armorPackets[3];
    }


    public Object getBedPacket() {
        return bedPacket;
    }

    public Object getTeleportPacket() {
        return teleportPacket;
    }

    @Override
    public String getSpawnCommand(PacketPlayer player) {
        return "AddTab, Spawn, Bed, Teleport, Helmet, Chestplate, Leggings, Boots";
    }

    @Override
    public void onClick(PacketEventInUseEntity event) {
    }
}
