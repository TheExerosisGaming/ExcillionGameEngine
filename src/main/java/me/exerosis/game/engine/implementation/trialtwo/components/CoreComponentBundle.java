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
import me.exerosis.game.engine.implementation.trialtwo.components.player.data.PlayerDataComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.DeathComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.LMSComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.world.WorldComponent;

public class CoreComponentBundle {
    private final CooldownManager _cooldownManager;
    private final PlayerDataComponent _playerDataComponent;
    private final ScoreboardComponent _scoreboardComponent;
    private final LMSComponent _lmsComponent;
    private final WorldComponent _worldComponent;
    private final CoreGameComponent _coreGameComponent;
    private final DeathComponent _deathComponent;
    private final ChatComponent _chatComponent;
    private final EventComponent _eventComponent;
    private final SpawnpointComponent _spawnpointComponent;
    private final PlayerComponent _playerComponent;
    private final SpectateComponent _spectateComponent;
    private final VoidLevelComponent _voidLevelComponent;
    private final LobbyCountdown _lobbyCountdown;
    private final PreGameCountdown _preGameCountdown;
    private final GameCountdown _gameCountdown;
    private final PostGameCountdown _postGameCountdown;

    public CoreComponentBundle(Game game) {
        _worldComponent = new WorldComponent(game);
        _deathComponent = new DeathComponent(game);
        _chatComponent = new ChatComponent(game);
        _eventComponent = new EventComponent(game);
        _cooldownManager = new CooldownManager(game);
        _playerDataComponent = new PlayerDataComponent(game);

        _spawnpointComponent = new SpawnpointComponent(game, _worldComponent);
        _playerComponent = new PlayerComponent(game, _spawnpointComponent);
        _spectateComponent = new SpectateComponent(game, _playerComponent);
        _voidLevelComponent = new VoidLevelComponent(game, _spectateComponent, _worldComponent, _deathComponent);
        _lmsComponent = new LMSComponent(game, _spectateComponent);
        _coreGameComponent = new CoreGameComponent(game, _spectateComponent, _spawnpointComponent);
        _scoreboardComponent = new ScoreboardComponent(game, _spectateComponent);

        //Countdowns
        _lobbyCountdown = new LobbyCountdown(game, _spawnpointComponent, _scoreboardComponent);
        _preGameCountdown = new PreGameCountdown(game, _scoreboardComponent);
        _gameCountdown = new GameCountdown(game, _scoreboardComponent);
        _postGameCountdown = new PostGameCountdown(game, _scoreboardComponent);
        //

        game.addComponent(_worldComponent);
        game.addComponent(_deathComponent);
        game.addComponent(_chatComponent);
        game.addComponent(_eventComponent);
        game.addComponent(_coreGameComponent);
        game.addComponent(_playerDataComponent);
        game.addComponent(_coreGameComponent);
        game.addComponent(_spawnpointComponent);
        game.addComponent(_playerComponent);
        game.addComponent(_spectateComponent);
        game.addComponent(_voidLevelComponent);
        game.addComponent(_lmsComponent);
        game.addComponent(_scoreboardComponent);

        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _lmsComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _voidLevelComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _deathComponent);
        game.addComponent(GameState.PRE_GAME, GameState.RESTARTING, _spectateComponent);
    }

    public PlayerDataComponent getPlayerDataComponent() {
        return _playerDataComponent;
    }

    public ScoreboardComponent getScoreboardComponent() {
        return _scoreboardComponent;
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
        components.add(new LivesComponent());
        components.add(new WinnersComponent());
        components.add(new LMSComponent());
        components.add(new PlayerDataComponent(playerData));
        components.add(new CommandExecutorComponent());
        components.add(new ScoreboardComponent());
        components.add(new RewardComponent());
        components.addAll(new PauseComponentBundle().getComponents());
   */