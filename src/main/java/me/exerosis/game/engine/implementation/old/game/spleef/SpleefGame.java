package me.exerosis.game.engine.implementation.old.game.spleef;

import me.exerosis.game.engine.implementation.old.core.CoreComponentBundle;
import me.exerosis.game.engine.implementation.old.core.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.core.world.WorldComponent;
import me.exerosis.game.engine.implementation.old.game.spleef.doublejump.DoubleJumpComponentBundle;
import org.bukkit.GameMode;
import org.bukkit.util.Vector;

public class SpleefGame extends Game {

    public SpleefGame() {
        addInstance(new GameFolderManager("spleef"));
        addInstance(new WorldComponent("https://www.dropbox.com/s/xlk4o2hxvjqgaom/ForgottenTemple.zip?dl=1"));
        addInstance(new CoreComponentBundle(GameMode.SURVIVAL, new Vector(2, 66, 0)));
        addInstance(new DoubleJumpComponentBundle(1, 1.5, 2));
        addInstance(new SpleefBlockBreakCompoent());
        addInstance(new SpleefComponent());
    }

    @Override
    public boolean doesFollowDependencyInjection() {
        return false;
    }
}