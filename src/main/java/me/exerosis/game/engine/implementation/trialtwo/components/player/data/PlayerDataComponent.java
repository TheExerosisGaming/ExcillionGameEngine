
package me.exerosis.game.engine.implementation.trialtwo.components.player.data;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.data.PlayerDataLoadEvent;
import me.exerosis.sql.SQLAPI;
import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.database.table.SQLResult;
import me.exerosis.sql.database.table.SQLRow;
import me.exerosis.sql.event.table.SQLTableOnlineEvent;
import me.exerosis.sql.queue.command.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataComponent extends GameComponent {
    private Map<String, Object> _defaultPlayerData;
    private Map<Player, PlayerData> _playerData = new HashMap<>();
    private MySQLTable _table;

    public PlayerDataComponent(Game game) {
        super(game);
        YamlConfiguration config = game.getConfig("defaultPlayerData.yml");
        _defaultPlayerData = config.getValues(false);
        _table = SQLAPI.TableManager.addTable("exerosis", "mineplexdevtest");
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = _playerData.get(player);
        if (playerData == null)
            _playerData.put(player, newPlayerData(player));
        return _playerData.get(player);
    }

    private PlayerData newPlayerData(Player player) {
        PlayerData playerData = new PlayerData(player, getArena().getEventManager());
        playerData.getData().putAll(_defaultPlayerData);
        return playerData;
    }

    public void loadPlayerData(Player player) {
        GetPlayerDataCommand runner = new GetPlayerDataCommand(player, "Coins:Exp:Kits");
        CommandMaker maker = new CommandMaker("SELECT * FROM {table} WHERE PlayerName = '");
        maker.append(player.getName()).append("'");

        SQLCommand command = new SQLCommand(maker, CommandType.QUERY, CommandPriority.CAN_QUEUE, runner);

        _table.executeCommand(command);
    }

    public void savePlayerData(Player player) {
        PlayerData playerData = getPlayerData(player);

        Map<String, Object> data = new HashMap<>(playerData.getData());
        data.put("PlayerName", player.getName());

        CommandMaker maker = new CommandMaker();
        maker.appendCommand(Type.REPLACE, data, "");

        _table.executeCommand(new SQLCommand(maker, CommandType.UPDATE, CommandPriority.CAN_QUEUE));
    }

    @Override
    public void onEnable() {
        System.out.println("excuting!");
        registerListener();
        getPlayers().forEach(this::loadPlayerData);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        unregisterListener();
        getPlayers().forEach(this::savePlayerData);
        super.onDisable();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        System.out.println("excuting!");
        savePlayerData(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        System.out.println("excuting!");
        loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onSQLComeOnline(SQLTableOnlineEvent event) {
        formattedBroadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "OMG OMG OMG OMG! THE SQL SERVER CAME ONLINE AGAIN! OH BOY! IM SO EXCITED! Oh yea I guess I can back up your player data... oh yea and here is your scoreboard...");
        getPlayers().forEach(this::savePlayerData);
    }

    public class GetPlayerDataCommand implements CommandRunner {
        private static final long serialVersionUID = -8372428967452042111L;
        private final String[] _collumNames;
        private final Player _player;

        public GetPlayerDataCommand(Player player, String collumNames) {
            _player = player;
            _collumNames = collumNames.split(":");
        }

        @Override
        public void run(SQLResult result) {
            if (!_player.isOnline())
                return;
            PlayerData playerData = getPlayerData(_player);
            if (result.size() < 1) {
                savePlayerData(_player);
                return;
            }

            SQLRow row = result.getRow(0);
            for (String collumName : _collumNames) {
                Object value = row.getValue(collumName);
                playerData.setData(collumName, value);
            }

            Bukkit.getScheduler().callSyncMethod(getPlugin(), () -> {
                getGame().getEventManager().callEvent(new PlayerDataLoadEvent(_player, playerData));
                return null;
            });
        }
    }
}