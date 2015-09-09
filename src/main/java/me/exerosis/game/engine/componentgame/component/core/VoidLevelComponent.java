package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.death.DeathComponent;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.component.core.world.WorldComponent;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.TreeSet;

public class VoidLevelComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private WorldComponent _worldComponent;
    @Depend
    private DeathComponent _deathComponent;
    private int _voidLevel;

    public VoidLevelComponent() {
    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME, GameState.IN_GAME))
            startTask(1, 1);
        else
            stopTask();
    }

    @Override
    public void run() {
        TreeSet<Player> toKill = new TreeSet<Player>((o1, o2) -> Double.compare(o1.getLocation().getY(), o2.getLocation().getY()));

        for (Player player : _spectateComponent.getGamePlayers())
            if (player.getLocation().getY() <= _voidLevel)
                toKill.add(player);

        for (Player player : toKill)
            _deathComponent.kill(player, null);
    }

    @Override
    public void onEnable() {
        registerListener(this);
        _voidLevel = _worldComponent.getMapDataValue("voidLevel", Integer.class);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        stopTask();
    }
}
