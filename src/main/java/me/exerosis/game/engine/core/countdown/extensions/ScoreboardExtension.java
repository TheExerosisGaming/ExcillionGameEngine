package me.exerosis.game.engine.core.countdown.extensions;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.CountdownExtension;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;
import me.exerosis.game.engine.util.TimeUtil;

public class ScoreboardExtension extends CountdownExtension {
    private ScoreboardComponent _scoreboardComponent;

    public ScoreboardExtension(Countdown countdown, Game game, ScoreboardComponent scoreboardComponent) {
        super(game, countdown);
        _scoreboardComponent = scoreboardComponent;
    }

    @Override
    public void tick(int timeLeft) {
        getPlayers().forEach(p -> _scoreboardComponent.getScoreboard(p).editLine(TimeUtil.formatTime(timeLeft), "time"));
    }
}