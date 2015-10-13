package me.exerosis.game.engine.implementation.trialtwo.games.spleef;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreComponentBundle;
import me.exerosis.game.engine.implementation.trialtwo.components.EventCanceller;
import me.exerosis.game.engine.implementation.trialtwo.games.spleef.doublejump.DoubleJumpComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.spleef.doublejump.DoubleJumpCooldown;
import me.exerosis.io.Remote.RemoteFolder;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

public class SpleefGame extends Game {
    public SpleefGame(Plugin plugin, Arena arena, RemoteFolder gameFolder, String name) {
        super(plugin, arena, gameFolder, name);
        CoreComponentBundle coreComponentBundle = new CoreComponentBundle(this);
        DoubleJumpCooldown doubleJumpCooldown = new DoubleJumpCooldown(this);

        new EventCanceller(this, EntityDamageEvent.class, FoodLevelChangeEvent.class);

        addComponent(GameState.IN_GAME, GameState.POST_GAME, doubleJumpCooldown);
        addComponent(GameState.IN_GAME, GameState.POST_GAME, new SpleefComponent(this, coreComponentBundle.getDeathComponent(), coreComponentBundle.getSpectateComponent()));
        addComponent(GameState.IN_GAME, GameState.POST_GAME, new DoubleJumpComponent(this, coreComponentBundle.getSpectateComponent(), doubleJumpCooldown));
    }
}