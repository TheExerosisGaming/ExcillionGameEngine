package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class CoreGameComponent extends GameComponent {
    private int _maxPlayers;
    private int _startPlayers;
    private int _endPlayers;

    public CoreGameComponent(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        registerListener();
        _maxPlayers = getGame().getGameConfigValue("maxPlayers", Integer.class);
        _startPlayers = getGame().getGameConfigValue("startPlayers", Integer.class);
        _endPlayers = getGame().getGameConfigValue("endPlayers", Integer.class);
        super.onEnable();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (getPlayers().size() - 1 <= _endPlayers)
            setGameState(GameState.POST_GAME);
        if (getPlayers().size() - 1 == 0)
            setGameState(GameState.RESTARTING);
    }

    public int getMaxPlayers() {
        return _maxPlayers;
    }

    public int getStartPlayers() {
        return _startPlayers;
    }

    public int getEndPlayers() {
        return _endPlayers;
    }


}