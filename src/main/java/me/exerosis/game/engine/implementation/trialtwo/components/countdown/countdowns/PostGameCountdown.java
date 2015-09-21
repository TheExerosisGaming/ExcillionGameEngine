package me.exerosis.game.engine.implementation.trialtwo.components.countdown.countdowns;

import me.exerosis.game.engine.core.GameState;
import me.exerosis.game.engine.implementation.old.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.implementation.old.core.scoreboard.ScoreboardComponent;
import me.exerosis.game.engine.implementation.old.core.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.countdown.StartGameStateCountdown;
import me.exerosis.game.engine.util.TimeUtil;
import me.exerosis.reflection.data.Pair;
import org.bukkit.entity.Player;

public class PostGameCountdown extends StartGameStateCountdown {
    private GameFolderManager _gameFolderManager;
    private ScoreboardComponent _scoreboardComponent;
    private int _time;

    public PostGameCountdown(GameFolderManager gameFolderManager, ScoreboardComponent scoreboardComponent) {
        _gameFolderManager = gameFolderManager;
        _scoreboardComponent = scoreboardComponent;
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
        for (Player player : getPlayers()) {
            Scoreboard scoreboard = _scoreboardComponent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardComponent.blueBold("Lobby in:"), "status");
                scoreboard.editLine(ScoreboardComponent.gray(TimeUtil.formatTime(timeLeft)), "time");
            }
        }
        return Pair.of(Integer.toString(timeLeft), "");
    }
}