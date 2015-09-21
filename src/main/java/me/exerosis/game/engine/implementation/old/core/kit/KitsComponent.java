package me.exerosis.game.engine.implementation.old.core.kit;

import me.exerosis.game.engine.implementation.InstancePool.Depend;
import me.exerosis.game.engine.implementation.old.event.game.playerdata.PlayerDataLoadEvent;
import me.exerosis.game.engine.implementation.old.event.game.post.PostGameStateChangeEvent;
import me.exerosis.game.engine.core.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitsComponent extends Component {
    private Map<Player, Kit> _players = new HashMap<Player, Kit>();
    private List<Kit> _kits;
    @Depend
    private CommandExecutorComponent _commandExecutorComponent;
    @Depend
    private PlayerDataComponent _playerDataComponent;

    //TODO make sure the _players list works! Make a disable!
    public KitsComponent() {
    }

    public void setDefaultKit(Player player, Kit kit) {
        setKit(player, kit);
        setDefault(player, kit);
    }

    public void setKit(Player player, Kit kit) {
        if (!_players.containsKey(player))
            _players.put(player, kit);
        else {
            player.getInventory().clear();
            _players.get(player).onKitDisable(player);
            _players.replace(player, kit);
        }

        kit.onKitEnable(player);
        if (getArena().getGameState().equals(GameState.LOBBY, GameState.RESTARTING))
            player.getInventory().setItem(8, new ItemStack(Material.COMPASS));
        if (!_players.containsKey(player))
            _players.put(player, kit);
    }

    public int getMoney(Player player) {
        return _playerDataComponent.getPlayerData(player).getData("Coins", int.class);
    }

    public void purchase(Player player, Class<? extends Kit> kitClass) {
        purchase(player, getKit(kitClass));
    }

    public void purchase(Player player, Kit kit) {
        PlayerData playerData = _playerDataComponent.getPlayerData(player);
        playerData.decrementIntBy("Coins", kit.getCost());
        setHasKit(player, true, kit);
    }

    public void setHasKit(Player player, boolean has, Class<? extends Kit> kitClass) {
        setHasKit(player, has, getKit(kitClass));
    }

    public void setHasKit(Player player, boolean has, Kit kit) {
        PlayerData playerData = _playerDataComponent.getPlayerData(player);
        String[] kits = playerData.getData("Kits", String.class).split(":");

        kits[kit.getDBPos()] = has ? "Y" : "N";

        StringBuilder kitsBuilder = new StringBuilder();
        kitsBuilder.append(kits[0]);
        for (int x = 1; x < kits.length; x++) {
            kitsBuilder.append(":");
            kitsBuilder.append(kits[x]);
        }
        playerData.setData("Kits", kitsBuilder.toString());

        if (has) {
            setDefaultKit(player, kit);
            return;
        }

        player.getInventory().clear();
        player.getInventory().setItem(8, new ItemStack(Material.COMPASS));
        _players.remove(player);
    }

    private void setDefault(Player player, Kit kit) {
        PlayerData playerData = _playerDataComponent.getPlayerData(player);
        String[] kits = playerData.getData("Kits", String.class).split(":");

        for (int i = 0; i < kits.length; i++) {
            if (kits[i].equals("D"))
                kits[i] = "Y";
        }

        kits[kit.getDBPos()] = "D";

        StringBuilder kitsBuilder = new StringBuilder();
        kitsBuilder.append(kits[0]);
        for (int x = 1; x < kits.length; x++) {
            kitsBuilder.append(":");
            kitsBuilder.append(kits[x]);
        }
        playerData.setData("Kits", kitsBuilder.toString());
    }

    public boolean hasKit(Player player, Class<? extends Kit> kitClass) {
        return hasKit(player, getKit(kitClass));
    }

    public boolean hasKit(Player player, Kit kit) {
        PlayerData playerData = _playerDataComponent.getPlayerData(player);
        String kits = playerData.getData("Kits", String.class);

        return !kits.split(":")[kit.getDBPos()].equals("N");
    }

    public boolean isDefault(Player player, Class<? extends Kit> kitClass) {
        return isDefault(player, getKit(kitClass));
    }

    public boolean isDefault(Player player, Kit kit) {
        PlayerData playerData = _playerDataComponent.getPlayerData(player);
        String kits = playerData.getData("Kits", String.class);

        return kits.split(":")[kit.getDBPos()].equals("D");
    }

    @SuppressWarnings("unchecked")
    public <T> T getKit(Class<T> kitClass) {
        for (Kit kit : _kits)
            if (kitClass.isInstance(kit))
                return (T) kit;
        return null;
    }

    public Kit getKit(int dbPos) {
        for (Kit kit : _kits)
            if (kit.getDBPos() == dbPos)
                return kit;
        return _kits.get(_kits.size() - 1);
    }

    public Kit getDefault(Player player) {
        PlayerData data = _playerDataComponent.getPlayerData(player);
        String[] kits = data.getData("Kits", String.class).split(":");
        for (int i = 0; i < kits.length; i++)
            if (kits[i].equals("D"))
                return getKit(i);
        return getKit(1);
    }

    public Kit getCurrent(Player player) {
        return _players.getOrDefault(player, getKit(0));
    }


    @EventHandler
    public void onGameStateChangeEvent(PostGameStateChangeEvent event) {
        if (event.getGameState().equals(GameState.PRE_GAME, GameState.LOBBY))
            for (Player player : Arena.getPlayers())
                setKit(player, getDefault(player));
    }

    @EventHandler
    public void onLoadData(PlayerDataLoadEvent event) {
        Player player = event.getPlayer();
        setKit(player, getDefault(player));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL))
            return;
        if (!getArena().getGameState().equals(GameState.LOBBY))
            return;
        if (!event.getPlayer().getItemInHand().getType().equals(Material.COMPASS))
            return;
        Inventory kitSelector = Bukkit.createInventory(null, 9, "Kit Selector");

        for (Kit kit : _kits)
            kitSelector.setItem(kit.getSlot(), kit.getKitItem(event.getPlayer()));

        event.getPlayer().openInventory(kitSelector);
    }

    @Override
    public void onEnable() {
        registerListener(this);
        _kits = getGame().getInstancePool().getInstances(Kit.class);

        OnCommand coinRunner = new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length != 2)
                    return false;
                PlayerData playerData = _playerDataComponent.getPlayerData(sender);
                if (args[0].equalsIgnoreCase("add")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ChatColor.GREEN);
                    stringBuilder.append(ChatColor.BOLD);
                    stringBuilder.append("You added ");
                    stringBuilder.append(Integer.valueOf(args[1]));
                    stringBuilder.append(" coins to your account!");
                    sender.sendMessage(stringBuilder.toString());
                    playerData.incrementIntBy("Coins", Integer.valueOf(args[1]));
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ChatColor.GREEN);
                    stringBuilder.append(ChatColor.RED);
                    stringBuilder.append("You removed ");
                    stringBuilder.append(Integer.valueOf(args[1]));
                    stringBuilder.append(" coins to your account!");
                    sender.sendMessage(stringBuilder.toString());
                    playerData.decrementIntBy("Coins", Integer.valueOf(args[1]));
                    return true;
                }
                return false;
            }
        };

        OnCommand kitRunner = new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length != 2)
                    return false;

                Kit kit = getKit(Integer.valueOf(args[1]));

                if (args[0].equalsIgnoreCase("remove")) {
                    if (!hasKit(sender, kit)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ChatColor.RED);
                        stringBuilder.append(ChatColor.BOLD);
                        stringBuilder.append("You didn't have the \"");
                        stringBuilder.append(kit.getName());
                        stringBuilder.append("\" kit!");
                        sender.sendMessage(stringBuilder.toString());
                        return true;
                    }

                    setHasKit(sender, false, kit);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ChatColor.RED);
                    stringBuilder.append(ChatColor.BOLD);
                    stringBuilder.append("You no longer have the \"");
                    stringBuilder.append(kit.getName());
                    stringBuilder.append("\" kit!");
                    sender.sendMessage(stringBuilder.toString());
                    return true;
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (hasKit(sender, kit)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(ChatColor.RED);
                        stringBuilder.append(ChatColor.BOLD);
                        stringBuilder.append("You already have the \"");
                        stringBuilder.append(kit.getName());
                        stringBuilder.append("\" kit!");
                        sender.sendMessage(stringBuilder.toString());
                        return true;
                    }

                    setHasKit(sender, true, kit);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(ChatColor.GREEN);
                    stringBuilder.append(ChatColor.BOLD);
                    stringBuilder.append("You now have the \"");
                    stringBuilder.append(kit.getName());
                    stringBuilder.append("\" kit!");
                    sender.sendMessage(stringBuilder.toString());
                    return true;
                }

                return false;
            }
        };

        _commandExecutorComponent.addCommandRunner("kits", kitRunner);
        _commandExecutorComponent.addCommandRunner("coins", coinRunner);
    }

    @Override
    public void onDisable() {
        unregisterListener(this);
    }
}
