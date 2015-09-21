package me.exerosis.game.engine.core.factory;

import me.exerosis.component.factory.SystemFactory;
import me.exerosis.game.engine.core.Game;

public class RotatingGame implements SystemFactory {
    private Game[] _games;
    private int _counter = 0;

    public RotatingGame(Game... games) {
        this._games = games;
    }

    @Override
    public Game getNextGame() {
        if (_counter >= _games.length - 1)
            _counter = 0;
        else
            _counter++;
        return _games[_counter];
    }
}
