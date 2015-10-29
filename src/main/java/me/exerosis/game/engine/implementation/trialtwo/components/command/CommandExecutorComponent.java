package me.exerosis.game.engine.implementation.trialtwo.components.command;


import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public class CommandExecutorComponent extends GameComponent implements CommandExecutor {
    private Map<String, OnCommand> _commands = new HashMap<>();

    public CommandExecutorComponent(Game game) {
        super(game);
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
                    if (getGame(). ()){
                        sender.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "The game is already running!");
                        return true;
                    }
                    formattedBroadcast(String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + "The game was started by " + sender.getName() + "!");

                    getGame().enableGame();
                    return true;
                } else if (args[0].equals("stop")) {
                    if (!getGame().isStarted()) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "The game is not running!");
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
                    formattedBroadcast(builder.toString());

                    getGame().disableGame();
                    return true;
                }
                return false;
            }
        });
    }
}
