package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;
import me.exerosis.game.engine.util.ChatColors;
import me.exerosis.game.engine.util.TimeUtil;
import org.bukkit.entity.Player;

public class ScoreboardExtension extends CountdownExtension implements ChatColors {
    private final String _timeID;
    private final String _statusID;
    private final String _status;
    private ScoreboardComponent _scoreboardComponent;

    public ScoreboardExtension(Countdown countdown, Game game, ScoreboardComponent scoreboardComponent, String timeID, String statusID, String status) {
        super(game, countdown);
        _scoreboardComponent = scoreboardComponent;
        _timeID = timeID;
        _statusID = statusID;
        _status = status;
    }

    @Override
    public void tick(int timeLeft) {
        for (Player player : getPlayers()) {
            Scoreboard scoreboard = _scoreboardComponent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(TimeUtil.formatTime(timeLeft), _timeID);
                scoreboard.editLine(boldDarkBlue() + _status, _statusID);
            }
        }
    }
}