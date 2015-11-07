package me.exerosis.game.engine.implementation.trialtwo.games.spleef;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.packet.display.displayables.ActionBar;
import me.exerosis.packet.injection.PacketPlayer;
import me.exerosis.packet.injection.handlers.PlayerHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpleefComponent extends GameComponent {
    private DeathComponent _deathComponent;
    private SpectateComponent _spectateComponent;
    private ActionBar _actionBar;
    private boolean _paused;

    public SpleefComponent(Game game, DeathComponent deathComponent, SpectateComponent spectateComponent) {
        super(game);
        _deathComponent = deathComponent;
        _spectateComponent = spectateComponent;
        _actionBar = new ActionBar(1, "");
    }

    @Override
    public void run() {
        for (Player player : getPlayers()) {
            PacketPlayer packetPlayer = PlayerHandler.getPlayer(player);
            if (_spectateComponent.isSpectating(player))
                return;
            if (player.getFoodLevel() == 0) {
                player.damage(2.0);
                _actionBar.setMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Break blocks to regain hunger!");
                packetPlayer.setDisplayed(_actionBar, true);
            }
            else {
                player.setFoodLevel(player.getFoodLevel() - 1);
                packetPlayer.setDisplayed(_actionBar, false);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause().equals(DamageCause.LAVA))
            _deathComponent.kill((Player) event.getEntity(), null);
    }

    @EventListener
    public void onDeath(PlayerKilledEvent event) {
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> PlayerHandler.getPlayer(event.getPlayer()).setDisplayed(_actionBar, false), 5);
    }

    @Override
    public void onEnable() {
        registerListener();
        startTask(20, 20);
        for (Player player : getPlayers()) player.setGameMode(GameMode.SURVIVAL);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopTask();
        for (Player player : getPlayers()) player.setGameMode(GameMode.ADVENTURE);
        super.onDisable();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (_paused || !event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        Player player = event.getPlayer();
        if (_spectateComponent.isSpectating(player))
            return;
        Block block = event.getClickedBlock();

        if (block.getType().equals(Material.BEDROCK))
            return;
        int newFoodLevel = player.getFoodLevel() == 20 ? 20 : player.getFoodLevel() + 1;

        player.setFoodLevel(newFoodLevel);
        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId());
        block.setType(Material.AIR);
    }

    public boolean isPaused() {
        return _paused;
    }

    public void setPaused(boolean paused) {
        _paused = paused;
        if (paused)
            stopTask();
        else
            startTask(20, 20);
    }
}