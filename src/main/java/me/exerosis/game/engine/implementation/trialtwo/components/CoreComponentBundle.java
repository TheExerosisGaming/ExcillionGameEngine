package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.component.Component;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.PlayerComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.WinnersComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;
import org.bukkit.GameMode;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CoreComponentBundle extends GameComponent {
    public CoreComponentBundle(Game game) {
        super(game);
    }


    @Override
    public Collection<Component> getSubComponents() {
        LinkedList<Component> components = new LinkedList<>();
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("Coins", 100);
        playerData.put("Exp", 200);
        playerData.put("Kits", "D:N:N");

        //No Dependencies
        CoreGameComponent coreGameComponent = new CoreGameComponent(getGame());
        WorldComponent worldComponent = new WorldComponent(getGame());
        DeathComponent deathComponent = new DeathComponent(getGame());
        ChatComponent chatComponent = new ChatComponent(getGame());
        EventComponent eventComponent = new EventComponent(getGame());

        //Dependency
        SpawnpointComponent spawnpointComponent = new SpawnpointComponent(getGame(), worldComponent);
        //TODO config gamemode!
        PlayerComponent playerComponent = new PlayerComponent(getGame(), spawnpointComponent, coreGameComponent, GameMode.ADVENTURE);
        SpectateComponent spectateComponent = new SpectateComponent(getGame(), playerComponent);
        VoidLevelComponent voidLevelComponent = new VoidLevelComponent(getGame(), spectateComponent, worldComponent, deathComponent);





        components.add(new LivesComponent());
        components.add(new WinnersComponent());

        components.add(new GameCountdown());
        components.add(new LobbyCountdown());
        components.add(new PostGameCountdown());
        components.add(new PreGameCountdown());

        components.add(new LMSComponent());
        components.add(new PlayerDataComponent(playerData));


        components.add(new CommandExecutorComponent());
        components.add(new ScoreboardComponent());
        components.add(new CooldownManager());
        components.add(new RewardComponent());
        components.addAll(new PauseComponentBundle().getComponents());
        return components;
    }
}
