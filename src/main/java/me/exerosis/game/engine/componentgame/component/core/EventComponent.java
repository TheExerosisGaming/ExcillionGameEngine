package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.pause.PauseCompoent;
import me.exerosis.game.engine.componentgame.event.PlayerLeaveEvent;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("static-method")
public class EventComponent extends Component {
    @Depend
    private PauseCompoent _pauseCompoent;

    public EventComponent() {
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities())
                if (entity instanceof Monster || entity instanceof Animals)
                    entity.remove();
            world.setDifficulty(Difficulty.NORMAL);
            world.setThunderDuration(0);
            world.setThundering(false);
            world.setStorm(false);
            world.setTime(0);
            world.setAutoSave(false);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("showDeathMessages", "false");
            world.setGameRuleValue("doMobSpawning", "false");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEvent event) {
        if (_pauseCompoent.isPaused())
            event.setCancelled(true);
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        event.getPlayer().setExp(0);
    }

    @EventHandler
    public void onRain(WeatherChangeEvent event) {
        event.getWorld().setWeatherDuration(0);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if ((!getArena().getGameState().equals(GameState.POST_GAME, GameState.IN_GAME)) || _pauseCompoent.isPaused()) {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            for (ItemStack stack : player.getInventory().getContents()) {
                if (stack.getType().equals(Material.ARROW))
                    stack.setAmount(stack.getAmount() + 1);
            }
        }
    }

    @EventHandler
    public void itemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void itemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onHungerLower(FoodLevelChangeEvent event) {
        if (!_pauseCompoent.isPaused()) ;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!getArena().getGameState().equals(GameState.POST_GAME, GameState.IN_GAME) || _pauseCompoent.isPaused())
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                Bukkit.getPluginManager().callEvent(new PlayerLeaveEvent(event.getPlayer(), event.getQuitMessage()));
            }
        }, 1);
    }
}