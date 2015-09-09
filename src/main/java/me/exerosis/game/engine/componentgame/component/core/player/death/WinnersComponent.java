package me.exerosis.game.engine.componentgame.component.core.player.death;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.componentgame.event.PlayerLeaveEvent;
import me.exerosis.game.engine.componentgame.event.game.player.PlayerSpectateEvent;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class WinnersComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;

    private Player[] _winners = new Player[3];

    public WinnersComponent() {
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(PlayerSpectateEvent event) {
        reducePlayers(event.getPlayer());
    }

    private void reducePlayers(Player player) {
        if (_spectateComponent.getNumberPlayers() == 3)
            _winners[2] = player;
        else if (_spectateComponent.getNumberPlayers() == 2)
            _winners[1] = player;
        else if (_spectateComponent.getNumberPlayers() == 1)
            _winners[0] = player;
        else if (_spectateComponent.getNumberPlayers() == 0)
            if (_winners[0] != null)
                _winners[0] = player;
    }

    @EventHandler
    public void onLeave(PlayerLeaveEvent event) {
        if (_spectateComponent.getGamePlayers().contains(event.getPlayer()))
            reducePlayers(event.getPlayer());
    }

    @EventHandler
    public void onGameStateChange(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(GameState.PRE_GAME)) {
            if (_spectateComponent.getNumberPlayers() < 4)
                for (int x = 0; x < 3 && x < _winners.length && x < _spectateComponent.getNumberPlayers(); x++)
                    _winners[x] = _spectateComponent.getGamePlayers().get(x);
        } else if (event.getGameState().equals(GameState.POST_GAME) && Arena.getPlayers().size() > 0) {
            for (int x = 0; x < 3 && x < _spectateComponent.getNumberPlayers(); x++)
                _winners[x] = _spectateComponent.getGamePlayers().get(x);

            StringBuilder builder = new StringBuilder(ChatColor.DARK_GRAY.toString());
            builder.append(ChatColor.STRIKETHROUGH);
            builder.append("===========================\n");
            for (int x = 0; x < _winners.length; x++) {
                Player player = _winners[x];
                if (player == null)
                    continue;
                builder.append(ChatColor.RESET);
                builder.append("\n ").append("\n ").append(ChatColor.DARK_RED);

                switch (x) {
                    case 0:
                        builder.append(ChatColor.GREEN);
                        break;
                    case 1:
                        builder.append(ChatColor.GOLD);
                        break;
                }

                builder.append(ChatColor.BOLD).append(x + 1).append(": ");
                builder.append(ChatColor.RESET).append(ChatColor.DARK_BLUE);
                builder.append(player.getName());
            }
            builder.append("\n ").append("\n ").append(ChatColor.DARK_GRAY);
            builder.append(ChatColor.STRIKETHROUGH);
            builder.append("===========================\n");

            for (Player player : Arena.getPlayers())
                player.sendMessage(builder.toString());
        }
    }

    public Player[] getWinners() {
        return _winners;
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