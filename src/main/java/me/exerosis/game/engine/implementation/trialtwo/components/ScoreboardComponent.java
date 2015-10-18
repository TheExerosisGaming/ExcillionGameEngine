package me.exerosis.game.engine.implementation.trialtwo.components;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.scoreboard.Scoreboard;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.player.data.PlayerData;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.data.PlayerDataLoadEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.data.PlayerDataUpdateEvent;
import me.exerosis.game.engine.util.ChatColors;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardComponent extends GameComponent implements ChatColors {
    private final Integer _startPlayers;
    private SpectateComponent _spectateComponent;
    private Map<Player, Scoreboard> _scoreboards = new HashMap<>();

    public ScoreboardComponent(Game game, SpectateComponent spectateComponent) {
        super(game);
        _spectateComponent = spectateComponent;
        _startPlayers = getGame().getGameConfigValue("startPlayers", Integer.class);
    }


    @EventListener
    public void onDataTransfer(PlayerDataLoadEvent event) {
        Scoreboard scoreboard = new Scoreboard(boldDarkBlue() + "Excillion");
        PlayerData data = event.getData();

        scoreboard.addLine(boldDarkBlue() + "Next Game:", "nextGame");
        scoreboard.addLine(gray() + getGame().getName(), "game");

        scoreboard.addBlank();
        scoreboard.addLine(boldDarkBlue() + "Players:", "players");

        scoreboard.addLine(gray() + (_spectateComponent.isEnabled() ? _spectateComponent.getNumberPlayers() : getPlayers().size()) + "/" + _startPlayers, "playerNum");

        scoreboard.addBlank();
        scoreboard.addLine(boldDarkBlue() + "Entering in:", "status");
        scoreboard.addLine(gray() + "Waiting", "time");

        scoreboard.addBlank();
        scoreboard.addLine(boldDarkBlue() + "Coins:", "coins");
        scoreboard.addLine(gray() + data.getData("Coins", Integer.class), "coinVal");

        scoreboard.addBlank();
        scoreboard.addLine(boldDarkBlue() + "Exp:", "exp");
        scoreboard.addLine(gray() + data.getData("Exp", Integer.class), "expVal");


   /*     if (_kitsComponent != null) {
            Kit kit = _kitsComponent.getDefault(event.getPlayer());
            scoreboard.addBlank();
            scoreboard.addLine(boldDarkBlue() + "Kit:", "kit");
            scoreboard.addLine(gray(kit.getName()), "kitVal");
        }*/

        _scoreboards.put(event.getPlayer(), scoreboard);
        scoreboard.showTo(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        updatePlayerCount();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        updatePlayerCount();
    }

    private void updatePlayerCount() {
        String newLine = gray() + _spectateComponent.getNumberPlayers() + '/' + _startPlayers;

        for (Player player : getPlayers()) {
            Scoreboard scoreboard = _scoreboards.get(player);
            if (scoreboard != null)
                scoreboard.editLine(newLine, "playerNum");
        }
    }

    @EventListener
    public void onPlayerData(PlayerDataUpdateEvent event) {
        if (!_scoreboards.containsKey(event.getPlayer()))
            return;
        Scoreboard scoreboard = _scoreboards.get(event.getPlayer());

        switch (event.getId()) {
            case "Coins":
                scoreboard.editLine(gray() + event.getData(), "coinVal");
            case "Exp":
                scoreboard.editLine(gray() + event.getData(), "expVal");
            /*case "Kits":

                ExTask.runTaskLater(() -> {
                    if (_kitsComponent == null)
                        return;
                    Kit kit = _kitsComponent.getDefault(event.getPlayer());
                    scoreboard.editLine(gray(kit.getName()), "kitVal");
                }, 1);*/
        }
    }

    @EventListener
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME))
            for (Player player : getPlayers())
                _scoreboards.get(player).editLine(boldDarkBlue() + "Game:", "nextGame");
    }

    public Scoreboard getScoreboard(Player player) {
        return _scoreboards.get(player);
    }

    @Override
    public void onEnable() {
        registerListener();
        super.onEnable();
    }
}