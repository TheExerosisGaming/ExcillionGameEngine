package me.exerosis.game.engine.implementation.old.game.spleef.abilities;

import me.exerosis.packet.player.injection.packet.player.handlers.PlayerHandler;
import me.exerosis.packet.player.injection.packets.PlayParticle;
import me.exerosis.packet.utils.ticker.TickListener;
import me.exerosis.packet.utils.ticker.Ticker;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class EntityLastingEffect implements TickListener {
    private int _effectTime;
    private Entity _entity;
    private int _id;
    private Vector _velocity;
    private double _speed;
    private double _particlesCount;

    public EntityLastingEffect(Entity entity, int effectTime, int id, Vector velocity, int speed, int particlesCount) {
        _entity = entity;
        _effectTime = effectTime;
        _id = id;
        _velocity = velocity;
        _speed = speed;
        _particlesCount = particlesCount;
        Ticker.registerListener(this);
    }

    public int getEffectTime() {
        return _effectTime;
    }

    public void setEffectTime(int effectTime) {
        _effectTime = effectTime;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public double getParticlesCount() {
        return _particlesCount;
    }

    public void setParticlesCount(double particlesCount) {
        _particlesCount = particlesCount;
    }

    public Entity getEntity() {
        return _entity;
    }

    public void setEntity(Entity entity) {
        _entity = entity;
    }

    public Vector getVelocity() {
        return _velocity;
    }

    public void setVelocity(Vector velocity) {
        _velocity = velocity;
    }

    public double getSpeed() {
        return _speed;
    }

    public void setSpeed(double speed) {
        _speed = speed;
    }

    public void mod() {

    }

    @Override
    public void tick() {
        if (_effectTime <= 0)
            Ticker.unregisterListener(this);
        PlayParticle particle = new PlayParticle(_id, _entity.getLocation(), _velocity, (float) _speed, (int) _particlesCount);
        PlayerHandler.sendGlobalPacket(particle);
        mod();
        _effectTime--;
    }
}
