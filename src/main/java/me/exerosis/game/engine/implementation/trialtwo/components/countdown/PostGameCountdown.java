package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.extensions.ScoreboardExtension;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.StartGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.TitleExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;

public class PostGameCountdown extends Countdown {
    public PostGameCountdown(Game game, ScoreboardComponent scoreboardComponent) {
        super(game.getGameConfigValue("postGameCountDownTime", Integer.class));
        addExtension(new StartGameStateExtension(this, game, GameState.POST_GAME));
        addExtension(new SetGameStateExtension(this, game, GameState.RESTARTING));
        addExtension(new TitleExtension(this, game));
        addExtension(new ScoreboardExtension(this, game, scoreboardComponent, "time", "status", "Lobby in:"));
    }
}