package me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.spleef.abilities.ground.Ground;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class GroundSmashComponent extends GameComponent {

    public GroundSmashComponent(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        registerListener();
    }

    @Override
    public void onDisable() {
        unregisterListener();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        new Ground(event.getPlayer().getLocation().add(0, 2, 0), 10);
    }
}