package me.exerosis.game.engine.implementation.old.game.spleef.abilities;

import me.exerosis.game.engine.implementation.old.game.spleef.abilities.ground.Ground;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class GroundSmashCompoent extends Component {

    public GroundSmashCompoent() {
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @SuppressWarnings("static-method")
    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        new Ground(event.getPlayer().getLocation().add(0, 2, 0), 10);
    }

}
