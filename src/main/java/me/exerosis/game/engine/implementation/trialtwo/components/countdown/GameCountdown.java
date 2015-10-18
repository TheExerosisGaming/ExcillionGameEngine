package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.extensions.ScoreboardExtension;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.StartGameStateExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;

public class GameCountdown extends me.exerosis.game.engine.core.countdown.Countdown {
    public GameCountdown(Game game, ScoreboardComponent scoreboardComponent) {
        super(game.getGameConfigValue("gameCountDownTime", Integer.class));
        addExtension(new StartGameStateExtension(this, game, GameState.IN_GAME));
        addExtension(new SetGameStateExtension(this, game, GameState.POST_GAME));
        addExtension(new ScoreboardExtension(this, game, scoreboardComponent, "time", "status", "Ends in:"));
    }
}