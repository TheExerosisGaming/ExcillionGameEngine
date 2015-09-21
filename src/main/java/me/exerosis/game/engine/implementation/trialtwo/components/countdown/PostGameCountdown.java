package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.StartGameStateExtension;
import me.exerosis.game.engine.core.state.GameState;

public class PostGameCountdown extends Countdown {
    public PostGameCountdown(int time, Game game) {
        super(time);
        addExtension(new StartGameStateExtension(this, game, GameState.POST_GAME));
        addExtension(new SetGameStateExtension(this, game, GameState.RESTARTING));
    }
}