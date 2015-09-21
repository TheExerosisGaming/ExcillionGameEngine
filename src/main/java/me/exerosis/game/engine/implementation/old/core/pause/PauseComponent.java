package me.exerosis.game.engine.implementation.old.core.pause;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.old.core.command.CommandExecutorComponent;
import me.exerosis.game.engine.implementation.old.core.command.OnCommand;
import me.exerosis.game.engine.implementation.old.core.cooldown.Cooldown;
import me.exerosis.game.engine.implementation.trialtwo.world.GameFolderManager;
import me.exerosis.game.engine.implementation.old.countdown.Countdown;
import me.exerosis.game.engine.implementation.old.countdown.countdowns.GameResumeCountdown;
import me.exerosis.game.engine.implementation.old.event.game.pause.PauseState;
import me.exerosis.game.engine.implementation.old.event.game.pause.PauseStateChangeEvent;
import me.exerosis.game.engine.implementation.old.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.util.FreezePlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class PauseComponent extends GameComponent {
    private Countdown[] _countdowns;
    private Cooldown[] _cooldowns;
    private CommandExecutorComponent _commandExecutorComponent;
    private GameResumeCountdown _resumeCountdown;
    private GameFolderManager _gameFolderManager;

    private PauseState _pauseState = PauseState.RESUMED;
    private int _slot;

    public PauseComponent(Plugin plugin, Game game, Countdown[] countdowns, Cooldown[] cooldowns, CommandExecutorComponent commandExecutorComponent, GameResumeCountdown resumeCountdown, GameFolderManager gameFolderManager) {
        super(plugin, game);
        _countdowns = countdowns;
        _cooldowns = cooldowns;
        _commandExecutorComponent = commandExecutorComponent;
        _resumeCountdown = resumeCountdown;
        _gameFolderManager = gameFolderManager;
    }

    @EventHandler
    public void onPauseStateChange(PauseStateChangeEvent event) {
        getPlayers().forEach(this::setPausePaneState);
        if (event.getPauseState().equals(PauseState.PAUSED)) {
            formattedBroadcast(ChatColor.RED + ChatColor.BOLD.toString() + "Game paused!");
            FreezePlayerUtil.getInstance().setAllFrozen(true);
            pauseCooldowns(true);
        }
        else if (event.getPauseState().equals(PauseState.RESUMED)) {
            formattedBroadcast(ChatColor.GREEN + ChatColor.BOLD.toString() + "Game resumed!");
            FreezePlayerUtil.getInstance().setAllFrozen(false, true);
            pauseCooldowns(false);
        }
        else
            _resumeCountdown.start();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().getItemInHand().getType().equals(Material.STAINED_GLASS_PANE))
            return;
        togglePauseState(event.getPlayer());
        event.getItemDrop().remove();
    }

    private void togglePauseState(Player player) {
        if (_pauseState.equals(PauseState.PAUSED)) {
            if (player.hasPermission("game.resume"))
                setPauseState(PauseState.RESUMING);
        }
        else if (_pauseState.equals(PauseState.RESUMED, PauseState.RESUMING))
            if (player.hasPermission("game.pause"))
                setPauseState(PauseState.PAUSED);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.STAINED_GLASS_PANE))
            togglePauseState(event.getPlayer());
        if (!event.getPlayer().getItemInHand().getType().equals(Material.BOW))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onGameStateChange(PostGameStateChangeEvent event) {
        getPlayers().forEach(this::setPausePaneState);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setPausePaneState(event.getPlayer());
    }

    @SuppressWarnings("deprecation")
    public void setPausePaneState(Player player) {
        if (!player.hasPermission("game"))
            return;
        player.getInventory().remove(Material.STAINED_GLASS_PANE);
        ItemStack stack = new ItemStack(Material.STAINED_GLASS_PANE);

        DyeColor dyeColor = null;
        StringBuilder displayName = new StringBuilder();

        switch (_pauseState) {
            case RESUMED:
                dyeColor = DyeColor.GREEN;
                displayName.append(ChatColor.GREEN);
                displayName.append(ChatColor.BOLD);
                displayName.append("Drop to pause the game!");
                break;
            case RESUMING:
                dyeColor = DyeColor.GRAY;
                displayName.append(ChatColor.GRAY);
                displayName.append(ChatColor.BOLD);
                displayName.append("Game resuming!");
                break;
            case PAUSED:
                dyeColor = DyeColor.RED;
                displayName.append(ChatColor.RED);
                displayName.append("Drop to resume the game!");
                break;
        }

        stack.setDurability(dyeColor.getWoolData());
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(displayName.toString());
        stack.setItemMeta(meta);

        player.getInventory().setItem(_slot, stack);
    }


    private void addCommands() {
        OnCommand pause = new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length != 1)
                    return false;
                if (args[0].equals("pause")) {
                    if (_pauseState.equals(PauseState.RESUMED, PauseState.RESUMING)) {
                        broadcast(ChatColor.RED.toString() + ChatColor.BOLD + "The game was paused by " + sender.getName() + ".");
                        togglePauseState(sender);
                    }
                    else if (_pauseState.equals(PauseState.PAUSED))
                        sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "The game is already paused!");
                    return true;
                }
                else if (args[0].equals("resume")) {
                    if (_pauseState.equals(PauseState.PAUSED)) {
                        broadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "The game was set to resume in 5 seconds by " + sender.getName() + ".");
                        togglePauseState(sender);
                    }
                    else if (_pauseState.equals(PauseState.RESUMED))
                        sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "The game is already resumed!");
                    else if (_pauseState.equals(PauseState.RESUMING)) {
                        broadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "The game was resumed instantly by " + sender.getName() + ".");
                        togglePauseState(sender);
                    }
                    return true;
                }
                return false;
            }
        };
        _commandExecutorComponent.addCommandRunner("game", pause);
    }

    @Override
    public void onEnable() {
        startTask(10, 10);
        registerListener();
        _slot = _gameFolderManager.getConfig("gameConfig.yml").getInt("pausePaneSlot");
        addCommands();
        setPauseState(PauseState.RESUMED);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopTask();
        unregisterListener();
        super.onDisable();
    }

    public PauseState getPauseState() {
        return _pauseState;
    }

    public void setPauseState(PauseState pauseState) {
        _pauseState = pauseState;
        callEvent(new PauseStateChangeEvent(_pauseState));
    }

    private void pauseCooldowns(boolean pause) {
        if (_cooldowns != null)
            for (Cooldown cooldown : _cooldowns)
                cooldown.pause(pause);
        if (_countdowns != null)
            for (Countdown countdown : _countdowns)
                countdown.pause(pause);
    }

    public boolean isPaused() {
        return _pauseState.equals(PauseState.PAUSED, PauseState.RESUMING);
    }
}