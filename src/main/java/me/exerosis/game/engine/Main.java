package me.exerosis.game.engine;

import me.exerosis.game.engine.core.Arena;
import me.exerosis.game.engine.core.factory.RandomGameFactory;
import me.exerosis.game.engine.implementation.trialtwo.games.runner.RunnerGame;
import me.exerosis.game.engine.util.FreezePlayerUtil;
import me.exerosis.packet.player.injection.packet.player.display.displayables.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private Title _title;

    @Override
    public void onEnable() {
        Arena arena = new Arena();
        arena.setFactory(new RandomGameFactory(this, arena, RunnerGame.class));
        arena.nextSystem();
        FreezePlayerUtil.getInstance().setPlugin(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        if (!label.equals("test"))
            return false;
        Player player = (Player) sender;
        new SonarEffect(player.getLocation());
        return super.onCommand(sender, command, label, args);
    }
}