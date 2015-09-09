package me.exerosis.game.engine.componentgame.game.lms.weapons.throwing.axes;

import me.exerosis.game.engine.componentgame.Arena;
import me.exerosis.game.engine.componentgame.InstancePool.Depend;
import me.exerosis.game.engine.componentgame.component.core.weapon.Weapon;
import me.exerosis.game.engine.componentgame.event.game.GameStateChangeEvent;
import me.exerosis.game.engine.componentgame.game.lms.kits.Axeman;
import me.exerosis.game.engine.componentgame.gamestate.GameState;
import me.exerosis.packet.utils.location.AdvancedLocation;
import me.exerosis.packet.utils.ticker.ExTask;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class ThrowingAxe extends Weapon implements Runnable {
    private static final Random RANDOM = new Random();
    @Depend
    private ThrowingAxeCooldown _axeCooldown;
    @Depend
    private Axeman _axeman;
    private HashMap<ArmorStand, Player> _axes = new HashMap<ArmorStand, Player>();
    private int _slot;

    private void addAxeItem(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItem(_slot);
        if (item == null)
            inventory.setItem(_slot, getItemStack());
        else {
            item.setAmount(item.getAmount() + 1);
            inventory.setItem(_slot, item);
        }
    }

    private void removeAxeItem(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItem(_slot);

        if (item == null)
            return;
        item.setAmount(item.getAmount() - 1);
        inventory.setItem(_slot, item);
    }

    public void onRightClick(Player player) {
        if (_axeCooldown.isCooling(player))
            return;

        removeAxeItem(player);
        createStand(player);
        _axeCooldown.addPlayer(player);
    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.POST_GAME))
            for (ArmorStand armorStand : _axes.keySet())
                armorStand.remove();
    }

    @EventHandler
    public void onPickUp(PlayerInteractAtEntityEvent event) {
        event.setCancelled(true);
        Entity stand = event.getRightClicked();

        if (!_axes.containsKey(stand))
            return;
        if (!_axeman.isAxeman(event.getPlayer()))
            return;

        addAxeItem(event.getPlayer());
        _axes.remove(stand);
        stand.remove();
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (!event.getItem().getItemStack().getType().equals(getMaterial()))
            return;
        event.setCancelled(true);

        if (!_axeman.isAxeman(event.getPlayer()))
            return;

        event.getItem().remove();
        addAxeItem(event.getPlayer());
    }

    public void run() {
        for (Entry<ArmorStand, Player> entry : _axes.entrySet()) {
            ArmorStand stand = entry.getKey();

            if (stand == null || !stand.hasGravity())
                continue;

            double newPose = stand.getRightArmPose().getX() + 0.5D;
            stand.setRightArmPose(new EulerAngle(newPose, 0.0D, 0.0D));

            if (stand.isOnGround())
                finishGroundPose(stand);

            for (Player player : stand.getWorld().getPlayers()) {
                Location location = player.getLocation();
                Location standLocation = stand.getEyeLocation();

                if (location.distance(standLocation) <= 1.4 || player.getEyeLocation().distance(standLocation) <= 1.4 || location.add(0, 1.5, 0).distance(standLocation) <= 1.1) {
                    if (player.equals(entry.getValue()))
                        continue;

                    player.damage(getDamage() + 0.0D);
                    stand.remove();
                    if (_axes.size() < 0)
                        ExTask.stopTask(this);
                }
            }

        }
    }

    private void finishGroundPose(ArmorStand stand) {
        double angle = RANDOM.nextInt(11) / 10.0;
        double hight = 0.25;

        if (new AdvancedLocation(stand.getLocation().add(0.0, -0.6, 0.0)).isSlab())
            hight -= 0.5;
        if (angle < 0.5)
            hight += 0.05;
        else if (angle < 0.1)
            hight += 0.07;

        stand.setRightArmPose(new EulerAngle(angle, 0.0, 0.0));
        Location location = stand.getLocation();
        location = location.add(0.0, -hight, 0.0);
        stand.teleport(location);
        stand.setGravity(false);
        ExTask.stopTask(this);
    }

    private void createStand(Player player) {
        Location location = player.getLocation();
        ArmorStand stand = location.getWorld().spawn(location.add(0.0, 1.0, 0.0), ArmorStand.class);

        stand.setArms(true);
        stand.setVisible(false);
        stand.setSmall(true);
        stand.setItemInHand(getItemStack());
        stand.setVelocity(player.getEyeLocation().getDirection().multiply(3));
        ExTask.startTask(this, 1, 1);

        _axes.put(stand, player);
    }

    public int getSlot() {
        return _slot;
    }

    public String getConfigName() {
        return "throwingAxe";
    }

    public void onEnable() {
        for (Player player : Arena.getPlayers())
            player.setResourcePack("https://www.dropbox.com/s/08zecg17mw1rqha/testPack.zip?dl=1");

        _slot = _axeCooldown.getSlot();
        super.onEnable();
    }

    public void onDisable() {
        ExTask.stopTask(this);

        for (Player player : Arena.getPlayers())
            player.setResourcePack("https://www.dropbox.com/s/rno1eohlgigzhbm/legacy_empty.zip?dl=1");

        super.onDisable();
    }
}
