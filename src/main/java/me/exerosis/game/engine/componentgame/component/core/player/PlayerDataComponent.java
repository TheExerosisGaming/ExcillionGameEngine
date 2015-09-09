
package me.exerosis.game.engine.componentgame.component.core.player;

import me.exerosis.SQLAPI;
import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.ChatComponent;
import me.exerosis.game.engine.componentgame.event.game.playerdata.PlayerDataLoadEvent;
import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.database.table.SQLResult;
import me.exerosis.sql.database.table.SQLRow;
import me.exerosis.sql.event.table.SQLTableOnlineEvent;
import me.exerosis.sql.queue.command.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class PlayerDataComponent extends Component {
    private Map<String, Object> _defaultPlayerData = new HashMap<String, Object>();
    private Map<Player, PlayerData> _playerData = new HashMap<Player, PlayerData>();
    private MySQLTable _table;

    public PlayerDataComponent(Map<String, Object> defaultPlayerData) {
        _defaultPlayerData = defaultPlayerData;
        _table = SQLAPI.TableManager.addTable("exerosis", "mineplexdevtest");
        //whoops sry anyone I might have forgot that has the chance of looking at this.. oh sry Lib (ur still my fav "staff memeber")lolthisisagreatpasswordisntitchissslashandordefekslashandorb2
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = _playerData.get(player);
        if (playerData == null)
            _playerData.put(player, newPlayerData(player));
        return _playerData.get(player);
    }

    private PlayerData newPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player);
        playerData.getData().putAll(_defaultPlayerData);
        return playerData;
    }

    public void loadPlayerData(Player player) {
        GetPlayerDataCommand runner = new GetPlayerDataCommand(player.getName(), "Coins:Exp:Kits");
        CommandMaker maker = new CommandMaker("SELECT * FROM {table} WHERE PlayerName = '");
        maker.append(player.getName()).append("'");

        SQLCommand command = new SQLCommand(maker, CommandType.QUERY, CommandPriority.CAN_QUEUE, runner);
        _table.executeCommand(command);
    }

    public void savePlayerData(Player player) {
        PlayerData playerData = getPlayerData(player);

        Map<String, Object> data = new HashMap<String, Object>(playerData.getData());
        data.put("PlayerName", player.getName());

        CommandMaker maker = new CommandMaker();
        maker.appendCommand(Type.REPLACE, data, "");

        _table.executeCommand(new SQLCommand(maker, CommandType.UPDATE, CommandPriority.CAN_QUEUE));
    }

    @Override
    public void onEnable() {
        registerListener(this);
        for (Player player : Arena.getPlayers())
            loadPlayerData(player);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
        for (Player player : Arena.getPlayers())
            savePlayerData(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onSQLComeOnline(SQLTableOnlineEvent event) {
        ChatComponent.tellAll(ChatColor.GREEN.toString() + ChatColor.BOLD + "OMG OMG OMG OMG! THE SQL CAME ONLINE AGAIN! OH BOY! IM SO EXCITED! Oh yea I guess I can back up your player data... oh yea and heres your scoreboard...");
        for (Player player : Arena.getPlayers())
            savePlayerData(player);
    }

    public class GetPlayerDataCommand implements CommandRunner {
        private static final long serialVersionUID = -8372428967452042111L;
        private final String[] _collumNames;
        private String _playerName;

        public GetPlayerDataCommand(final String playerName, final String collumNames) {
            _playerName = playerName;
            _collumNames = collumNames.split(":");
        }

        @Override
        public void run(SQLResult result) {
            Bukkit.getScheduler().callSyncMethod(getPlugin(), new Callable<Event>() {
                @Override
                public Event call() throws Exception {
                    Player player = Bukkit.getPlayer(_playerName);
                    if (!player.isOnline())
                        return null;
                    PlayerData playerData = getGame().getInstancePool().getInstances(PlayerDataComponent.class).get(0).getPlayerData(player);
                    if (result.size() < 1) {
                        savePlayerData(player);
                        return null;
                    }

                    SQLRow row = result.getRow(0);
                    for (String collumName : _collumNames) {
                        Object value = row.getValue(collumName);
                        playerData.setData(collumName, value);
                    }

                    PlayerDataLoadEvent event = new PlayerDataLoadEvent(player, playerData);
                    Bukkit.getPluginManager().callEvent(event);
                    return event;
                }
            });
        }
    }
}

