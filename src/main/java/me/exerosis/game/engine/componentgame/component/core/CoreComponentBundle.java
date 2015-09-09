package me.exerosis.game.engine.componentgame.component.core;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.command.CommandExecutorComponent;
import me.exerosis.game.engine.componentgame.component.core.cooldown.CooldownManager;
import me.exerosis.game.engine.componentgame.component.core.pause.PauseComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerComponent;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerDataComponent;
import me.exerosis.game.engine.componentgame.component.core.player.death.*;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.scoreboard.ScoreboardCompoent;
import me.exerosis.game.engine.componentgame.countdown.countdowns.GameCountdown;
import me.exerosis.game.engine.componentgame.countdown.countdowns.LobbyCountdown;
import me.exerosis.game.engine.componentgame.countdown.countdowns.PostGameCountdown;
import me.exerosis.game.engine.componentgame.countdown.countdowns.PreGameCountdown;
import org.bukkit.GameMode;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CoreComponentBundle implements ComponentBundle {
    private Vector _spawnPoint;
    private GameMode _defaultGM;

    public CoreComponentBundle(GameMode defaultGM, Vector spawnPoint) {
        _defaultGM = defaultGM;
        _spawnPoint = spawnPoint;
    }

    public CoreComponentBundle(GameMode defaultGM) {
        _defaultGM = defaultGM;
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        Map<String, Object> playerData = new HashMap<String, Object>();
        playerData.put("Coins", 100);
        playerData.put("Exp", 200);
        playerData.put("Kits", "D:N:N");

        compoents.add(new CoreGameComponent());
        compoents.addAll(new SpectateComponentBundle().getComponents());
        compoents.add(new DeathComponent());
        compoents.add(new LivesComponent());
        compoents.add(new WinnersComponent());
        compoents.add(new GameCountdown());
        compoents.add(new LobbyCountdown());
        compoents.add(new PostGameCountdown());
        compoents.add(new PreGameCountdown());
        compoents.add(new LMSComponent());
        compoents.add(new PlayerDataComponent(playerData));
        compoents.add(new VoidLevelComponent());
        compoents.add(new PlayerComponent(_defaultGM));
        compoents.add(new SpawnpointComponent(_spawnPoint));
        compoents.add(new CommandExecutorComponent());
        compoents.add(new ChatComponent());
        compoents.add(new EventComponent());
        compoents.add(new ScoreboardCompoent());
        compoents.add(new CooldownManager());
        compoents.add(new RewardComponent());
        compoents.addAll(new PauseComponentBundle().getComponents());
        return compoents;
    }
}
