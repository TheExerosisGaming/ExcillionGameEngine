package me.exerosis.game.engine.implementation.trialtwo.components.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.countdown.Countdown;
import me.exerosis.game.engine.core.countdown.extensions.SetGameStateExtension;
import me.exerosis.game.engine.core.countdown.extensions.TitleExtension;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.reflection.data.Pair;

public class LobbyCountdown extends Countdown {
    public LobbyCountdown(int time, Game game) {
        super(time);
        addExtension(new SetGameStateExtension(this, game, GameState.PRE_GAME));
        addExtension(new TitleExtension(this, game) {
            @Override
            public Pair<String, String> mod(int time) {
                return Pair.of(Integer.toString(time), "");
            }
        });
    }
}