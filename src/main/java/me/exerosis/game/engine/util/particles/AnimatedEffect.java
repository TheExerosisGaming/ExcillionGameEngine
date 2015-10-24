package me.exerosis.game.engine.util.particles;

import me.exerosis.packet.utils.ticker.ExTask;

import java.util.HashSet;
import java.util.Set;

public abstract class AnimatedEffect implements Runnable {
    private Set<Effect> _effects = new HashSet<>();
    private int time = 0;

    public AnimatedEffect() {
        ExTask.startTask(this, 1, 1);
    }

    public void addEffect(Effect effect) {
        _effects.add(effect);
    }

    @Override
    public void run() {
        time++;
        for (Effect effect : _effects)
            effect.tick(time);
    }

    public int getTime() {
        return time;
    }

    public Set<Effect> getEffects() {
        return _effects;
    }


    public abstract static class Effect {
        private int _tick;

        public Effect(int tick) {
            _tick = tick;
        }

        public void tick(int time) {
            if (_tick == time)
                effect();
        }

        public abstract void effect();
    }
}