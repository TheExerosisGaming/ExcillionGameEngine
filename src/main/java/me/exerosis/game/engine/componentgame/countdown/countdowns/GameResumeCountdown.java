package me.exerosis.game.engine.componentgame.countdown.countdowns;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.core.pause.PauseCompoent;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.countdown.Countdown;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseState;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseStateChangeEvent;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.ActionBar;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.reflection.data.Pair;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class GameResumeCountdown extends Countdown {
    @Depend
    private PauseCompoent _pauseCompoent;
    @Depend
    private GameFolderManager _gameFolderManager;

    private ActionBar _bar;

    public GameResumeCountdown() {
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
        _pauseCompoent.setPauseState(PauseState.RESUMED);
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
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}