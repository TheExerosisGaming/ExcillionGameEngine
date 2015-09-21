package me.exerosis.game.engine.implementation.old.game.lms;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.pause.PauseCompoent;
import me.exerosis.game.engine.implementation.old.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import me.exerosis.game.engine.util.FreezePlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PreGameMotionStopComponent extends Component {
    @Depend
    private PauseCompoent _pauseComponent;

    public PreGameMotionStopComponent() {
    }

    @SuppressWarnings("static-method")
    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(GameState.IN_GAME))
            FreezePlayerUtil.getInstance().setAllFrozen(false, false);
        else if (event.getGameState().equals(GameState.PRE_GAME))
            FreezePlayerUtil.getInstance().setAllFrozen(true, false);
    }

    @Override
    public void run() {
        if (!_pauseComponent.isPaused())
            for (Player player : Arena.getPlayers())
                player.setFoodLevel(20);
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