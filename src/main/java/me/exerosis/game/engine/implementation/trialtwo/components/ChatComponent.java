package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.util.ChatColors;
import me.exerosis.reflection.event.EventListener;
import me.exerosis.reflection.event.Priority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("static-method")
public class ChatComponent extends GameComponent implements ChatColors {
    public ChatComponent(Game game) {
        super(game);
    }

    @EventListener
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(darkBlue() + " %s" + gray() + " %s");
    }

    @EventListener
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(darkGray() + "Joined> " + reset() + gray() + event.getPlayer().getName());
    }

    @EventListener(priority = Priority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(darkGray() + "Quit> " + reset() + gray() + event.getPlayer().getName());
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }
}