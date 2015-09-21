package me.exerosis.game.engine.implementation.old.game.lms.armor;

import me.exerosis.game.engine.implementation.old.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.EntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class FakeArmor implements Listener {
    private ItemStack _helmet;
    private ItemStack _chestplate;
    private ItemStack _leggings;
    private ItemStack _boots;
    private Player _player;
    private SpectateComponent _spectateComponent;
    private Arena _arena;

    public FakeArmor(Player player, Arena arena, SpectateComponent spectateComponent, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        _player = player;
        _arena = arena;
        _spectateComponent = spectateComponent;
        _helmet = helmet;
        _chestplate = chestplate;
        _leggings = leggings;
        _boots = boots;
        Bukkit.getPluginManager().registerEvents(this, arena.getPlugin());
    }

    //Listeners
    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (!event.getGameState().equals(GameState.PRE_GAME))
            return;
        for (Player player : Arena.getPlayers())
            sendFakeArmor(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (_arena.getGameState().equals(GameState.RESTARTING, GameState.LOBBY))
            return;
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();

        if (!_spectateComponent.getGamePlayers().contains(player))
            return;
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(_arena.getPlugin(), new Runnable() {
            @Override
            public void run() {
                sendFakeArmor(player);
            }
        }, 1);
    }

    private void sendFakeArmor(Player player) {
        int ID = PlayerHandler.getPlayer(player).getCraftPlayer().getId();
        for (PacketPlayer packetPlayer : PlayerHandler.getOnlinePlayers()) {
            new EntityEquipment(ID, 3, _helmet).send(packetPlayer);
            new EntityEquipment(ID, 2, _chestplate).send(packetPlayer);
            new EntityEquipment(ID, 1, _leggings).send(packetPlayer);
            new EntityEquipment(ID, 0, _boots).send(packetPlayer);
        }
    }

    //Getters and setters.
    public Player getPlayer() {
        return _player;
    }

    //Armor getters.
    public ItemStack getHelmet() {
        return _helmet;
    }

    //Armor setters.
    public void setHelmet(ItemStack helmet) {
        _helmet = helmet;
    }

    public ItemStack getChestplate() {
        return _chestplate;
    }

    public void setChestplate(ItemStack chestplate) {
        _chestplate = chestplate;
    }

    public ItemStack getLeggings() {
        return _leggings;
    }

    public void setLeggings(ItemStack leggings) {
        _leggings = leggings;
    }

    public ItemStack getBoots() {
        return _boots;
    }

    public void setBoots(ItemStack boots) {
        _boots = boots;
    }
}
