package me.exerosis.game.engine.implementation.trialtwo.components.player;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerClearEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class PlayerComponent extends GameComponent {
    private SpawnpointComponent _spawnpointComponent;
    private GameMode _defaultGameMode;
    private int _maxPlayers;

    public PlayerComponent(Game game, SpawnpointComponent spawnpointComponent) {
        super(game);
        _spawnpointComponent = spawnpointComponent;
        _maxPlayers = getGame().getGameConfigValue("maxPlayers", Integer.class);
    }

    @Override
    public void onEnable() {
        registerListener();
        getGame().getGameConfig().addDefault("defaultGameMode", "ADVENTURE");
        _defaultGameMode = GameMode.valueOf(getGame().getGameConfigValue("defaultGameMode", String.class));
        getGame().saveConfig();
        super.onEnable();
    }

    @EventHandler
    public void onPreLogin(PlayerLoginEvent event) {
        if (getPlayers().size() >= _maxPlayers)
            event.disallow(Result.KICK_OTHER, String.valueOf(ChatColor.RED) + ChatColor.BOLD + "The game is full!");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        _spawnpointComponent.sendToSpawn(event.getPlayer());
        clearPlayer(event.getPlayer(), true);
    }

    public void clearPlayer(Player player) {
        clearPlayer(player, true);
    }

    public void clearPlayer(Player player, boolean full) {
        callEvent(new PlayerClearEvent(player, full), event -> {
            if (event.isCancelled())
                return;
            player.setGameMode(_defaultGameMode);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.setFallDistance(0);
            player.setExp(0);
            player.setWalkSpeed(0.2F);
            if (!event.isFull())
                return;
            PlayerInventory inventory = player.getInventory();
            inventory.clear();
            inventory.removeItem(inventory.getArmorContents());

            for (PotionEffect potionEffect : player.getActivePotionEffects())
                player.removePotionEffect(potionEffect.getType());
        });
    }

    public GameMode getDefaultGameMode() {
        return _defaultGameMode;
    }

    public int getMaxPlayers() {
        return _maxPlayers;
    }
}