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
import org.bukkit.entity.Player;

public class PostGameCountdown extends StartGameStateCountdown {
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private ScoreboardCompoent _scoreboardCompoent;
    private int _time;

    public PostGameCountdown() {
    }

    @Override
    public void onEnable() {
        _time = _gameFolderManager.getGameConfigValue("postGameCountDownTime", Integer.class);
        super.onEnable();
    }

    @Override
    public int getTime() {
        return _time;
    }

    @Override
    public GameState getStartGameState() {
        return GameState.POST_GAME;
    }

    @Override
    public GameState getNextGameState() {
        return GameState.RESTARTING;
    }

    @Override
    public void done() {
        super.done();
    }

    @Override
    public Pair<String, String> mod(int timeLeft) {
        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboardCompoent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardCompoent.blueBold("Lobby in:"), "status");
                scoreboard.editLine(ScoreboardCompoent.gray(TimeUtil.formatTime(timeLeft)), "time");
            }
        }
        return Pair.of(Integer.toString(timeLeft), "");
    }
}