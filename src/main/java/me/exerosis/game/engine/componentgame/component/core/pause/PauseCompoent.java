package me.exerosis.game.engine.componentgame.component.core.pause;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.Component;
import me.exerosis.game.engine.componentgame.component.core.ChatComponent;
import me.exerosis.game.engine.componentgame.component.core.command.CommandExecutorComponent;
import me.exerosis.game.engine.componentgame.component.core.command.OnCommand;
import me.exerosis.game.engine.componentgame.component.core.cooldown.Cooldown;
import me.exerosis.game.engine.componentgame.component.core.world.GameFolderManager;
import me.exerosis.game.engine.componentgame.countdown.Countdown;
import me.exerosis.game.engine.componentgame.countdown.countdowns.GameResumeCountdown;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseState;
import me.exerosis.game.engine.componentgame.event.game.pause.PauseStateChangeEvent;
import me.exerosis.game.engine.componentgame.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.util.FreezePlayerUtil;
import org.bukkit.Bukkit;
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

public class PauseCompoent extends Component {
    @Depend(false)
    private Countdown[] _countdowns;
    @Depend(false)
    private Cooldown[] _cooldowns;
    @Depend
    private CommandExecutorComponent _commandExecutorComponent;
    @Depend
    private GameResumeCountdown _resumeCountdown;
    @Depend
    private GameFolderManager _gameFolderManager;

    private PauseState _pauseState = PauseState.RESUMED;
    private int _slot;

    public PauseCompoent() {
    }

    @EventHandler
    public void onPauseStateChange(PauseStateChangeEvent event) {
        for (Player player : Arena.getPlayers())
            setPausePaneState(player);
        if (event.getPauseState().equals(PauseState.PAUSED)) {
            ChatComponent.tellAll(ChatColor.RED + ChatColor.BOLD.toString() + "Game paused!");
            FreezePlayerUtil.getInstance().setAllFrozen(true);
            pauseCooldowns(true);
        } else if (event.getPauseState().equals(PauseState.RESUMED)) {
            ChatComponent.tellAll(ChatColor.GREEN + ChatColor.BOLD.toString() + "Game resumed!");
            FreezePlayerUtil.getInstance().setAllFrozen(false, true);
            pauseCooldowns(false);
        } else
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
        } else if (_pauseState.equals(PauseState.RESUMED, PauseState.RESUMING))
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
        for (Player player : Arena.getPlayers())
            setPausePaneState(player);
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

    public PauseState getPauseState() {
        return _pauseState;
    }

    public void setPauseState(PauseState pauseState) {
        _pauseState = pauseState;
        Bukkit.getPluginManager().callEvent(new PauseStateChangeEvent(_pauseState));
    }

    @Override
    public void onEnable() {
        startTask(10, 10);
        registerListener(this);
        _slot = _gameFolderManager.getConfig("gameConfig.yml").getInt("pausePaneSlot");
        addCommands();
        setPauseState(PauseState.RESUMED);
    }


    private void addCommands() {
        OnCommand pause = new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length != 1)
                    return false;
                if (args[0].equals("pause")) {
                    if (_pauseState.equals(PauseState.RESUMED, PauseState.RESUMING)) {
                        StringBuilder message = new StringBuilder(ChatColor.RED.toString());
                        message.append(ChatColor.BOLD);
                        message.append("The game was paused by ").append(sender.getName());
                        ChatComponent.tellAll(message.append(".").toString());

                        togglePauseState(sender);
                    } else if (_pauseState.equals(PauseState.PAUSED)) {
                        StringBuilder message = new StringBuilder(ChatColor.RED.toString());
                        message.append(ChatColor.BOLD).append("The game is already paused!");
                        ChatComponent.tell(sender, message.toString());
                    }
                    return true;
                } else if (args[0].equals("resume")) {
                    if (_pauseState.equals(PauseState.PAUSED)) {
                        StringBuilder message = new StringBuilder(ChatColor.GREEN.toString());
                        message.append(ChatColor.BOLD);
                        message.append("The game was set to resume in 5 seconds by ").append(sender.getName());
                        ChatComponent.tellAll(message.append(".").toString());

                        togglePauseState(sender);

                    } else if (_pauseState.equals(PauseState.RESUMED)) {
                        StringBuilder message = new StringBuilder(ChatColor.RED.toString());
                        message.append(ChatColor.BOLD).append("The game is already resumed!");
                        ChatComponent.tell(sender, message.toString());
                    } else if (_pauseState.equals(PauseState.RESUMING)) {
                        StringBuilder message = new StringBuilder(ChatColor.GREEN.toString());
                        message.append(ChatColor.BOLD);
                        message.append("The game was resumed instantly by ").append(sender.getName());
                        ChatComponent.tellAll(message.append(".").toString());

                        togglePauseState(sender);
                    }
                    return true;
                }
                return false;
            }
        };
        _commandExecutorComponent.addCommandRunner("game", pause);
    }


    private void pauseCooldowns(boolean pause) {
        if (_cooldowns != null)
            for (Cooldown cooldown : _cooldowns)
                cooldown.pause(pause);
        if (_countdowns != null)
            for (Countdown countdown : _countdowns)
                countdown.pause(pause);
    }

    @Override
    public void onDisable() {
        stopTask();
        unregisterListener(this);
    }

    public boolean isPaused() {
        return _pauseState.equals(PauseState.PAUSED, PauseState.RESUMING);
    }
}