package me.exerosis.game.engine.componentgame.component.core.player.death;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerData;
import me.exerosis.game.engine.componentgame.component.core.player.PlayerDataComponent;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerKilledEvent;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.game.lms.DeathmatchGame;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class RewardComponent extends Component {
    private Map<Player, Integer> _kills = new HashMap<Player, Integer>();
    @Depend
    private GameFolderManager _gameFolderManager;
    @Depend
    private WinnersComponent _winnersComponent;
    @Depend
    private PlayerDataComponent _playerDataComponent;

    private int _killExpValue, _killCoinsValue;
    private int _firstPlaceCoinsValue, _secondPlaceCoinsValue, _thirdPlaceCoinsValue;
    private int _firstPlaceExpValue, _secondPlaceExpValue, _thirdPlaceExpValue;
    private int _playingCoinsValue, _playingExpValue;
    private int _rankExpMult, _rankCoinsMult;

    public RewardComponent() {
    }

    public static String blueBold(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.DARK_BLUE).append(ChatColor.BOLD).append(text).toString();
    }

    public static String gray(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.GRAY).append(text).toString();
    }

    public static String grayBold(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(text).toString();
    }

    public static String dark(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.DARK_GRAY).append(text).toString();
    }

    public static String blue(Object text) {
        StringBuilder title = new StringBuilder().append(ChatColor.RESET);
        return title.append(ChatColor.DARK_BLUE).append(text).toString();
    }

    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (!event.getGameState().equals(GameState.POST_GAME))
            return;
        for (Player player : Arena.getPlayers()) {
            int allNewExp = 0;
            int allNewCoins = 0;

            StringBuilder message = new StringBuilder();
            PlayerData data = _playerDataComponent.getPlayerData(player);

            if (getGame() instanceof DeathmatchGame) {
                int kills = 0;
                if (_kills.containsKey(player))
                    kills = _kills.get(player).intValue();
                int killExp = (kills * _killExpValue);
                int killCoins = (kills * _killCoinsValue);

                allNewCoins += killCoins;
                allNewExp += killExp;

                message.append(grayBold("--------") + blueBold("Kills") + grayBold("---------@"));
                message.append(blue("Exp ") + dark("+ "));
                message.append(gray(killExp));

                message.append(blue("@Coins ") + dark("+ "));
                message.append(gray(killCoins));

                message.append("@(For ");
                message.append(kills);
                message.append(" kill");
                message.append((kills > 1 ? "s." : "."));
                message.append(")@");
            }

            Player[] winners = _winnersComponent.getWinners();

            message.append(grayBold("-------") + blueBold("Placing") + grayBold("--------@"));

            String placing = gray("You did not place, try agian next game!@");
            outer:
            for (int x = 0; x < 3; x++) {
                if (winners[x] != null && winners[x].equals(player)) {
                    StringBuilder builder = new StringBuilder();
                    int expValue = 0;
                    int coinsValue = 0;
                    String placeName = "";

                    switch (x) {
                        case 0:
                            placeName = ChatColor.GREEN + "First";
                            expValue = _firstPlaceExpValue;
                            coinsValue = _firstPlaceCoinsValue;
                            break;
                        case 1:
                            placeName = ChatColor.YELLOW + "Second";
                            expValue = _secondPlaceExpValue;
                            coinsValue = _secondPlaceCoinsValue;
                            break;
                        case 2:
                            placeName = ChatColor.GOLD + "Third";
                            expValue = _thirdPlaceExpValue;
                            coinsValue = _thirdPlaceCoinsValue;
                            break;
                    }

                    allNewCoins += coinsValue;
                    allNewExp += expValue;

                    builder.append(blue("Exp ") + dark("+ "));
                    builder.append(gray(expValue));

                    builder.append(blue("@Coins ") + dark("+ "));
                    builder.append(gray(coinsValue));

                    builder.append("@(For ");
                    builder.append(placeName);
                    builder.append(gray(" Place!)@"));
                    placing = builder.toString();
                    break outer;
                }
            }

            message.append(placing);

            message.append(grayBold("-------") + blueBold("Playing") + grayBold("--------@"));
            message.append(blue("Exp ") + dark("+ "));
            message.append(gray(_playingExpValue));

            message.append(blue("@Coins ") + dark("+ "));
            message.append(gray(_playingCoinsValue));

            message.append("@(For playing, thanks!)");

            allNewCoins += _playingCoinsValue;
            allNewExp += _playingCoinsValue;

            String name = player.getName();
            if (name.equals("Chiss") || name.equals("Exerosis") || name.equals("defek7")) {
                message.append(grayBold("@-----") + blueBold("Rank-Bonus") + grayBold("------@"));
                message.append(blue("Exp ") + grayBold("* "));
                message.append(gray(_rankExpMult));

                message.append(blue("@Coins ") + grayBold("* "));
                message.append(gray(_rankCoinsMult));

                allNewExp *= _rankExpMult;
                allNewCoins *= _rankCoinsMult;
            }
            message.append(grayBold("@-------------------------@"));

            String[] lines = message.toString().split("@");
            player.sendMessage(lines[0]);
            for (int x = 1; x < lines.length; x++)
                player.sendMessage(ChatColor.getLastColors(lines[x - 1]) + lines[x]);

            data.incrementIntBy("Exp", allNewExp);
            data.incrementIntBy("Coins", allNewCoins);
        }
        _kills.clear();
    }

    @EventHandler
    public void onKill(PlayerKilledEvent event) {
        Player player = event.getKiller();
        if (player == null)
            return;
        if (_kills.containsKey(player)) {
            int kills = _kills.remove(player) + 1;
            _kills.put(player, kills);
        } else
            _kills.put(player, 1);
    }

    @Override
    public void onEnable() {
        registerListener(this);
        YamlConfiguration config = _gameFolderManager.getConfig("rewards.yml");

        if (getGame() instanceof DeathmatchGame) {
            _killExpValue = config.getInt("killExpValue");
            _killCoinsValue = config.getInt("killCoinsValue");
        }

        _firstPlaceCoinsValue = config.getInt("firstPlaceCoinsValue");
        _secondPlaceCoinsValue = config.getInt("secondPlaceCoinsValue");
        _thirdPlaceCoinsValue = config.getInt("thirdPlaceCoinsValue");

        _firstPlaceExpValue = config.getInt("firstPlaceExpValue");
        _secondPlaceExpValue = config.getInt("secondPlaceExpValue");
        _thirdPlaceExpValue = config.getInt("thirdPlaceExpValue");

        _playingCoinsValue = config.getInt("playingCoinsValue");
        _playingExpValue = config.getInt("playingExpValue");

        _rankExpMult = config.getInt("rankExpMult");
        _rankCoinsMult = config.getInt("rankCoinsMult");
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}
