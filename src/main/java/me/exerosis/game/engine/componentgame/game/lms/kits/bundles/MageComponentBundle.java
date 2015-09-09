package me.exerosis.game.engine.componentgame.game.lms.kits.bundles;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.game.lms.kits.Mage;
import me.exerosis.game.engine.componentgame.game.lms.weapons.CrystalBlade;
import me.exerosis.game.engine.componentgame.game.lms.weapons.staff.FireJavelinCooldown;
import me.exerosis.game.engine.componentgame.game.lms.weapons.staff.MoltenShockwaveCooldown;
import me.exerosis.game.engine.componentgame.game.lms.weapons.staff.Staff;

import java.util.LinkedList;

public class MageComponentBundle implements ComponentBundle {

    public MageComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new MoltenShockwaveCooldown());
        compoents.add(new FireJavelinCooldown());
        compoents.add(new Staff());
        compoents.add(new CrystalBlade());
        compoents.add(new Mage());
        return compoents;
    }
}
