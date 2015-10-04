package me.exerosis.game.engine.core.factory;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.Game;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class RandomGameFactory extends ConfigurationGameFactory {
    public static final Random RANDOM = new Random();
    private Game _lastGame;

    @SafeVarargs
    public RandomGameFactory(Plugin plugin, Arena arena, Class<? extends Game>... gameClasses) {
        super(plugin, arena, gameClasses);
    }

    @Override
    public Game getNextGame() {
        if (getGames().size() == 1)
            return getGames().get(0);
        Game newGame = pickRandom();
        while (_lastGame.equals(newGame))
            newGame = pickRandom();
        return _lastGame = newGame;
    }

    private Game pickRandom() {
        if (getGames().size() > 0)
            return getGames().get(RANDOM.nextInt(getGames().size()));
        return null;
    }
}