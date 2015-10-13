package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EventCanceller extends GameComponent {
    private final Class<? extends Event>[] _events;

    @SafeVarargs
    public EventCanceller(Game game, Class<? extends Event>... events) {
        super(game);
        _events = events;
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        handleEvent(event);
    }

    private void handleEvent(Event event) {
        for (Class<? extends Event> clazz : _events) {
            if (clazz.isAssignableFrom(event.getClass())) {
                if (event instanceof Cancellable)
                    ((Cancellable) event).setCancelled(true);
                else if (event instanceof me.exerosis.component.event.Cancellable)
                    ((me.exerosis.component.event.Cancellable) event).setCancelled(true);
            }
        }
    }
}