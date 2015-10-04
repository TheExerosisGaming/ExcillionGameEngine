package me.exerosis.game.engine.implementation.trialtwo.event.player;

import me.exerosis.component.event.Cancellable;
import org.bukkit.entity.Player;

public class PlayerSpectateEvent extends GamePlayerEvent implements Cancellable {
    public PlayerSpectateEvent(Player player) {
        super(player);
    }
}
