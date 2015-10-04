package me.exerosis.game.engine.util;

import me.exerosis.reflection.data.Pair;
import me.exerosis.reflection.data.Triplet;
import org.bukkit.*;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Copyright (c) 2015 Exerosis, The Exerosis, The Exerosis Gaming
 * <p>
 * Pseudonyms registered under legal name.
 *
 * @author Exerosis
 *         <p>
 *         A utility for preventing a player from moving, and a bunch of useful methods to go with it.
 */
public class FreezePlayerUtil implements Runnable, Listener {
    //Fields.
    //Singleton instance.
    private static FreezePlayerUtil instance = null;

    //Frozen player data values.
    private Map<Player, Triplet<Integer, Float, Pair<Integer, Integer>>> _players = new HashMap<>();

    //Queued offline players.
    private Map<OfflinePlayer, Boolean> _toFreeze = new HashMap<>();
    private byte _freezeNew = 0;

    //Task and events fields.
    private int _id;
    private Plugin _plugin;

    //Peaceful worlds
    private Set<World> _worlds = new HashSet<>();

    //Private singleton constructor.
    private FreezePlayerUtil() {
    }

    /**
     * Gets the single instance of this class.
     *
     * @return FreezePlayerUtil - The single instance of this class.
     */
    public static FreezePlayerUtil getInstance() {
        if (instance == null)
            instance = new FreezePlayerUtil();
        return instance;
    }

    //Primary methods.
    //setFrozen methods.

    /**
     * Sets the plugin needed to run the runners, and register join events. However base freezeing and unfreezing will function without the plugin.
     *
     * @param plugin
     */
    public void setPlugin(Plugin plugin) {
        _plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, _plugin);
        start();
    }

    /**
     * Stops movement and jumping of a list of {@linkplain Player}s or {@linkplain OfflinePlayer}s, the Players
     * walk speed, hunger, and {@linkplain PotionEffectType}.JUMP, will be maintained.
     * If any of the Players are offline they will be frozen, or unfrozen, when they next come online again unless unfrozen before then.
     *
     * @param freeze  boolean - True if the Players are to be frozen. False if the players are to be unfrozen.
     * @param players {@linkplain Player}[] The list of Players or OfflinePlayers to be frozen or unfrozen. Must be greater then 0.
     */
    public void setFrozen(boolean freeze, OfflinePlayer... players) {
        if (players.length < 1)
            throw new IllegalArgumentException("You must freeze at least one player!");
        for (OfflinePlayer offlinePlayer : players) {

            if (!offlinePlayer.isOnline()) {
                if (willBeAffected(offlinePlayer))
                    if (_toFreeze.get(offlinePlayer) != freeze)
                        _toFreeze.replace(offlinePlayer, freeze);
                    else
                        _toFreeze.put(offlinePlayer, freeze);
                continue;
            }

            Player player = (Player) offlinePlayer;
            if (freeze) {
                Pair<Integer, Integer> potionPair = null;
                for (PotionEffect effect : player.getActivePotionEffects())
                    if (effect.getType().equals(PotionEffectType.JUMP))
                        potionPair = new Pair<>(effect.getDuration(), effect.getAmplifier());
                _players.put(player, new Triplet<>(player.getFoodLevel(), player.getWalkSpeed(), potionPair));
                player.setWalkSpeed(0.0F);
                player.removePotionEffect(PotionEffectType.JUMP);
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));
                player.setFoodLevel(6);
            } else {
                Triplet<Integer, Float, Pair<Integer, Integer>> values = _players.get(player);
                if (values == null) {
                    Log.error("Something went wrong and a players old hunger, walk speed, and active potions where not restored!");
                    return;
                }
                Pair<Integer, Integer> potionPair = values.getC();
                player.removePotionEffect(PotionEffectType.JUMP);
                if (potionPair != null) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, potionPair.getA().intValue(), potionPair.getB().intValue()));
                }
                player.setWalkSpeed(values.getB());
                player.setFoodLevel(values.getA());
                _players.remove(player);
            }
        }
    }


    //setAllFrozen methods.

    /**
     * Stops movement and jumping of a list of {@linkplain Player}s or {@linkplain OfflinePlayer}s, the Players
     * walk speed, hunger, and {@linkplain PotionEffectType}.JUMP, will be maintained.
     * If any of the Players are offline they will be frozen when they next come online again unless unfrozen before then.
     *
     * @param players {@linkplain Player}[] The list of Players or OfflinePlayers to be frozen. Must be greater then 0.
     */
    public void setFrozen(OfflinePlayer... players) {
        for (OfflinePlayer player : players)
            setFrozen(player);
    }

    /**
     * Freeze or unfreeze all {@linkplain Player}s or future players.
     *
     * @param frozen  boolean - True if all Players are to be frozen. False if all Players are to be unfrozen.
     * @param offline boolean - True if Players are to be affected when they join.
     */
    @SuppressWarnings("deprecation")
    public void setAllFrozen(boolean frozen, boolean offline) {
        PlayerUtil.getOnlinePlayers().stream().filter(player -> isFrozen(player) != frozen).forEach(player -> setFrozen(frozen, player));
        if (offline)
            setFreezeNewPlayers((byte) (frozen ? 1 : -1));
    }


    //setFreezeNewPlayers methods.

    /**
     * Freeze all {@linkplain Player}s or future players.
     *
     * @param offline boolean - True if Players are to be frozen when they join.
     */
    public void setAllFrozen(boolean offline) {
        setAllFrozen(true, offline);
    }

    /**
     * Changes if new {@linkplain Player}s are to be affected when they join, and how.
     *
     * @param freezeNew byte - 1 if new Players are to be frozen, 0 if Players are to be unaffected, -1 if Players are to be frozen.
     */
    public void setFreezeNewPlayers(byte freezeNew) {
        _freezeNew = freezeNew;
    }


    //Check methods.

    /**
     * Sets new {@linkplain Player}s to be frozen when they join.
     */
    public void setFreezeNewPlayers() {
        setFreezeNewPlayers((byte) 1);
    }

    /**
     * Checks if a {@linkplain OfflinePlayer} has been queued to be frozen or unfrozen once they join the server.
     *
     * @param player {@linkplain OfflinePlayer} - The OfflinePlayer to be checked.
     * @return boolean - True if the OfflinePlayer is to be frozen or unfrozen.
     */
    public boolean willBeAffected(OfflinePlayer player) {
        return _toFreeze.containsKey(player) && _toFreeze.get(player);
    }

    //Listeners and runners.

    /**
     * Checks if a {@linkplain Player} is currently frozen.
     *
     * @param player {@linkplain Player} - The Player to be checked.
     * @return boolean - True if the Player is frozen.
     */
    public boolean isFrozen(Player player) {
        return _players.containsKey(player);
    }

    /**
     * Checks if the {@linkplain Player} is queued to be frozen or unfrozen and acts accordingly.
     *
     * @param event {@linkplain PlayerJoinEvent} A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //Checks if this is a queued player joining.
        _toFreeze.entrySet().stream().filter(entry -> entry.getKey().getPlayer().equals(event.getPlayer())).forEach(entry -> {
            setFrozen(entry.getValue(), event.getPlayer());
            _toFreeze.remove(entry.getKey());
        });

        //Check if new players should be affected.
        if (_freezeNew == 1)
            setFrozen(true, event.getPlayer());
        else if (_freezeNew == -1)
            setFrozen(false, event.getPlayer());
    }

    /**
     * Set peaceful worlds to normal and make sure no mobs spawn.
     *
     * @param event {@linkplain WorldLoadEvent} A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event) {
        if (!event.getWorld().getDifficulty().equals(Difficulty.PEACEFUL))
            return;
        event.getWorld().setDifficulty(Difficulty.NORMAL);
        _worlds.add(event.getWorld());
    }

    /**
     * Makes sure no mobs spawn in peaceful worlds.
     *
     * @param event {@linkplain EntitySpawnEvent} A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (_worlds.contains(entity.getWorld()))
            if (entity instanceof Monster)
                entity.remove();
    }

    /**
     * Makes sure hackers can't move.
     *
     * @param event {@linkplain PlayerMoveEvent} A param for {@linkplain Bukkit}'s events system.
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!isFrozen(event.getPlayer()))
            return;
        Location from = event.getFrom();
        Location to = event.getTo();
        double x = Math.floor(from.getX());
        double z = Math.floor(from.getZ());

        if (Math.floor(to.getZ()) == z && Math.floor(to.getX()) == x)
            return;

        x += .5;
        z += .5;
        event.getPlayer().teleport(new Location(from.getWorld(), x, from.getY(), z, from.getYaw(), from.getPitch()));
    }

    /**
     * Makes sure that the {@linkplain PotionEffect} is not given to the {@linkplain Player} if the time has
     * elapsed and is instead given to the Player with the correct amount of remaining time.
     */
    @Override
    public void run() {
        for (Entry<Player, Triplet<Integer, Float, Pair<Integer, Integer>>> entry : _players.entrySet()) {
            Triplet<Integer, Float, Pair<Integer, Integer>> value = entry.getValue();
            Pair<Integer, Integer> potionPair = value.getC();

            if (potionPair != null) {
                //Check if time is up.
                int timeLeft = potionPair.getA();
                if (timeLeft > 0)
                    potionPair.setA(timeLeft - 1);
                else
                    potionPair = null;
            }
            //Replace old time with new time -1.
            _players.replace(entry.getKey(), value.setC(potionPair));
        }
    }

    //Potion ticker.
    public void start() {
        if (!isRunning())
            _id = Bukkit.getScheduler().runTaskTimer(_plugin, this, 1, 1).getTaskId();
    }

    public void stopTask() {
        if (isRunning())
            Bukkit.getScheduler().cancelTask(_id);
    }

    public boolean isRunning() {
        return Bukkit.getScheduler().isQueued(_id);
    }
}