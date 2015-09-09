package me.exerosis.game.engine.componentgame.game.lms.corpse;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerKilledEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class CorpseComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    private Set<Corpse> corpses = new HashSet<Corpse>();

    public CorpseComponent() {
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    //Listeners
    @EventHandler
    public void onDeath(PlayerKilledEvent event) {
        if (!getArena().getGameState().equals(GameState.IN_GAME, GameState.POST_GAME))
            return;
        Player player = event.getPlayer();
        new Corpse(player);
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.RESTARTING))
            for (Corpse corpse : corpses)
                corpse.sendModCommand("RemoveTab, Remove");
    }
}
