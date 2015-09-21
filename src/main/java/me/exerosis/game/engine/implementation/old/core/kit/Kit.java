package me.exerosis.game.engine.implementation.old.core.kit;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.player.PlayerDataComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Kit extends Component {
    @Depend
    private PlayerDataComponent _playerDataComponent;
    @Depend
    private KitsComponent _kitsComponent;

    public Kit() {
    }

    public abstract void onKitEnable(Player player);

    public abstract void onKitDisable(Player player);

    public abstract int getSlot();

    public abstract String getName();

    public abstract ItemStack getKitItem(Player player);

    public abstract int getCost();

    public abstract int getDBPos();

    @EventHandler
    public void InventoryClick(InventoryClickEvent event) {
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

            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.GREEN);
            builder.append(ChatColor.BOLD);
            builder.append("Purchased ");
            builder.append(getName());
            builder.append(" ");
            builder.append(getCost());
            builder.append(" coins removed from your account!");
            player.sendMessage(builder.toString());

            player.closeInventory();
            return;
        }
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 10, 10);
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.RED);
        builder.append(ChatColor.BOLD);
        builder.append("You don't have enough coins to buy that kit!");
        player.sendMessage(builder.toString());
        player.getOpenInventory().setItem(getSlot(), getKitItem(player));
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }
}
