package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;

public class CoreGameComponent extends GameComponent {
    private int _maxPlayers;
    private int _startPlayers;
    private int _endPlayers;

    public CoreGameComponent(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        _maxPlayers = getGame().getFileManager().getGameConfigValue("maxPlayers", Integer.class);
        _startPlayers = getGame().getFileManager().getGameConfigValue("startPlayers", Integer.class);
        _endPlayers = getGame().getFileManager().getGameConfigValue("endPlayers", Integer.class);
        super.onEnable();
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