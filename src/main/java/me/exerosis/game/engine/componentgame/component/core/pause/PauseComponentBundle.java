package me.exerosis.game.engine.componentgame.component.core.pause;

import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.ComponentBundle;
import me.exerosis.game.engine.componentgame.countdown.countdowns.GameResumeCountdown;

import java.util.LinkedList;

public class PauseComponentBundle implements ComponentBundle {

    public PauseComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new PauseCompoent());
        compoents.add(new GameResumeCountdown());
        return compoents;
    }
}
