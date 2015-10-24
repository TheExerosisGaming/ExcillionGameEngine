package me.exerosis.game.engine.implementation.trialtwo.components.kit;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Kit extends GameComponent {
    private KitsComponent _kitsComponent;

    public Kit(Game game, KitsComponent kitsComponent) {
        super(game);
        _kitsComponent = kitsComponent;
    }

    public abstract void onKitEnable(Player player);

    public abstract void onKitDisable(Player player);

    public abstract int getSlot();

    public abstract String getName();

    public abstract ItemStack getKitItem(Player player);

    public abstract int getCost();

    public abstract int getDBPos();

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (!event.getInventory().getName().equals("Kit Selector"))
            return;
        if (event.getAction().equals(InventoryAction.UNKNOWN))
            return;
        if (event.getSlot() != getSlot())
            return;
        Player player = (Player) event.getWhoClicked();

        if (_kitsComponent.hasKit(player, this)) {
            _kitsComponent.setDefaultKit(player, this);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 200, 0);
            player.closeInventory();
            return;
        }

        if (_kitsComponent.getMoney(player) >= getCost()) {
            _kitsComponent.purchase(player, this);

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 10, 10);

            player.sendMessage(String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + "Purchased " + getName() + " " + getCost() + " coins removed from your account!");

            player.closeInventory();
            return;
        }
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 10, 10);
        player.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "You don't have enough coins to buy that kit!");
        player.getOpenInventory().setItem(getSlot(), getKitItem(player));
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    public KitsComponent getKitsComponent() {
        return _kitsComponent;
    }
}