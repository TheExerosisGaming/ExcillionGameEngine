package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;

public class CoreGameComponent extends Component {
    @Depend
    private GameFolderManager _gameFolderManager;
    private int _maxPlayers;
    private int _startPlayers;
    private int _endPlayers;
    private String _name;

    public CoreGameComponent() {
    }

    @Override
    public void onEnable() {
        _name = _gameFolderManager.getGameConfigValue("name", String.class);
        _maxPlayers = _gameFolderManager.getGameConfigValue("maxPlayers", Integer.class);
        _startPlayers = _gameFolderManager.getGameConfigValue("startPlayers", Integer.class);
        _endPlayers = _gameFolderManager.getGameConfigValue("endPlayers", Integer.class);
    }

    @Override
    public void onDisable() {
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

    public String getName() {
        return _name;
    }
}