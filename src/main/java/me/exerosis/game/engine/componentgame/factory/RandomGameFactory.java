package me.exerosis.game.engine.componentgame.factory;

import me.exerosis.game.engine.componentgame.Game;

import java.util.Random;

public class RandomGameFactory implements GameFactory {
    private Game[] _games;
    private Game _lastGame;
    private Random _random;

    public RandomGameFactory(Game... games) {
        _games = games;
        _random = new Random();
    }

    @Override
    public Game getNextGame() {
        Game newGame = pickRandom();
        while (_lastGame == newGame)
            newGame = pickRandom();
        return _lastGame = newGame;
    }

    private Game pickRandom() {
        return _games[_random.nextInt(_games.length - 1)];
    }
}
