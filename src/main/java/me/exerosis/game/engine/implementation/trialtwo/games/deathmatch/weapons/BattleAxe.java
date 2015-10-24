package me.exerosis.game.engine.implementation.trialtwo.games.deathmatch.weapons;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;

public class BattleAxe extends Weapon {
    public BattleAxe(Game game, SpectateComponent spectateComponent) {
        super("battleAxe", game, spectateComponent);
    }
}