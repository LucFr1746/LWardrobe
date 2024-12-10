package io.github.lucfr1746.LWardrobe.Commands;

import io.github.lucfr1746.LLib.Inventory.InventoryListener;
import io.github.lucfr1746.LLib.Inventory.InventoryManager;
import io.github.lucfr1746.LLib.Player.PlayerAPI;
import io.github.lucfr1746.LWardrobe.GUI.LWardrobeMenu;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandsRegister implements CommandExecutor {

    private final LWardrobe plugin;
    private final InventoryManager inventoryManager;

    public CommandsRegister(LWardrobe plugin) {
        this.plugin = plugin;
        this.inventoryManager = new InventoryManager();
        InventoryListener guiListener = new InventoryListener(inventoryManager);
        Bukkit.getPluginManager().registerEvents(guiListener, this.plugin);
        Objects.requireNonNull(plugin.getCommand("wardrobe")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            return playerExecute(sender, args);
        } else {
            return consoleExecute(sender, args);
        }
    }

    private boolean consoleExecute(@NotNull CommandSender sender, String[] args) {
        return false;
    }

    private boolean playerExecute(@NotNull CommandSender sender, String[] args) {
        PlayerAPI player = new PlayerAPI((Player) sender);
        switch (args.length) {
            case 0 -> {
                this.inventoryManager.openGUI(this.plugin , new LWardrobeMenu(this.plugin, 1), player.getPlayer());
            }
            default -> {
                // Invalid command usage
                player.sendMessage("Invalid command usage. Use /wardrobe [page]");
                return false;
            }
        }
        return false;
    }
}
