package me.exerosis.game.engine.implementation.trialtwo.components.countdown.countdowns;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.old.core.pause.PauseComponent;
import me.exerosis.game.engine.implementation.trialtwo.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.countdown.Countdown;
import me.exerosis.game.engine.implementation.old.event.game.pause.PauseState;
import me.exerosis.game.engine.implementation.old.event.game.pause.PauseStateChangeEvent;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.ActionBar;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.reflection.data.Pair;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

public class GameResumeCountdown extends Countdown {
    private PauseComponent _pauseComponent;
    private GameFolderManager _gameFolderManager;
    private ActionBar _bar;

    public GameResumeCountdown(Plugin plugin, Arena arena, Game game, PauseComponent pauseComponent, GameFolderManager gameFolderManager) {
        super(plugin, arena, game);
        _pauseComponent = pauseComponent;
        _gameFolderManager = gameFolderManager;
        _bar = new ActionBar(2, "");
    }

    @Override
    public void pause(boolean pause) {
    }

    @Override
    public void start() {
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_bar, true);
        super.start();
    }

    @Override
    public int getTime() {
        return _gameFolderManager.getConfig("gameConfig.yml").getInt("gameResumeCountdownTime");
    }

    @Override
    public void done() {
        stopTask();
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_bar, false);
        _pauseComponent.setPauseState(PauseState.RESUMED);
    }

    @Override
    public Pair<String, String> mod(int timeLeft) {
        _bar.setMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Game resumes in " + timeLeft + " seconds!");
        if (timeLeft == 0)
            _bar.setMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Game resumed!");
        return null;
    }

    @EventHandler
    public void onPauseStateChange(PauseStateChangeEvent event) {
        if (event.getPauseState().equals(PauseState.RESUMING))
            start();
        else {
            for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
                player.setDisplayed(_bar, false);
            stopTask();
        }
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        super.onDisable();
    }
}