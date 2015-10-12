package me.exerosis.game.engine.core.countdown;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;

public class CountdownExtension extends GameComponent {
    private Countdown _countdown;

    public CountdownExtension(Game game, Countdown countdown) {
        super(game);
        _countdown = countdown;
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }

    public Countdown getCountdown() {
        return _countdown;
    }


    public void restart() {

    }

    public void tick(int timeLeft) {

    }

    public void start(int time) {

    }

    public void stop(int index) {
        unregisterListener();
    }
}