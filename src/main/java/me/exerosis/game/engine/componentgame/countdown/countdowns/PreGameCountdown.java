package me.exerosis.game.engine.componentgame.countdown.countdowns;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.componentgame.component.core.scoreboard.ScoreboardCompoent;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.countdown.StartGameStateCountdown;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import me.exerosis.game.engine.util.TimeUtil;
import me.exerosis.reflection.data.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PreGameCountdown extends StartGameStateCountdown {
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private ScoreboardCompoent _scoreboardCompoent;
    private int _time;

    public PreGameCountdown() {
    }

    @Override
    public void onEnable() {
        _time = _gameFolderManager.getGameConfigValue("preGameCountDownTime", Integer.class);
        super.onEnable();
    }

    @Override
    public int getTime() {
        return _time;
    }

    @Override
    public Pair<String, String> mod(int timeLeft) {
        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboardCompoent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardCompoent.blueBold("Starts in:"), "status");
                scoreboard.editLine(ScoreboardCompoent.gray(TimeUtil.formatTime(timeLeft)), "time");
            }
        }

        if (timeLeft == 0)
            return Pair.of("GO!", "");
        return Pair.of(Integer.toString(timeLeft), "");
    }

    @Override
    public void start() {
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> PreGameCountdown.super.start(), 21);
    }

    @Override
    public GameState getStartGameState() {
        return GameState.PRE_GAME;
    }

    @Override
    public GameState getNextGameState() {
        return GameState.IN_GAME;
    }

    @Override
    public void done() {
        super.done();
    }
}
