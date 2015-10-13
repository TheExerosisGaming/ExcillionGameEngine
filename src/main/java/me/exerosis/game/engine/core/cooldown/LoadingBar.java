package me.exerosis.game.engine.core.cooldown;

import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.ActionBar;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class LoadingBar implements Runnable {
    private PacketPlayer _player;
    private double _timeLeft;
    private double _fill;
    private double _perTick;
    private ActionBar _actionBar;
    private boolean _paused;
    private String _completeMessage;
    private double _time;

    public LoadingBar(int priority, Player player, String completeMessage, double time) {
        _completeMessage = completeMessage;
        _time = time * 20;
        _timeLeft = _time;

        _player = PlayerHandler.getPlayer(player);
        _perTick = 100D / _time;
        _actionBar = new ActionBar(priority, getDisplayString());
    }

    public void done() {
    }

    public void start() {
        ExTask.startTask(this, 1, 1);
        _player.setDisplayed(_actionBar, true);
    }

    public void stop() {
        ExTask.stopTask(this);
        _player.setDisplayed(_actionBar, false);
    }

    public void restart() {
        _timeLeft = _time;
        start();
    }

    public void mod(double time) {
    }

    @Override
    public void run() {
        if (!_paused)
            _timeLeft--;
        if (_timeLeft >= 0.0) {
            mod(_timeLeft);
            _fill += _perTick;
            _actionBar.setMessage(getDisplayString());
        }
        if (_timeLeft == 0.0)
            done();
        else if (_timeLeft < 0.0 && _timeLeft > -35.0) {
            _actionBar.setMessage(_completeMessage);
        } else if (_timeLeft <= -35.0)
            stop();
    }

    public double getTimeRemaining() {
        return (_timeLeft + 40) / 20;
    }

    public PacketPlayer getPlayer() {
        return _player;
    }

    public ActionBar getActionBar() {
        return _actionBar;
    }

    public String getDisplayString() {
        List<Object> stringList = new ArrayList<>();

        stringList.add(ChatColor.GREEN);
        for (int x = 0; (x < (int) _fill - 1 && x < 100); x++)
            stringList.add("\u258F");

        stringList.add(ChatColor.RED);
        while (stringList.size() < 100)
            stringList.add("\u258F");

        StringBuilder builder = new StringBuilder();
        stringList.forEach(builder::append);

        return builder.toString();
    }

    public void pause(boolean pause) {
        _paused = pause;
    }
}