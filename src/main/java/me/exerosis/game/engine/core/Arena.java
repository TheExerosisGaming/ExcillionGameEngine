package me.exerosis.game.engine.core;

import me.exerosis.component.SystemHolder;
import me.exerosis.component.factory.SystemFactory;
import me.exerosis.game.engine.core.factory.ConfigurationGameFactory;

public class Arena extends SystemHolder {
    @Override
    public void setFactory(SystemFactory factory) {
        if (!factory.getClass().isAssignableFrom(ConfigurationGameFactory.class))
            System.err.println("Factory has to be an instance off ConfigurationGameFactory!");
        super.setFactory(factory);
    }
}