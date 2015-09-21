package me.exerosis.game.engine.implementation.old.game.lms.kits;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.kit.Kit;
import me.exerosis.game.engine.implementation.old.core.kit.KitsComponent;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.CrystalBlade;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.staff.Staff;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class Mage extends Kit {
    @Depend
    private CrystalBlade _crystalBlade;
    @Depend
    private KitsComponent _kitsComponent;
    @Depend
    private Staff _staff;

    public Mage() {
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onKitEnable(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(new MaterialData(Material.CARPET, DyeColor.PURPLE.getWoolData()).toItemStack());
        inventory.setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        inventory.setItem(0, _crystalBlade.getItemStack());
        inventory.setItem(1, _staff.getItemStack());
    }

    @Override
    public void onKitDisable(Player player) {

    }

    @Override
    public int getSlot() {
        return 2;
    }

    @Override
    public String getName() {
        return "Mage";
    }

    @Override
    public ItemStack getKitItem(Player player) {
        ItemStack stack = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = stack.getItemMeta();
        boolean hasKit = _kitsComponent.hasKit(player, this);

        StringBuilder stringBuilder = new StringBuilder();
        ChatColor color = ChatColor.RED;
        if (hasKit)
            color = ChatColor.GREEN;
        stringBuilder.append(color + "Mage");
        meta.setDisplayName(stringBuilder.toString());

        List<String> lore = new ArrayList<String>();
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
        return 100;
    }

    @Override
    public int getDBPos() {
        return 2;
    }
}
