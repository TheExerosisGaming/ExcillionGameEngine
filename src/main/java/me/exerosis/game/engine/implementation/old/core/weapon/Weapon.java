package me.exerosis.game.engine.implementation.old.core.weapon;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.core.player.death.spectate.SpectateComponent;
import me.exerosis.game.engine.implementation.trialtwo.world.GameFolderManager;
import me.exerosis.game.engine.core.GameState;
import me.exerosis.game.engine.util.VectorUtil;
import me.exerosis.packet.utils.location.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Weapon extends Component {
    @Depend
    private SpectateComponent _spectateComponent;
    @Depend
    private GameFolderManager _gameFolderManager;
    private YamlConfiguration _config;

    public Weapon() {
    }

    public abstract String getConfigName();

    public double getDamage() {
        return getConfigValue("damage", Double.class).doubleValue();
    }

    public int getRange() {
        return getConfigValue("range", Integer.class).intValue();
    }

    public double getKnockback() {
        return getConfigValue("knockback", Integer.class).doubleValue();
    }

    public Material getMaterial() {
        return Material.valueOf(getConfigValue("material", String.class).toUpperCase().replace(' ', '_'));
    }

    public int getAmount() {
        return getConfigValue("amount", Integer.class).intValue();
    }

    public byte getData() {
        return getConfigValue("data", Integer.class).byteValue();
    }

    public String getName() {
        return getConfigValue("name", String.class);
    }

    public boolean buff() {
        return getConfigValue("buff", Boolean.class).booleanValue();
    }

    @SuppressWarnings("unchecked")
    private <T> T getConfigValue(String name, Class<T> returnType) {
        return (T) _config.get(new StringBuilder(getConfigName()).append('.').append(name).toString());
    }

    public void onRightClick(Player player) {
    }

    public void onLeftClick(Player player) {
    }

    public ItemStack getItemStack() {
        ItemStack stack = new ItemStack(getMaterial(), getAmount(), getData());
        ItemMeta meta = stack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.setDisplayName(ChatColor.DARK_PURPLE + getName());
        List<String> lore = new ArrayList<String>();
        lore.add("Damage: " + getDamage());
        lore.add("Range: " + getRange());
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onPunch(PlayerInteractEvent event) {
        if (!getArena().getGameState().equals(GameState.IN_GAME))
            return;
        Player player = event.getPlayer();
        if (_spectateComponent.isSpectating(player))
            return;
        if (!player.getItemInHand().getType().equals(getMaterial()))
            return;
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            onRightClick(player);
        else if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            onLeftClick(player);
        if (!event.getAction().equals(Action.LEFT_CLICK_AIR))
            return;
        if (!buff())
            return;

        LivingEntity target = LocationUtils.getLivingTarget(player, getRange(), false);
        if (target != null) {
            target.damage(getDamage() + 0D, player);
            VectorUtil.knockback(target, player.getLocation(), getKnockback());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!getArena().getGameState().equals(GameState.IN_GAME))
            return;
        if (!event.getDamager().getType().equals(EntityType.PLAYER))
            return;
        if (_spectateComponent.isSpectating((Player) event.getDamager()))
            return;
        if (!buff())
            return;
        Player player = (Player) event.getDamager();
        if (player.getItemInHand().getType().equals(getMaterial())) {
            VectorUtil.knockback(event.getEntity(), player.getLocation(), getKnockback());
            event.setDamage(getDamage() + 0D);
        }
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }

    @Override
    public void onEnable() {
        registerListener(this);
        _config = _gameFolderManager.getConfig("weapons.yml");
    }
}