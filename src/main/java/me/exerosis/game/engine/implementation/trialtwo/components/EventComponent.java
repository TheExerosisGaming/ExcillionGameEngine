package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.reflection.event.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("static-method")
public class EventComponent extends GameComponent {
    public EventComponent(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        for (World world : Bukkit.getWorlds()) {
            world.getEntities().stream().filter(e -> e instanceof Monster || e instanceof Animals).forEach(org.bukkit.entity.Entity::remove);
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

    @EventListener
    public void onExpChange(PlayerExpChangeEvent event) {
        event.getPlayer().setExp(0);
    }

    @EventListener
    public void onRain(WeatherChangeEvent event) {
        event.getWorld().setWeatherDuration(0);
    }

    @EventListener
    public void onShoot(EntityShootBowEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER))
            return;
        if (!getGameState().equals(GameLocation.LOBBY_WORLD.getStates()))
            return;
        event.setCancelled(true);
        Player player = (Player) event.getEntity();
        for (ItemStack stack : player.getInventory().getContents())
            if (stack.getType().equals(Material.ARROW))
                stack.setAmount(stack.getAmount() + 1);
    }

    @EventListener
    public void itemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventListener
    public void itemDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventListener
    public void onHungerLower(FoodLevelChangeEvent event) {
        if (getGameState().equals(GameLocation.LOBBY_WORLD.getStates()))
            event.setCancelled(true);
    }

    @EventListener
    public void onEntityDamage(EntityDamageEvent event) {
        if (getGameState().equals(GameLocation.LOBBY_WORLD.getStates()))
            event.setCancelled(true);
    }

    @EventListener
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEvent(Event event){
        callEvent(event);
    }

  /*  @EventListener
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> Bukkit.getPluginManager().callEvent(new PlayerLeaveEvent(event.getPlayer(), event.getQuitMessage())), 1);
    }*/
}