package me.exerosis.game.engine.implementation.trialtwo.games.spleef.doublejump;

import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.player.death.SpectateComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoubleJumpComponent extends GameComponent {
    private SpectateComponent _spectateComponent;
    private DoubleJumpCooldown _doubleJumpCooldown;
    private double _height;
    private double _forwardForce;
    private double _distance;

    public DoubleJumpComponent(Game game, SpectateComponent spectateComponent, DoubleJumpCooldown doubleJumpCooldown) {
        super(game);
        _spectateComponent = spectateComponent;
        _doubleJumpCooldown = doubleJumpCooldown;
    }

    @Override
    public void onEnable() {
        registerListener();
        _height = getGame().getGameConfigValue("doubleJump.height", Double.class);
        _forwardForce = getGame().getGameConfigValue("doubleJump.forwardForce", Double.class);
        _distance = getGame().getGameConfigValue("doubleJump.distance", Double.class);

        for (Player player : _spectateComponent.getGamePlayers()) {
            ItemStack stack = new ItemStack(Material.IRON_AXE);
            ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            stack.setItemMeta(meta);
            player.getInventory().setItem(0, stack);
        }
    }

    @Override
    public void onDisable() {
        unregisterListener();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!_spectateComponent.getGamePlayers().contains(player))
            return;
        if (!player.getItemInHand().getType().equals(Material.IRON_AXE))
            return;
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        if (_doubleJumpCooldown.isCooling(player))
            return;

        new DoubleJump(player, _forwardForce, _height, _distance);
        _doubleJumpCooldown.addPlayer(player);
    }
}