package me.exerosis.game.engine.implementation.trialtwo.components.kit;

import me.exerosis.component.event.EventListener;
import me.exerosis.game.engine.core.Game;
import me.exerosis.game.engine.core.GameComponent;
import me.exerosis.game.engine.core.state.GameState;
import me.exerosis.game.engine.implementation.trialtwo.components.command.CommandExecutorComponent;
import me.exerosis.game.engine.implementation.trialtwo.components.command.OnCommand;
import me.exerosis.game.engine.implementation.trialtwo.components.player.data.PlayerData;
import me.exerosis.game.engine.implementation.trialtwo.components.player.data.PlayerDataComponent;
import me.exerosis.game.engine.implementation.trialtwo.event.GameStateChangeEvent;
import me.exerosis.game.engine.implementation.trialtwo.event.player.data.PlayerDataLoadEvent;
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

public class KitsComponent extends GameComponent {
    private Map<Player, Kit> _players = new HashMap<>();
    private List<Kit> _kits;
    private CommandExecutorComponent _commandExecutorComponent;
    private PlayerDataComponent _playerDataComponent;

    public KitsComponent(Game game, CommandExecutorComponent commandExecutorComponent, PlayerDataComponent playerDataComponent) {
        super(game);
        _commandExecutorComponent = commandExecutorComponent;
        _playerDataComponent = playerDataComponent;
    }

    //TODO make sure the _players list works! Make a disable!

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
        if (getGameState().equals(GameState.LOBBY, GameState.RESTARTING))
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


    @EventListener(postEvent = true)
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        if (event.getNewGameState().equals(GameState.PRE_GAME, GameState.LOBBY))
            for (Player player : getPlayers())
                setKit(player, getDefault(player));
    }

    @EventListener
    public void onLoadData(PlayerDataLoadEvent event) {
        Player player = event.getPlayer();
        setKit(player, getDefault(player));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.PHYSICAL))
            return;
        if (!getGameState().equals(GameState.LOBBY))
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
        registerListener();
        _kits = getGame().getInstancePool().getInstances(Kit.class);

        OnCommand coinRunner = new OnCommand() {
            @Override
            public boolean run(String[] args, Player sender) {
                if (args.length != 2)
                    return false;
                PlayerData playerData = _playerDataComponent.getPlayerData(sender);
                if (args[0].equalsIgnoreCase("add")) {
                    sender.sendMessage(String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + "You added " + Integer.valueOf(args[1]) + " coins to your account!");
                    playerData.incrementIntBy("Coins", Integer.valueOf(args[1]));
                    return true;
                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    sender.sendMessage(String.valueOf(ChatColor.GREEN) + ChatColor.RED + "You removed " + Integer.valueOf(args[1]) + " coins to your account!");
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
                        sender.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "You didn't have the \"" + kit.getName() + "\" kit!");
                        return true;
                    }

                    setHasKit(sender, false, kit);
                    sender.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "You no longer have the \"" + kit.getName() + "\" kit!");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("add")) {
                    if (hasKit(sender, kit)) {
                        sender.sendMessage(String.valueOf(ChatColor.RED) + ChatColor.BOLD + "You already have the \"" + kit.getName() + "\" kit!");
                        return true;
                    }

                    setHasKit(sender, true, kit);
                    sender.sendMessage(String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + "You now have the \"" + kit.getName() + "\" kit!");
                    return true;
                }

                return false;
            }
        };

        _commandExecutorComponent.addCommandRunner("kits", kitRunner);
        _commandExecutorComponent.addCommandRunner("coins", coinRunner);
        super.onEnable();
    }
}