package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.component.Component;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.GameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.LobbyCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.PostGameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.PreGameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.player.PlayerComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;

import java.util.Collection;
import java.util.LinkedList;

public class CoreComponentBundle extends GameComponent {

    private WorldComponent _worldComponent;
    private CoreGameComponent _coreGameComponent;
    private DeathComponent _deathComponent;
    private ChatComponent _chatComponent;
    private EventComponent _eventComponent;
    private SpawnpointComponent _spawnpointComponent;
    private PlayerComponent _playerComponent;
    private SpectateComponent _spectateComponent;
    private VoidLevelComponent _voidLevelComponent;
    private LobbyCountdown _lobbyCountdown;
    private PreGameCountdown _preGameCountdown;
    private GameCountdown _gameCountdown;
    private PostGameCountdown _postGameCountdown;

    public CoreComponentBundle(Game game) {
        super(game);
    }


    @Override
    public Collection<Component> getSubComponents() {
        LinkedList<Component> components = new LinkedList<>();

        //No Dependencies
        _coreGameComponent = new CoreGameComponent(getGame());
        _worldComponent = new WorldComponent(getGame());
        _deathComponent = new DeathComponent(getGame());
        _chatComponent = new ChatComponent(getGame());
        _eventComponent = new EventComponent(getGame());

        components.add(_coreGameComponent);
        components.add(_worldComponent);
        components.add(_deathComponent);
        components.add(_chatComponent);
        components.add(_eventComponent);

        //Dependency
        _spawnpointComponent = new SpawnpointComponent(getGame(), _worldComponent);
        //TODO config gamemode!
        _playerComponent = new PlayerComponent(getGame(), _spawnpointComponent, _coreGameComponent);
        _spectateComponent = new SpectateComponent(getGame(), _playerComponent);
        _voidLevelComponent = new VoidLevelComponent(getGame(), _spectateComponent, _worldComponent, _deathComponent);

        components.add(_spawnpointComponent);
        components.add(_playerComponent);
        components.add(_spectateComponent);
        components.add(_voidLevelComponent);

        //Countdowns
        _lobbyCountdown = new LobbyCountdown(getGame(), _coreGameComponent, _spawnpointComponent);
        _preGameCountdown = new PreGameCountdown(10, getGame());
        _gameCountdown = new GameCountdown(10, getGame());
        _postGameCountdown = new PostGameCountdown(10, getGame());

        components.addAll(_lobbyCountdown.getExtensions());
        components.addAll(_preGameCountdown.getExtensions());
        components.addAll(_gameCountdown.getExtensions());
        components.addAll(_postGameCountdown.getExtensions());
    /*
           Map<String, Object> playerData = new HashMap<>();
        playerData.put("Coins", 100);
        playerData.put("Exp", 200);
        playerData.put("Kits", "D:N:N");

     components.add(new LivesComponent());
        components.add(new WinnersComponent());


        components.add(new LMSComponent());
        components.add(new PlayerDataComponent(playerData));

        components.add(new CommandExecutorComponent());
        components.add(new ScoreboardComponent());
        components.add(new CooldownManager());
        components.add(new RewardComponent());
        components.addAll(new PauseComponentBundle().getComponents());*/
        return components;
    }

    public WorldComponent getWorldComponent() {
        return _worldComponent;
    }

    public CoreGameComponent getCoreGameComponent() {
        return _coreGameComponent;
    }

    public DeathComponent getDeathComponent() {
        return _deathComponent;
    }

    public ChatComponent getChatComponent() {
        return _chatComponent;
    }

    public EventComponent getEventComponent() {
        return _eventComponent;
    }

    public SpawnpointComponent getSpawnpointComponent() {
        return _spawnpointComponent;
    }

    public PlayerComponent getPlayerComponent() {
        return _playerComponent;
    }

    public SpectateComponent getSpectateComponent() {
        return _spectateComponent;
    }

    public VoidLevelComponent getVoidLevelComponent() {
        return _voidLevelComponent;
    }

    public LobbyCountdown getLobbyCountdown() {
        return _lobbyCountdown;
    }

    public PreGameCountdown getPreGameCountdown() {
        return _preGameCountdown;
    }

    public GameCountdown getGameCountdown() {
        return _gameCountdown;
    }

    public PostGameCountdown getPostGameCountdown() {
        return _postGameCountdown;
    }
}
