package me.exerosis.game.engine.componentgame.component.core.player.death;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerKilledEvent;
import me.exerosis.game.engine.componentgame.game.lms.DeathmatchGame;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class DeathComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    private int blood = 0;

    public DeathComponent() {
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        event.getEntity().setHealth(20.0);
        event.getDrops().clear();
        kill(event.getEntity(), event.getEntity().getKiller());
    }

    public void kill(Player player, Player killer) {
        PlayerKilledEvent event = new PlayerKilledEvent(player, killer);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        StringBuilder message = new StringBuilder(ChatColor.DARK_GRAY.toString());
        message.append("Died> ").append(ChatColor.RESET).append(ChatColor.GRAY);
        Bukkit.broadcastMessage(message.append(player.getName()).toString());

        playDeathEffect(player.getLocation());

        _spectateComponent.setSpectating(player);
    }

    private ItemStack getNextStack() {
        ItemStack stack = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName("Blood " + blood++);
        stack.setItemMeta(meta);
        return stack;
    }

    private void playDeathEffect(Location location) {
        for (int x = 0; x < 6; x++) {
            World world = location.getWorld();

            world.playEffect(location, Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());
            world.playEffect(location.clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK.getId());

            if (!(getGame() instanceof DeathmatchGame))
                world.dropItemNaturally(location, getNextStack());
        }
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}