package me.exerosis.game.engine.implementation.old.game.lms.kits.bundles;

import me.exerosis.game.engine.implementation.old.game.lms.kits.Axeman;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.BattleAxe;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.throwing.axes.ThrowingAxe;
import me.exerosis.game.engine.implementation.old.game.lms.weapons.throwing.axes.ThrowingAxeCooldown;

import java.util.LinkedList;

public class AxemanComponentBundle implements ComponentBundle {

    public AxemanComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new Axeman());
        compoents.add(new BattleAxe());
        compoents.add(new ThrowingAxeCooldown());
        compoents.add(new ThrowingAxe());
        return compoents;
    }
}