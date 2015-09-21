package me.exerosis.game.engine.core.countdown;

import me.exerosis.packet.utils.ticker.ExTask;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Countdown implements Runnable {
    private int _timeLeft = 1;
    private int _time;
    private Collection<CountdownExtension> extensions = new ArrayList<>();

    public Countdown(int time) {
        _time = time;
    }

    public int getTime() {
        return _time;
    }

    public int getTimeLeft() {
        return _timeLeft;
    }

    public void start() {
        _timeLeft = _time;
        extensions.forEach(e -> e.start(_time));
        ExTask.startTask(this, 1, 1);
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
        _timeLeft--;
        extensions.forEach(e -> e.tick(_timeLeft));

        if (_timeLeft == 0)
            extensions.forEach(CountdownExtension::done);
    }

    public void addExtension(CountdownExtension extension) {
        extensions.add(extension);
    }

    public Collection<CountdownExtension> getExtensions() {
        return extensions;
    }
}