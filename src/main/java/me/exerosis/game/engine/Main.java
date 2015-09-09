package me.exerosis.game.engine;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.factory.RotatingGame;
import me.exerosis.game.engine.componentgame.game.lms.DeathmatchGame;
import me.exerosis.game.engine.componentgame.game.runner.RunnerGame;
import me.exerosis.game.engine.componentgame.game.spleef.SpleefGame;
import me.exerosis.game.engine.util.FreezePlayerUtil;
import net.minecraft.server.v1_8_R1.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    public static void spawnCustomMob(Location location, Entity entity) {
        entity.setPosition(location.getX(), location.getY(), location.getZ());
        ((CraftWorld) location.getWorld()).getHandle().addEntity(entity);
    }

    @Override
    public void onEnable() {
        new Arena(this, new RotatingGame(new DeathmatchGame(), new SpleefGame(), new RunnerGame())).start();
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