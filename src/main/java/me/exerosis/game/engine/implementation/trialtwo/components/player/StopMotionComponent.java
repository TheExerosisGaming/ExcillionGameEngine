package me.exerosis.game.engine.implementation.trialtwo.components.player;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.util.FreezePlayerUtil;

public class StopMotionComponent extends GameComponent {

    public StopMotionComponent(Game game) {
        super(game);
    }

    @Override
    public void onEnable() {
        FreezePlayerUtil.getInstance().setAllFrozen(true, false);
        super.onEnable();

    }

    @Override
    public void onDisable() {
        FreezePlayerUtil.getInstance().setAllFrozen(false, false);
        super.onDisable();
    }
}