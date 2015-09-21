package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.StateComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerKilledEvent;
import me.exerosis.game.engine.util.ChatColors;
import me.exerosis.reflection.event.EventListener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

@SuppressWarnings("deprecation")
public class DeathComponent extends StateComponent implements ChatColors {
    private int blood = 0;

    public DeathComponent(Game game) {
        super(game, GameLocation.GAME_WORLD);
    }

    @EventListener
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
        event.getEntity().setHealth(20.0);
        event.getDrops().clear();
        event.getEntity().setExp(0);
        kill(event.getEntity(), event.getEntity().getKiller());
    }

    public void kill(Player player) {
        kill(player, null);
    }
    public void kill(Player player, Player killer) {
        callEvent(new PlayerKilledEvent(player, killer), event -> {
            if (event.isCancelled())
                return;
            broadcast(darkGray() + "Died> " + reset() + gray() + player.getName());
        });
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        super.onEnable();
    }

}