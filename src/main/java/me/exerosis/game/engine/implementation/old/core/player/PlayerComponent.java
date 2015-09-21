package me.exerosis.game.engine.implementation.old.core.player;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.CoreGameComponent;
import me.exerosis.game.engine.implementation.trialtwo.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.old.core.scoreboard.ScoreboardComponent;
import me.exerosis.game.engine.implementation.trialtwo.world.WorldComponent;
import me.exerosis.game.engine.implementation.old.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class PlayerComponent extends Component {
    @Depend
    private WorldComponent _worldComponent;
    @Depend
    private SpawnpointComponent _spawnpointComponent;
    @Depend
    private ScoreboardComponent _scoreboardComponent;
    @Depend
    private CoreGameComponent _coreGameComponent;
    private GameMode _defaultGameMode;

    public PlayerComponent(GameMode defaultGameMode) {
        _defaultGameMode = defaultGameMode;
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(GameState.RESTARTING))
            for (Player player : Arena.getPlayers())
                sendToSpawn(player);
    }

    @EventHandler
    public void onPreLogin(PlayerLoginEvent event) {
        if (Arena.getPlayers().size() < _coreGameComponent.getMaxPlayers())
            return;
        StringBuilder kickMessage = new StringBuilder();
        kickMessage.append(ChatColor.RED).append(ChatColor.BOLD);
        event.disallow(Result.KICK_OTHER, kickMessage.append("The game is full!").toString());
    }

    public void clearPlayer(Player player) {
        player.setGameMode(_defaultGameMode);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setExp(0);
        player.setWalkSpeed(0.2F);
    }

    public void fullClear(Player player) {
        clearPlayer(player);

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.removeItem(inventory.getArmorContents());

        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());
    }

    private Location getLobbySpawn() {
        return _worldComponent.getLobbyWorld().getSpawnLocation().add(0, 4, 0);
    }

    public void sendToSpawn(Player player) {
        if (getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING)) {
            player.teleport(getLobbySpawn());
            fullClear(player);
        } else {
            Location gameSpawn = _spawnpointComponent.getNextSpawn();
            player.teleport(gameSpawn);
            clearPlayer(player);
        }
    }

    public GameMode getDefaultGameMode() {
        return _defaultGameMode;
    }
}
