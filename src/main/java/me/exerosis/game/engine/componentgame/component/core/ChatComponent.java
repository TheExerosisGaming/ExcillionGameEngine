package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerData;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerDataComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("static-method")
public class ChatComponent extends Component {
    @Depend(false)
    private PlayerDataComponent _playerDataComponent;

    public ChatComponent() {
    }

    public static void tellAll(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.DARK_GRAY);
        builder.append(ChatColor.BOLD);
        builder.append("Game: ");
        builder.append(message);

        Bukkit.broadcastMessage(builder.toString());
    }

    public static void tell(Player player, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.DARK_GRAY);
        builder.append(ChatColor.BOLD);
        builder.append("Game: ");
        builder.append(message);

        player.sendMessage(builder.toString());
    }

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        PlayerData playerData = null;
        if (_playerDataComponent != null)
            playerData = _playerDataComponent.getPlayerData(event.getPlayer());

        StringBuilder builder = new StringBuilder(ChatColor.GRAY.toString());
        builder.append(ChatColor.BOLD);

        builder.append(playerData == null ? "N/A" : playerData.getData("Exp"));

        builder.append(ChatColor.RESET).append(ChatColor.DARK_BLUE);
        builder.append(" %s").append(ChatColor.GRAY).append(" %s");

        event.setFormat(builder.toString());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        StringBuilder joinMessage = new StringBuilder(ChatColor.DARK_GRAY.toString()).append("Joined> ");
        joinMessage.append(ChatColor.RESET).append(ChatColor.GRAY).append(event.getPlayer().getName());
        event.setJoinMessage(joinMessage.toString());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        StringBuilder quitMessage = new StringBuilder(ChatColor.DARK_GRAY.toString()).append("Quit> ");
        quitMessage.append(ChatColor.RESET).append(ChatColor.GRAY).append(event.getPlayer().getName());
        event.setQuitMessage(quitMessage.toString());
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

}
