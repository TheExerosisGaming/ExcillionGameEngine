package me.exerosis.game.engine.componentgame.game.lms;

import me.exerosis.game.engine.componentgame.Game;
import me.exerosis.game.engine.componentgame.component.core.CoreComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.component.core.world.WorldComponent;
import me.exerosis.game.engine.componentgame.game.lms.corpse.CorpseComponent;
import me.exerosis.game.engine.componentgame.game.lms.kits.bundles.DeathmatchKitComponentBundle;
import org.bukkit.GameMode;

public class DeathmatchGame extends Game {

    public DeathmatchGame() {
        addInstance(new GameFolderManager("deathmatch"));
        addInstance(new WorldComponent("https://www.dropbox.com/s/vbkfazr7t0doi4k/Gladiator%20Arena.zip?dl=1"));
        addInstance(new CoreComponentBundle(GameMode.ADVENTURE));
        addInstance(new CorpseComponent());
        addInstance(new DeathmatchKitComponentBundle());
        addInstance(new PreGameMotionStopComponent());
    }

    @Override
    public boolean doesFollowDependencyInjection() {
        return false;
    }
}