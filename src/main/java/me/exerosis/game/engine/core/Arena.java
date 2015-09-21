package me.exerosis.game.engine.core;

import me.exerosis.component.SystemHolder;
import me.exerosis.component.factory.SystemFactory;
import me.exerosis.game.engine.implementation.trialtwo.components.world.GameFolderManager;

public class Arena extends SystemHolder {
    private final GameFolderManager _gameFolderManager;

    public Arena(SystemFactory factory) {
        super(factory);
        _gameFolderManager = new GameFolderManager();
    }

    public GameFolderManager getManager() {
        return _gameFolderManager;
    }
}