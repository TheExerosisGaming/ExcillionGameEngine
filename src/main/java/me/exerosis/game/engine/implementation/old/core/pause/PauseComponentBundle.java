package me.exerosis.game.engine.implementation.old.core.pause;

import me.exerosis.game.engine.implementation.old.countdown.countdowns.GameResumeCountdown;

import java.util.LinkedList;

public class PauseComponentBundle implements ComponentBundle {

    public PauseComponentBundle() {
    }

    @Override
    public LinkedList<Component> getComponents() {
        LinkedList<Component> compoents = new LinkedList<Component>();
        compoents.add(new PauseComponent());
        compoents.add(new GameResumeCountdown());
        return compoents;
    }
}
