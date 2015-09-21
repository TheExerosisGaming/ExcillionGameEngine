package me.exerosis.game.engine.implementation.old.core.command;

import org.bukkit.entity.Player;

public abstract class OnCommand {
    public abstract boolean run(String[] args, Player sender);
}