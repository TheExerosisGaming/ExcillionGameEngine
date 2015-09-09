package me.exerosis.game.engine.componentgame.countdown.countdowns;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.core.CoreGameComponent;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.component.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.componentgame.component.core.scoreboard.ScoreboardCompoent;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.component.core.world.WorldComponent;
import me.exerosis.game.engine.componentgame.countdown.GameStateCountdown;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import me.exerosis.game.engine.util.TimeUtil;
import me.exerosis.reflection.data.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyCountdown extends GameStateCountdown {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private WorldComponent _worldComponent;
    @Depend
    private ScoreboardCompoent _scoreboardCompoent;
    @Depend
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
            Scoreboard scoreboard = _scoreboardCompoent.getScoreboard(player);
            if (scoreboard != null) {
                scoreboard.editLine(ScoreboardCompoent.blueBold("Entering in:"), "status");
                scoreboard.editLine(ScoreboardCompoent.gray(TimeUtil.formatTime(timeLeft)), "time");
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