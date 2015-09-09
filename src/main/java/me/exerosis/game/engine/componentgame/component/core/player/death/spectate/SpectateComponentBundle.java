package me.exerosis.game.engine.componentgame.component.core.player.death.spectate;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;

import java.util.LinkedList;

public class SpectateComponentBundle implements ComponentBundle {

    public SpectateComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> components = new LinkedList<Component>();
        //	components.add(new SpectateGamemode());
        components.add(new SpectateComponent());
        //	components.add(new SpectatorInventoryComponent());
        return components;
    }
}
