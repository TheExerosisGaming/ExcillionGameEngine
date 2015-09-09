package me.exerosis.game.engine.componentgame.factory;

import me.exerosis.game.engine.componentgame.Game;

public class RotatingGame implements GameFactory {
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
