package me.exerosis.game.engine.componentgame.game.spleef.doublejump;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;

import java.util.LinkedList;

public class DoubleJumpComponentBundle implements ComponentBundle {
    private double _distance;
    private double _hight;
    private double _forwardMult;

    public DoubleJumpComponentBundle(double forwardMult, double hight, double distance) {
        _forwardMult = forwardMult;
        _hight = hight;
        _distance = distance;
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new DoubleJumpComponent(_forwardMult, _hight, _distance));
        compoents.add(new DoubleJumpCooldown());
        return compoents;
    }
}
