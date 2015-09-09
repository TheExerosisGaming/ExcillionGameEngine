package me.exerosis.game.engine.componentgame.event.game;

import org.bukkit.entity.Player;

public class CommandEvent extends GameEvent {

    private String _label;
    private String[] _args;
    private Player _player;

    public CommandEvent(String label, String[] args, Player player) {
        _label = label;
        _args = args;
        _player = player;
    }

    public String[] getArgs() {
        return _args;
    }

    public String getLabel() {
        return _label;
    }

    public Player getPlayer() {
        return _player;
    }
}
