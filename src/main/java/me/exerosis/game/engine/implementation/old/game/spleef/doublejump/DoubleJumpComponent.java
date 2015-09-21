package me.exerosis.game.engine.implementation.old.game.spleef.doublejump;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.implementation.old.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoubleJumpComponent extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private DoubleJumpCooldown _doubleJumpCooldown;
    private double _hight;
    private double _forwardMult;
    private double _distance;

    public DoubleJumpComponent(double forwardMult, double hight, double distance) {
        _forwardMult = forwardMult;
        _hight = hight;
        _distance = distance;
    }

    @Override
    public void onEnable() {
        registerListener(this);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME))
            for (Player player : _spectateComponent.getGamePlayers()) {
                ItemStack stack = new ItemStack(Material.IRON_AXE);
                ItemMeta meta = stack.getItemMeta();
                meta.spigot().setUnbreakable(true);
                stack.setItemMeta(meta);
                player.getInventory().setItem(0, stack);
            }

    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!getArena().getGameState().equals(GameState.IN_GAME))
            return;
        if (!_spectateComponent.getGamePlayers().contains(player))
            return;
        if (!player.getItemInHand().getType().equals(Material.IRON_AXE))
            return;
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        if (_doubleJumpCooldown.isCooling(player))
            return;
        event.setCancelled(true);

        new DoubleJump(player, _forwardMult, _hight, _distance);
        _doubleJumpCooldown.addPlayer(player);
    }
}