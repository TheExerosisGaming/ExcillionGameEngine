/*
package me.exerosis.game.engine.componentgame.component.core.player.death.spectate;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerSpectateEvent;
import me.exerosis.game.engine.util.SkullUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SpectatorInventoryComponent extends Component {
	@Depend
	private SpectateComponent _spectateComponent;
	@Depend
	private SpectateGamemode _spectateGamemode;
	private Map<Player, Inventory> _inventories = new HashMap<Player, Inventory>();

	public SpectatorInventoryComponent() {}

	@EventHandler
	public void onSpectate(PlayerSpectateEvent event) {
		Player player = event.getPlayer();

		if(!_inventories.containsKey(player)) {
			Inventory inventory = Bukkit.createInventory(player, 9, "Players");
			for(int x  = 0; x < _spectateComponent.getNumberPlayers(); x++) {
				String playerName = _spectateComponent.getGamePlayers().get(x).getName();
				inventory.setItem(x, SkullUtil.getSkullItem(playerName));
			}
			_inventories.put(player, inventory);
		}
		
		for(Inventory inventory : _inventories.values()) {
			int x = 0;
			ItemStack[] contents = inventory.getContents();
			while(x < contents.length)
				if(contents[x] != null && contents[x].getItemMeta().getDisplayName().equals(player.getName()))
					inventory.remove(contents[x]);
				else
					x++;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(!_inventories.containsKey(event.getWhoClicked()))
			return;
		String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
		Player clickedPlayer = Bukkit.getPlayer(name);
		if(clickedPlayer != null && clickedPlayer.isOnline())
			_spectateGamemode.setSpectating((Player) event.getWhoClicked(), clickedPlayer);
	}
	
	public Inventory getInventory(Player player) {
		return _inventories.get(player);
	}

	@Override
	public void onEnable() {
		registerListener(this);
	}

	@Override
	public void onDisable() {
		unregisterListener(this);
		_inventories.clear();
	}
}
*/
