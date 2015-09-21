package me.exerosis.game.engine.implementation.trialtwo.components.player.death.temp;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.trialtwo.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.countdown.countdowns.DeadPlayerCountdown;
import me.exerosis.game.engine.implementation.old.event.PlayerLeaveEvent;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.old.event.game.player.PlayerKilledEvent;
import me.exerosis.game.engine.core.state.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class LivesComponent extends Component {
    @Depend
    private GameFolderManager _gameFolderManager;

    private HashMap<Player, Integer> _lives = new HashMap<Player, Integer>();
    private int _livesPerPlayer;

    public LivesComponent() {
    }

    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (removeLife(event.getPlayer()) > 0)
            new DeadPlayerCountdown(event.getPlayer(), getGame());
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME))
            for (Player player : Arena.getPlayers())
                _lives.put(player, _livesPerPlayer);
        else if (event.getNewGameState().equals(GameState.RESTARTING))
            _lives.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (!getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING, GameState.STARTING))
            return;
        if (!_lives.containsKey(event.getPlayer()))
            _lives.put(event.getPlayer(), _livesPerPlayer);
        else
            _lives.replace(event.getPlayer(), _livesPerPlayer);
    }

    @EventHandler
    public void onLeave(PlayerLeaveEvent event) {
        if (_lives.containsKey(event.getPlayer()))
            _lives.remove(event.getPlayer());
    }

    public int getLives(Player player) {
        return _lives.get(player);
    }

    public int addLife(Player player) {
        int lives = _lives.get(player) + 1;
        _lives.replace(player, lives);
        return lives;
    }

    public int removeLife(Player player) {
        int lives = _lives.get(player) - 1;
        if (lives > -1)
            _lives.replace(player, lives);
        return lives;
    }

    @Override
    public void onEnable() {
        registerListener(this);
        _livesPerPlayer = _gameFolderManager.getGameConfigValue("lives", Integer.class);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}
