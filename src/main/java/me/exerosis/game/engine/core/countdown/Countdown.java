package me.exerosis.game.engine.core.countdown;

import me.exerosis.packet.utils.ticker.ExTask;

import java.util.LinkedList;

//TODO PAUSE
public abstract class Countdown implements Runnable {
    private int _timeLeft = 1;
    private int _time;
    private boolean _paused;
    private LinkedList<CountdownExtension> extensions = new LinkedList<>();

    public Countdown(int time) {
        _time = time;
    }

    public void start() {
        _timeLeft = _time;
        extensions.forEach(e -> e.start(_time));
        ExTask.startTask(this, 20, 20);
    }

    public void stop() {
        extensions.forEach(e -> e.stop(_timeLeft));
        ExTask.stopTask(this);
    }

    public void restart() {
        extensions.forEach(CountdownExtension::restart);
        stop();
        start();
    }

    @Override
    public void run() {
        if (!_paused)
            _timeLeft--;
        extensions.forEach(e -> e.tick(_timeLeft));

        if (_timeLeft == 0) {
            extensions.forEach(CountdownExtension::done);
            stop();
        }
    }

    public boolean isRunning() {
        return ExTask.isRunning(this);
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

    public int getTime() {
        return _time;
    }

    public void addExtension(CountdownExtension extension) {
        extensions.add(extension);
    }

    public LinkedList<CountdownExtension> getExtensions() {
        return extensions;
    }

    public boolean isPaused() {
        return _paused;
    }

    public void setPaused(boolean paused) {
        _paused = paused;
    }
}