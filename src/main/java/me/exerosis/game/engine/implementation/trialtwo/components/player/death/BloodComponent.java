package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class BloodComponent extends GameComponent {
    private int blood;

    public BloodComponent(Plugin plugin, Game game) {
        super(game);
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event){
        playDeathEffect(event.getPlayer().getLocation());
    }

    private ItemStack getNextStack() {
        ItemStack stack = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Blood " + blood++);
        stack.setItemMeta(meta);
        return stack;
    }

    public void playDeathEffect(Location location) {
        for (int x = 0; x < 6; x++) {
            World world = location.getWorld();

            world.playEffect(location, Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
            world.playEffect(location.clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());

            world.dropItemNaturally(location, getNextStack());
        }
    }
}