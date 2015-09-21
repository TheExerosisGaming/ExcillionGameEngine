package me.exerosis.game.engine.implementation.old.core.scoreboard;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.CoreGameComponent;
import me.exerosis.game.engine.implementation.old.core.kit.Kit;
import me.exerosis.game.engine.implementation.old.core.kit.KitsComponent;
import me.exerosis.game.engine.implementation.old.core.player.PlayerData;
import me.exerosis.game.engine.implementation.old.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.implementation.old.event.PlayerLeaveEvent;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.old.event.game.playerdata.PlayerDataLoadEvent;
import me.exerosis.game.engine.implementation.old.event.game.playerdata.PlayerDataUpdateEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardComponent extends Component {
    @Depend(false)
    private KitsComponent _kitsComponent;
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private CoreGameComponent _coreComponent;

    private Map<Player, Scoreboard> _scoreboards = new HashMap<Player, Scoreboard>();

    public ScoreboardComponent() {
    }

    public static String blueBold(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.DARK_BLUE);
        return title.append(ChatColor.BOLD).append(text).toString();
    }

    public static String gray(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.GRAY).append(text).toString();
    }

    @EventHandler
    public void onDataTransfer(PlayerDataLoadEvent event) {
        Scoreboard scoreboard = new Scoreboard(blueBold("Excillion"));
        PlayerData data = event.getData();

        scoreboard.addLine(blueBold("Next Game:"), "nextGame");
        scoreboard.addLine(gray(_coreComponent.getName()), "game");

        scoreboard.addBlank();
        scoreboard.addLine(blueBold("Players:"), "players");
        scoreboard.addLine(gray(Arena.getPlayers().size() + "/" + _coreComponent.getStartPlayers()), "playerNum");

        scoreboard.addBlank();
        scoreboard.addLine(blueBold("Entering in:"), "status");
        scoreboard.addLine(gray("Waiting"), "time");

        scoreboard.addBlank();
        scoreboard.addLine(blueBold("Coins:"), "coins");
        scoreboard.addLine(gray(data.getData("Coins", Integer.class)), "coinVal");

        scoreboard.addBlank();
        scoreboard.addLine(blueBold("Exp:"), "exp");
        scoreboard.addLine(gray(data.getData("Exp", Integer.class)), "expVal");


        if (_kitsComponent != null) {
            Kit kit = _kitsComponent.getDefault(event.getPlayer());
            scoreboard.addBlank();
            scoreboard.addLine(blueBold("Kit:"), "kit");
            scoreboard.addLine(gray(kit.getName()), "kitVal");
        }

        _scoreboards.put(event.getPlayer(), scoreboard);
        scoreboard.showTo(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        StringBuilder newLine = new StringBuilder(ChatColor.GRAY.toString());
        newLine.append(_spectateComponent.getNumberPlayers()).append('/');
        newLine.append(_coreComponent.getStartPlayers());

        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboards.get(player);
            if (scoreboard != null)
                scoreboard.editLine(newLine.toString(), "playerNum");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerLeaveEvent event) {
        StringBuilder newLine = new StringBuilder(ChatColor.GRAY.toString());
        newLine.append(_spectateComponent.getNumberPlayers()).append('/');
        newLine.append(_coreComponent.getStartPlayers());

        for (Player player : Arena.getPlayers()) {
            Scoreboard scoreboard = _scoreboards.get(player);
            if (scoreboard != null)
                scoreboard.editLine(newLine.toString(), "playerNum");
        }
    }

    @EventHandler
    public void onPlayerData(PlayerDataUpdateEvent event) {
        if (!_scoreboards.containsKey(event.getPlayer()))
            return;
        Scoreboard scoreboard = _scoreboards.get(event.getPlayer());

        switch (event.getId()) {
            case "Coins":
                scoreboard.editLine(gray(event.getData()), "coinVal");
            case "Exp":
                scoreboard.editLine(gray(event.getData()), "expVal");
            case "Kits":

                Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
                    if (_kitsComponent == null)
                        return;
                    Kit kit = _kitsComponent.getDefault(event.getPlayer());
                    scoreboard.editLine(gray(kit.getName()), "kitVal");
                }, 1);
        }
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME))
            for (Player player : Arena.getPlayers())
                _scoreboards.get(player).editLine(blueBold("Game:"), "nextGame");
    }

    public Scoreboard getScoreboard(Player player) {
        return _scoreboards.get(player);
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}