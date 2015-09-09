package me.exerosis.game.engine.componentgame;

import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.factory.GameFactory;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class Arena {
    // Sudo game fields.
    private GameState _gameState;

    // Arena fields.
    private Game _currentGame;
    private GameFactory _factory;
    private Plugin _plugin;

    public Arena(Plugin plugin, GameFactory factory) {
        _factory = factory;
        _plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public static List<Player> getPlayers() {
        return Arrays.asList(Bukkit.getOnlinePlayers());
    }

    // Game management.
    public void nextGame() {
        //Ensure
        if (_gameState != GameState.STARTING)
            _gameState = GameState.RESTARTING;

        // Get next.
        this._currentGame = getFactory().getNextGame();

        // Load
        _currentGame.getInstancePool().add(_currentGame);
        _currentGame.getInstancePool().add(_plugin);
        _currentGame.getInstancePool().add(this);
        _currentGame.enableGame();
    }

    public void start() {
        _gameState = GameState.STARTING;
        nextGame();
    }

    public GameState getGameState() {
        return _gameState;
    }

    // Getters and setters.
    public void setGameState(GameState gameState) {
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(_gameState, gameState));
        this._gameState = gameState;
        Bukkit.getPluginManager().callEvent(new PostGameStateChangeEvent(gameState));
    }

    public Game getCurrentGame() {
        return _currentGame;
    }

    public Plugin getPlugin() {
        return _plugin;
    }

    public GameFactory getFactory() {
        return _factory;
    }
}
