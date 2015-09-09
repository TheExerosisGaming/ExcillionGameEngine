package me.exerosis.game.engine.componentgame.game.spleef;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.death.DeathComponent;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseState;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseStateChangeEvent;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerKilledEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.ActionBar;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@SuppressWarnings("static-method")
public class SpleefComponent extends Component {
    @Depend
    private DeathComponent _deathComponent;
    @Depend
    private SpectateComponent _spectateComponent;
    private ActionBar _actionBar;

    public SpleefComponent() {
        _actionBar = new ActionBar(1, "");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void run() {
        for (Player player : Arena.getPlayers()) {
            PacketPlayer packetPlayer = PlayerHandler.getPlayer(player);
            if (_spectateComponent.isSpectating(player))
                return;
            if (player.getFoodLevel() == 0) {
                player.damage(2.0);
                _actionBar.setMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Break blocks to regain hunger!");
                packetPlayer.setDisplayed(_actionBar, true);
            } else {
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

    @EventHandler
    public void onPauseStateChange(PauseStateChangeEvent event) {
        if (getArena().getGameState().equals(GameState.IN_GAME))
            if (event.getPauseState().equals(PauseState.PAUSED))
                stopTask();
            else if (event.getPauseState().equals(PauseState.RESUMED))
                startTask(20, 20);
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        Bukkit.getScheduler().runTaskLater(getPlugin(), new Runnable() {
            @Override
            public void run() {
                PlayerHandler.getPlayer(event.getPlayer()).setDisplayed(_actionBar, false);
            }
        }, 5);
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.IN_GAME))
            startTask(20, 20);
        else
            stopTask();
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        stopTask();
    }
}