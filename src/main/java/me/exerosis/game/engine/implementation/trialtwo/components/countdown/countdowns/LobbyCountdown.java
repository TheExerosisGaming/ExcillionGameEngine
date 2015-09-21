package me.exerosis.game.engine.implementation.trialtwo.components.countdown.countdowns;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.CoreGameComponent;
import me.exerosis.game.engine.implementation.old.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.implementation.old.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.implementation.old.core.scoreboard.ScoreboardComponent;
import me.exerosis.game.engine.implementation.old.core.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.core.world.WorldComponent;
import me.exerosis.game.engine.implementation.old.countdown.GameStateCountdown;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import me.exerosis.game.engine.util.TimeUtil;
import me.exerosis.reflection.data.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyCountdown extends GameStateCountdown {
    private SpectateComponent _spectateComponent;
    private GameFolderManager _gameFolderManager;
    private WorldComponent _worldComponent;
    private ScoreboardComponent _scoreboardComponent;
    private CoreGameComponent _coreComponent;

    private int _time;

    public LobbyCountdown() {
    }

    @Override
    public int getTime() {
        return _time;
    }

    @Override
    public GameState getNextGameState() {
        return GameState.PRE_GAME;
    }

    @Override
    public Pair<String, String> mod(int timeLeft) {
        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboardComponent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardComponent.blueBold("Entering in:"), "status");
                scoreboard.editLine(ScoreboardComponent.gray(TimeUtil.formatTime(timeLeft)), "time");
            }
        }

        return Pair.of(Integer.toString(timeLeft), "");
    }

    @Override
    public void done() {
        super.done();
    }

    @Override
    public void start() {
        for (Player player : Arena.getPlayers())
            player.sendMessage(_worldComponent.getMapDsc());
        super.start();
    }


    @Override
    public void onEnable() {
        registerListener(this);
        _time = _gameFolderManager.getGameConfigValue("lobbyCountDownTime", Integer.class);

        if (getArena().getGameState().equals(GameState.LOBBY))
            if (_spectateComponent.getNumberPlayers() >= _coreComponent.getStartPlayers())
                start();

        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }


    //Listeners
    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.LOBBY))
            if (_spectateComponent.getNumberPlayers() >= _coreComponent.getStartPlayers())
                start();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (getArena().getGameState().equals(GameState.LOBBY))
            if (_spectateComponent.getNumberPlayers() >= _coreComponent.getStartPlayers())
                start();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (getArena().getGameState().equals(GameState.LOBBY))
            if (_spectateComponent.getNumberPlayers() <= _coreComponent.getStartPlayers())
                stopTask();
    }
}