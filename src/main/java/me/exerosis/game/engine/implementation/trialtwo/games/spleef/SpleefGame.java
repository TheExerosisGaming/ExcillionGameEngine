package me.exerosis.game.engine.implementation.trialtwo.games.spleef;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreComponentBundle;
import me.exerosis.io.Remote.RemoteFolder;
import org.bukkit.plugin.Plugin;

public class SpleefGame extends Game {
    public SpleefGame(Plugin plugin, Arena arena, RemoteFolder gameFolder, String name) {
        super(plugin, arena, gameFolder, name);
        CoreComponentBundle coreComponentBundle = new CoreComponentBundle(this);
        addComponent(new SpleefComponent(this, coreComponentBundle.getDeathComponent(), coreComponentBundle.getSpectateComponent()));
    }
}
