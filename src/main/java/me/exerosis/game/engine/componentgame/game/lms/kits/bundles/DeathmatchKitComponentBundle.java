package me.exerosis.game.engine.componentgame.game.lms.kits.bundles;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.component.core.kit.KitsComponent;

import java.util.LinkedList;

public class DeathmatchKitComponentBundle implements ComponentBundle {

    public DeathmatchKitComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new KitsComponent());
        compoents.addAll(new ArcherComponentBundle().getComponents());
        compoents.addAll(new AxemanComponentBundle().getComponents());
        compoents.addAll(new MageComponentBundle().getComponents());
        return compoents;
    }
}
