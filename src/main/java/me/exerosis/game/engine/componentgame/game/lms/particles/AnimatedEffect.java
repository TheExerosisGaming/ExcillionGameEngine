package me.exerosis.game.engine.componentgame.game.lms.particles;

import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;

import java.util.HashSet;
import java.util.Set;

public abstract class AnimatedEffect implements TickListener {
    private Set<Effect> _effects = new HashSet<Effect>();
    private int time = 0;

    public AnimatedEffect() {
        Ticker.registerListener(this);
    }

    public void addEffect(Effect effect) {
        _effects.add(effect);
    }


    @Override
    public void tick() {
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
