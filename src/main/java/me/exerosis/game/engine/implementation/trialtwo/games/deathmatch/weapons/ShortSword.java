package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;

public class ShortSword extends Weapon {
    public ShortSword(Game game, SpectateComponent spectateComponent) {
        super("shortSword", game, spectateComponent);
    }
}