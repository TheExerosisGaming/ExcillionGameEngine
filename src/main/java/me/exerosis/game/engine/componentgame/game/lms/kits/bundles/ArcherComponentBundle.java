package me.exerosis.game.engine.componentgame.game.lms.kits.bundles;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.game.lms.kits.Archer;
import me.exerosis.game.engine.componentgame.game.lms.weapons.ShortSword;
import me.exerosis.game.engine.componentgame.game.lms.weapons.longbow.LongbowBow;

import java.util.LinkedList;

public class ArcherComponentBundle implements ComponentBundle {

    public ArcherComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new Archer());
        compoents.add(new ShortSword());
        compoents.add(new LongbowBow());
        return compoents;
    }
}
