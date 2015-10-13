package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.cooldown.CooldownManager;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.GameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.LobbyCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.PostGameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.countdown.PreGameCountdown;
import me.exerosis.game.engine.implementation.trialtwo.components.player.PlayerComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.SpawnpointComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.LMSComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;

public class CoreComponentBundle {
    private final CooldownManager _cooldownManager;
    private LMSComponent _lmsComponent;
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
        _coreGameComponent = new CoreGameComponent(game);
        _worldComponent = new WorldComponent(game);
        _deathComponent = new DeathComponent(game);
        _chatComponent = new ChatComponent(game);
        _eventComponent = new EventComponent(game);
        _cooldownManager = new CooldownManager(game);

        _spawnpointComponent = new SpawnpointComponent(game, _worldComponent);
        _playerComponent = new PlayerComponent(game, _spawnpointComponent, _coreGameComponent);
        _spectateComponent = new SpectateComponent(game, _playerComponent);
        _voidLevelComponent = new VoidLevelComponent(game, _spectateComponent, _worldComponent, _deathComponent);
        _lmsComponent = new LMSComponent(game, _spectateComponent, _coreGameComponent, _spawnpointComponent);

        game.addComponent(_coreGameComponent);
        game.addComponent(_worldComponent);
        game.addComponent(_chatComponent);
        game.addComponent(_eventComponent);
        game.addComponent(_spawnpointComponent);
        game.addComponent(_playerComponent);
        game.addComponent(_cooldownManager);

        _lobbyCountdown = new LobbyCountdown(game, _coreGameComponent, _spawnpointComponent);
        _preGameCountdown = new PreGameCountdown(game);
        _gameCountdown = new GameCountdown(game);
        _postGameCountdown = new PostGameCountdown(game);

        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _lmsComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _voidLevelComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _deathComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _spectateComponent);
    }

    public CooldownManager getCooldownManager() {
        return _cooldownManager;
    }

    public LMSComponent getLMSComponent() {
        return _lmsComponent;
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

        components.add(new RewardComponent());
        components.addAll(new PauseComponentBundle().getComponents());*/