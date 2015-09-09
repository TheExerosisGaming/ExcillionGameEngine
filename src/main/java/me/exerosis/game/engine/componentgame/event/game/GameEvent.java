package me.exerosis.game.engine.componentgame.event.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public GameEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
