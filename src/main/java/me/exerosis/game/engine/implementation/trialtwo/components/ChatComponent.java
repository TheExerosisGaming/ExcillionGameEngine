package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.util.ChatColors;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("static-method")
public class ChatComponent extends GameComponent implements ChatColors {
    public ChatComponent(Game game) {
        super(game);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(darkBlue() + " %s" + gray() + " %s");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(darkGray() + "Joined> " + reset() + gray() + event.getPlayer().getName());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(darkGray() + "Quit> " + reset() + gray() + event.getPlayer().getName());
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }
}