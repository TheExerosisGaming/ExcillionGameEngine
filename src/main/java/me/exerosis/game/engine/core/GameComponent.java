package me.exerosis.game.engine.core;

import me.exerosis.component.Component;
import me.exerosis.component.event.EventExecutor;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.util.PlayerUtil;
import me.exerosis.packet.utils.ticker.ExTask;
import me.exerosis.reflection.util.Enableable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.PrintStream;
import java.util.List;

public class GameComponent implements Component, Runnable, Listener, Enableable {
    private final Game game;
    private boolean enabled;

    public GameComponent(Game game) {
        this.game = game;
      /*  game.addComponent(this);*/
    }
/*
    public GameComponent(Game game, GameState enable, GameState disable) {
        this.game = game;
        game.addComponent(enable, disable, this);
    }*/

    public GameState getGameState() {
        return (GameState) game.getSystemState();
    }

    public void setGameState(GameState state) {
        game.setSystemState(state);
    }

    public Plugin getPlugin() {
        return game.getPlugin();
    }

    public Arena getArena() {
        return game.getArena();
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getPlayers() {
        return PlayerUtil.getOnlinePlayers();
    }

    public void registerListener() {
        game.getEventManager().registerListener(this);
        Bukkit.getPluginManager().registerEvents(this, getPlugin());
    }

    public void unregisterListener() {
        game.getEventManager().unregisterListener(this);
        HandlerList.unregisterAll(this);
    }

    public void startTask(long delay, long time) {
        ExTask.startTask(this, delay, time);
    }

    public void stopTask() {
        ExTask.stopTask(this);
    }

    public void formattedMessage(Player player, String string) {
        player.sendMessage(getFormattedString(string));
    }

    public void formattedBroadcast(String string) {
        Bukkit.broadcastMessage(getFormattedString(string));
    }

    public void broadcast(String string) {
        Bukkit.broadcastMessage(string);
    }

    public void print(String string) {
        print(string, false);
    }

    public void print(String string, boolean error) {
        PrintStream stream = error ? System.err : System.out;
        stream.println("[" + getClass().getSimpleName() + "]: " + string);
    }

    public String getFormattedString(String string) {
        return ChatColor.BOLD.toString() + ChatColor.DARK_GRAY + "Game: " + string;
    }

    public void callEvent(Object event) {
        game.getEventManager().callEvent(event);
    }

    public <T> void callEvent(T event, EventExecutor<T> executor) {
        game.getEventManager().callEvent(event, executor);
    }

    @Override
    public void run() {

    }

    @Override
    public void onEnable() {
        enabled = true;
    }

    @Override
    public void onDisable() {
        unregisterListener();
        stopTask();
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}