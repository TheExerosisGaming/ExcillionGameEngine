/*
package me.exerosis.game.engine.componentgame.component.core.player.death.spectate;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.packet.PacketAPI;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.Camera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class SpectateGamemode extends Component {
	@Depend
	private SpectateComponent _spectateComponent;
	@Depend
	private SpectatorInventoryComponent _inventoryComponent;
	private Set<Player> _players = new HashSet<Player>();
	private Set<Player> _inPlayer = new HashSet<Player>();

	public SpectateGamemode() {
		Bukkit.getPluginManager().registerEvents(this, PacketAPI.getPlugin());
	}
	public void removeSpectating(Player player) {
		_inPlayer.remove(player);
		PlayerHandler.getPlayer(player).sendPacket(new Camera(player));
	}
	public void setSpectating(Player player, Entity entity) {
		_inPlayer.add(player);
		PlayerHandler.getPlayer(player).sendPacket(new Camera(entity));
	}

	@SuppressWarnings("deprecation")
	public void addPlayer(Player player) {
		_players.add(player);
		player.setGameMode(GameMode.CREATIVE);
		player.setFlying(true);
		player.getInventory().clear();
		Bukkit.getScheduler().runTaskLater(getPlugin(), () -> player.getInventory().setItem(8, new ItemStack(Material.COMPASS)), 20);
		for(Player onlinePlayer : Arena.getPlayers())
			if(!player.equals(onlinePlayer))
				onlinePlayer.hidePlayer(player);
	}
	public void removePlayer(Player player) {
		if(player != null && _players.contains(player))
			_players.remove(player);
	}
	public boolean contains(Player player) {
		return _players.contains(player);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(!_spectateComponent.isSpectating(player))
			return;
		if(!player.getItemInHand().getType().equals(Material.COMPASS))
			return;

		Inventory inventory = _inventoryComponent.getInventory(player);
		player.openInventory(inventory);
		if(_players.contains(player))
			if(!player.getItemInHand().getType().equals(Material.BOW))
				event.setCancelled(true);
	}

	@EventHandler
	public void onClick(PlayerInteractEntityEvent event){
		if(!_players.contains(event.getPlayer()))
			return;
		setSpectating(event.getPlayer(), event.getRightClicked());
	}
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event) {
		if(!_inPlayer.contains(event.getPlayer()))
			return;
		removePlayer(event.getPlayer());
	}

	@Override
	public void onEnable() {
		registerListener(this);
	}

	@Override
	public void onDisable() {
		unregisterListener(this);
		for(Player player : _players)
			removePlayer(player);
	}
}*/
