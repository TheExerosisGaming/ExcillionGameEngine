package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.StateComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;
import org.bukkit.entity.Player;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class VoidLevelComponent extends StateComponent {
    private SpectateComponent _spectateComponent;
    private WorldComponent _worldComponent;
    private DeathComponent _deathComponent;
    private int _voidLevel;

    public VoidLevelComponent(Game game, SpectateComponent spectateComponent, WorldComponent worldComponent, DeathComponent deathComponent) {
        super(game, GameLocation.GAME_WORLD);
        _spectateComponent = spectateComponent;
        _worldComponent = worldComponent;
        _deathComponent = deathComponent;
    }

    @Override
    public void run() {
        TreeSet<Player> toKill = new TreeSet<>((o1, o2) -> Double.compare(o1.getLocation().getY(), o2.getLocation().getY()));
        toKill.addAll(_spectateComponent.getGamePlayers().stream().filter(p -> p.getLocation().getY() <= _voidLevel).collect(Collectors.toList()));
        toKill.forEach(_deathComponent::kill);
    }

    @Override
    public void onEnable() {
        _voidLevel = _worldComponent.getMapDataValue("voidLevel", Integer.class);
        startTask(1, 1);
    }

    @Override
    public void onDisable() {
        stopTask();
    }
}