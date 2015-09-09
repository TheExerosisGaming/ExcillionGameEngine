package me.exerosis.game.engine.componentgame.countdown;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.packet.player.injection.packet.player.PacketPlayer;
import me.exerosis.packet.player.injection.packet.player.display.displayables.Title;
import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.reflection.data.Pair;
import org.bukkit.Bukkit;

public abstract class Countdown extends Component {
    private int _timeLeft = 1;
    private boolean _stopped = true;
    private boolean _paused = false;
    private Title _title;

    public Countdown() {
        _title = new Title(5, "", "", 0, 2, 0);
    }

    //Abstract methods.
    public abstract int getTime();

    public abstract Pair<String, String> mod(int timeLeft);

    //Primary methods

    /**
     * Called every tick.
     */
    @Override
    public void run() {
        if (!_paused)
            _timeLeft--;
        handleDisplay();

        if (_timeLeft > 0)
            return;
        stopTask();
        done();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
                player.setDisplayed(_title, false);
        }, 20);
    }

    /**
     * Called when the time expires.
     */
    public void done() {
    }

    /**
     * Update the title if there is one.
     */
    private void handleDisplay() {
        Pair<String, String> display = mod(_timeLeft);
        if (display != null) {
            _title.setTitle(display.getA());
            _title.setSubtitle(display.getB());
        }
    }

    /**
     * Stop this Countdown DOES NOT RESUME!
     */
    @Override
    public void stopTask() {
        _stopped = true;
        super.stopTask();
    }

    /**
     * Start the count down.
     */
    public void start() {
        _stopped = false;
        _timeLeft = getTime();
        for (PacketPlayer player : PlayerHandler.getOnlinePlayers())
            player.setDisplayed(_title, true);
        startTask(20, 20);
    }

    public void pause(boolean pause) {
        _paused = pause;
    }

    @Override
    public void onDisable() {
        stopTask();
    }

    //Getters and setters

    /**
     * Get the time left on this count down in ticks;
     *
     * @return
     */
    public int getTimeLeft() {
        return _timeLeft;
    }

    /**
     * Set the amount of time before this count down ends in ticks.
     *
     * @param time
     */
    public void setTimeLeft(int time) {
        this._timeLeft = time;
    }

    /**
     * Remove time from this count down in ticks.
     *
     * @param time
     */
    public void addTime(int time) {
        _timeLeft += time;
    }

    /**
     * Add time to this count down in ticks.
     *
     * @param time
     */
    public void removeTime(int time) {
        _timeLeft -= time;
    }

    /**
     * Returns true if the Countdown is stopped.
     *
     * @return
     */
    public boolean isStopped() {
        return _stopped;
    }
}