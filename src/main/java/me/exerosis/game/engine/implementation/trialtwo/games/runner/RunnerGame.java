package me.exerosis.game.engine.implementation.trialtwo.games.runner;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreComponentBundle;
import me.exerosis.io.Remote.RemoteFolder;
import org.bukkit.plugin.Plugin;

public class RunnerGame extends Game {
    public RunnerGame(Plugin plugin, Arena arena, RemoteFolder gameFolder, String name) {
        super(plugin, arena, gameFolder, name);
        CoreComponentBundle coreComponentBundle = new CoreComponentBundle(this);
        addComponent(GameState.IN_GAME, GameState.POST_GAME, new BlockDropComponent(this, coreComponentBundle.getSpectateComponent(), 3));
    }
}
