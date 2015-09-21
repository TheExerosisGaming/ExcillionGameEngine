package me.exerosis.game.engine.implementation.trialtwo.components.countdown.countdowns;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.event.game.GameDisableEvent;
import me.exerosis.packet.player.injection.packet.player.display.displayables.Title;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class DeadPlayerCountdown implements Runnable, Listener {
    int time = 15;
    @Depend
    private LivesComponent _livesComponent;
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private Plugin plugin;
    private Player _player;
    private Title _title;


    public DeadPlayerCountdown(Player player, Game game) {
        _player = player;
        game.getInstancePool().setFields(this);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        ExTask.startTask(this, 1, 1);
    }

    @EventHandler
    public void onDisable(GameDisableEvent event) {
        ExTask.stopTask(this);
    }

    public void done() {
        int lives = _livesComponent.getLives(_player);

        StringBuilder chatMessageBuilder = new StringBuilder();
        chatMessageBuilder.append(ChatColor.GREEN);
        chatMessageBuilder.append(ChatColor.BOLD);
        chatMessageBuilder.append(_player.getName());
        chatMessageBuilder.append(" respawned, they have ");
        chatMessageBuilder.append(lives);
        chatMessageBuilder.append(" lives left!");

        ChatComponent.tellAll(chatMessageBuilder.toString());

        StringBuilder titleMessageBuilder = new StringBuilder();
        titleMessageBuilder.append(lives);
        titleMessageBuilder.append(" lives remaing, good luck!");

        String titleMessage = lives > 1 ? titleMessageBuilder.toString() : "Last life, good luck!";

        _title = new Title(5, "", titleMessage, 5, 20, 5);
        PlayerHandler.getPlayer(_player).setDisplayed(_title, true);

        _spectateComponent.removeSpectating(_player);
    }

    @Override
    public void run() {
        time--;
        if (time == 0) {
            done();
            ExTask.stopTask(this);
            PlayerHandler.getPlayer(_player).setDisplayed(_title, false);
        }
    }
}
