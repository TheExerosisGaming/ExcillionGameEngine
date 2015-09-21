package me.exerosis.game.engine.implementation.old.core.command;

import me.exerosis.game.engine.implementation.old.event.game.CommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutorComponent extends Component implements CommandExecutor {

    private Map<String, OnCommand> _commands = new HashMap<String, OnCommand>();

    public CommandExecutorComponent() {
    }

    public void addCommandRunner(String commandName, OnCommand onCommand) {
        ((JavaPlugin) getPlugin()).getCommand(commandName).setExecutor(this);
        _commands.put(commandName, onCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Bukkit.getPluginManager().callEvent(new CommandEvent(label, args, (Player) sender));
            if (_commands.containsKey(label))
                return _commands.get(label).run(args, (Player) sender);
        }
        return false;
    }

    @Override
    public void onEnable() {
        addCommandRunner("game", new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length < 1 || args.length > 2)
                    return false;
                if (args[0].equals("start")) {
                    if (getGame().isStarted()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(ChatColor.RED);
                        builder.append(ChatColor.BOLD);
                        builder.append("The game is already running!");
                        sender.sendMessage(builder.toString());
                        return true;
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append(ChatColor.GREEN);
                    builder.append(ChatColor.BOLD);
                    builder.append("The game was started by ");
                    builder.append(sender.getName());
                    builder.append("!");
                    Bukkit.broadcastMessage(builder.toString());

                    getGame().enableGame();
                    return true;
                } else if (args[0].equals("stop")) {
                    if (!getGame().isStarted()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append(ChatColor.RED);
                        builder.append(ChatColor.BOLD);
                        builder.append("The game is not running!");
                        sender.sendMessage(builder.toString());
                        return true;
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append(ChatColor.RED);
                    builder.append(ChatColor.BOLD);
                    builder.append("The game was stopped by ");
                    builder.append(sender.getName());
                    if (args.length == 2) {
                        builder.append(" because, ");
                        builder.append(args[1]);
                    } else
                        builder.append("!");
                    Bukkit.broadcastMessage(builder.toString());

                    getGame().disableGame();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDisable() {
    }
}
