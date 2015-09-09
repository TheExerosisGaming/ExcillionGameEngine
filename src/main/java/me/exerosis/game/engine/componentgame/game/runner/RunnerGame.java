package me.exerosis.game.engine.componentgame.game.runner;

import me.exerosis.game.engine.componentgame.Game;
import me.exerosis.game.engine.componentgame.component.core.CoreComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.component.core.world.WorldComponent;
import org.bukkit.GameMode;
import org.bukkit.util.Vector;

public class RunnerGame extends Game {

    public RunnerGame() {
        addInstance(new GameFolderManager("runner"));
        addInstance(new WorldComponent("https://www.dropbox.com/s/mkf3apedufoy1rd/Clay%20Plaza.zip?dl=1"));
        addInstance(new CoreComponentBundle(GameMode.ADVENTURE, new Vector(0, 26, 0)));
        addInstance(new BlockDropComponent(3));
        addInstance(new AntiCheat());
    }

    @Override
    public boolean doesFollowDependencyInjection() {
        return false;
    }
}