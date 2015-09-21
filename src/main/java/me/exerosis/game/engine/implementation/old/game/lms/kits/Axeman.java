package me.exerosis.game.engine.implementation.old.game.lms.kits;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.kit.Kit;
import me.exerosis.game.engine.implementation.old.core.kit.KitsComponent;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.BattleAxe;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.throwing.axes.ThrowingAxe;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Axeman extends Kit {
    @Depend
    private ThrowingAxe _throwingAxe;
    @Depend
    private BattleAxe _battleAxe;
    @Depend
    private KitsComponent _kitsComponent;
    private Set<Player> _players = new HashSet<Player>();

    public Axeman() {
    }

    public boolean isAxeman(Player player) {
        return _players.contains(player);
    }

    @Override
    public ItemStack getKitItem(Player player) {
        ItemStack stack = new ItemStack(Material.GOLD_HOE, 1);
        ItemMeta meta = stack.getItemMeta();
        boolean hasKit = _kitsComponent.hasKit(player, this);

        StringBuilder stringBuilder = new StringBuilder();
        ChatColor color = ChatColor.RED;
        if (hasKit)
            color = ChatColor.GREEN;
        stringBuilder.append(color + "Axe Thrower");
        meta.setDisplayName(stringBuilder.toString());

        List<String> lore = new ArrayList<String>();
        lore.add(color + "5 Axes");
        lore.add(color + "Can right click to throw an axe!");
        if (hasKit)
            lore.add(color + "Click to equip!");
        else {
            int money = _kitsComponent.getMoney(player);
            int cost = getCost();
            lore.add("");
            lore.add("");
            lore.add(ChatColor.GREEN + "Click to purchase.");

            lore.add(ChatColor.BLUE + "Cost: " + ChatColor.GREEN + String.valueOf(cost));
            lore.add(ChatColor.BLUE + "Coins: " + ChatColor.GREEN + String.valueOf(money));
            lore.add(ChatColor.BLUE + "------------------------");
            if (money >= cost)
                lore.add(ChatColor.BLUE + "Left: " + ChatColor.GREEN + String.valueOf(money - cost));
            else
                lore.add(ChatColor.BLUE + "Required: " + ChatColor.RED + String.valueOf(cost - money));
        }
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public int getCost() {
        return 20;
    }

    @Override
    public int getDBPos() {
        return 0;
    }

    @Override
    public String getName() {
        return "Axe Thrower";
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onKitEnable(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(new MaterialData(Material.CARPET, DyeColor.GRAY.getWoolData()).toItemStack());
        inventory.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        inventory.setItem(0, _battleAxe.getItemStack());

        ItemStack stack = _throwingAxe.getItemStack();
        stack.setAmount(5);
        inventory.setItem(_throwingAxe.getSlot(), stack);

        _players.add(player);
    }

    @SuppressWarnings("static-method")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setResourcePack("https://www.dropbox.com/s/08zecg17mw1rqha/testPack.zip?dl=1");
    }

    @Override
    public void onKitDisable(Player player) {
        _players.remove(player);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!getArena().getGameState().equals(GameState.LOBBY))
            return;
        Material type = event.getPlayer().getItemInHand().getType();
        if (!type.equals(_throwingAxe.getMaterial()) || !type.equals(_battleAxe.getMaterial()))
            return;
        if (!isAxeman(event.getPlayer()))
            return;

        event.getPlayer().sendMessage(ChatColor.RED + "MEH! Too lazy to put a tutorial in! FIGURE IT OUT ON YOUR OWN!");
    }
}