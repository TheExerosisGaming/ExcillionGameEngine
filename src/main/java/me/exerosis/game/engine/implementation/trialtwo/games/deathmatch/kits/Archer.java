package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.kits;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.kit.Kit;
import me.exerosis.game.engine.implementation.trialtwo.components.kit.KitsComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.ShortSword;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.longbow.LongbowBow;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Archer extends Kit {
    private LongbowBow _longbowBow;
    private ShortSword _dagger;

    public Archer(Game game, KitsComponent kitsComponent, LongbowBow longbowBow, ShortSword dagger) {
        super(game, kitsComponent);
        _longbowBow = longbowBow;
        _dagger = dagger;
    }

    @Override
    public ItemStack getKitItem(Player player) {
        ItemStack stack = new ItemStack(Material.BOW, 1);
        ItemMeta meta = stack.getItemMeta();
        boolean hasKit = getKitsComponent().hasKit(player, this);

        StringBuilder stringBuilder = new StringBuilder();
        ChatColor color = ChatColor.RED;

        if (hasKit)
            color = ChatColor.GREEN;
        stringBuilder.append(color).append("Archer");
        meta.setDisplayName(stringBuilder.toString());

        List<String> lore = new ArrayList<>();
        lore.add(color + "Longbow and 5 arrows.");
        lore.add(color + "Gets an extra arrow every 15 seconds.");
        if (hasKit)
            lore.add(color + "Click to equip!");
        else {
            int money = getKitsComponent().getMoney(player);
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
        return 50;
    }

    @Override
    public int getDBPos() {
        return 1;
    }

    @Override
    public String getName() {
        return "Archer";
    }

    @Override
    public void onKitEnable(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(new ItemStack(Material.ACACIA_STAIRS));
        inventory.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.IRON_BOOTS));
        inventory.setItem(0, _dagger.getItemStack());
        _longbowBow.addArcher(player);
    }

    @Override
    public void onKitDisable(Player player) {
        _longbowBow.removeArcher(player);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!getGameState().equals(GameState.LOBBY))
            return;
        if (!event.getPlayer().getItemInHand().getType().equals(Material.BOW))
            return;
        if (!_longbowBow.isArcher(event.getPlayer()))
            return;
        event.getPlayer().sendMessage(ChatColor.RED + "MEH! Too lazy to put a tutorial in! FIGURE IT OUT ON YOUR OWN!");
    }

    @Override
    public int getSlot() {
        return 1;
    }
}