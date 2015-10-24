package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.kits.bundles;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.CoreComponentBundle;
import me.exerosis.game.engine.implementation.trialtwo.components.kit.KitsComponent;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.kits.Archer;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.kits.Axeman;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.BattleAxe;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.ShortSword;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.longbow.LongbowBow;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.throwing.axes.ThrowingAxe;
import me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons.throwing.axes.ThrowingAxeCooldown;


public class ArcherComponentBundle {

    public ArcherComponentBundle(Game game, CoreComponentBundle coreComponentBundle, KitsComponent kitsComponent) {
        ShortSword shortSword = new ShortSword(game, coreComponentBundle.getSpectateComponent());
        LongbowBow longbowBow = new LongbowBow(game, coreComponentBundle.getSpectateComponent());
        game.addComponent(new Archer(game, kitsComponent, longbowBow, shortSword));

        ThrowingAxeCooldown throwingAxeCooldown = new ThrowingAxeCooldown(game);
        ThrowingAxe throwingAxe = new ThrowingAxe(game, coreComponentBundle.getSpectateComponent(), throwingAxeCooldown);
        BattleAxe battleAxe = new BattleAxe(game, coreComponentBundle.getSpectateComponent());
        game.addComponent(new Axeman(game, kitsComponent, throwingAxe, battleAxe, kitsComponent));
    }
}