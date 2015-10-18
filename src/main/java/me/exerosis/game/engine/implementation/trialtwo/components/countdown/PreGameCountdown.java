package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.extensions.ScoreboardExtension;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.StartGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.TitleExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.ScoreboardComponent;
import me.exerosis.reflection.data.Pair;

public class PreGameCountdown extends Countdown {
    public PreGameCountdown(Game game, ScoreboardComponent scoreboardComponent) {
        super(game.getGameConfigValue("preGameCountDownTime", Integer.class));
        addExtension(new StartGameStateExtension(this, game, GameState.PRE_GAME));
        addExtension(new SetGameStateExtension(this, game, GameState.IN_GAME));
        addExtension(new ScoreboardExtension(this, game, scoreboardComponent));
        addExtension(new TitleExtension(this, game) {
            @Override
            public Pair<String, String> mod(int time) {
                return Pair.of(time > 1 ? Integer.toString(time) : "GO!", "");
            }
        });
    }
}