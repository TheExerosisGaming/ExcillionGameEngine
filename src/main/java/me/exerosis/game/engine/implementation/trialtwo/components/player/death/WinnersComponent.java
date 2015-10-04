package me.exerosis.game.engine.implementation.trialtwo.components.player.death;

import me.exerosis.component.event.EventListener;
import me.exerosis.component.event.Priority;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.StateComponent;
import me.exerosis.game.engine.core.state.GameLocation;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.PlayerSpectateEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class WinnersComponent extends StateComponent {
    private SpectateComponent _spectateComponent;
    private Player[] _winners = new Player[3];

    public WinnersComponent(Game game, SpectateComponent spectateComponent) {
        super(game, GameLocation.GAME_WORLD);
        _spectateComponent = spectateComponent;
    }

    @EventListener(priority = Priority.HIGHEST)
    public void onDeath(PlayerSpectateEvent event) {
        reducePlayers(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (!_spectateComponent.getSpectatingPlayers().contains(event.getPlayer()))
            reducePlayers(event.getPlayer());
    }

    @EventListener(postEvent = true)
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME)) {
            if (_spectateComponent.getNumberPlayers() < 4)
                for (int x = 0; x < 3 && x < _winners.length && x < _spectateComponent.getNumberPlayers(); x++)
                    _winners[x] = _spectateComponent.getGamePlayers().get(x);
        }
        else if (event.getNewGameState().equals(GameState.POST_GAME) && getPlayers().size() > 0) {
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

            broadcast(builder.toString());
        }
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

    public Player[] getWinners() {
        return _winners;
    }
}