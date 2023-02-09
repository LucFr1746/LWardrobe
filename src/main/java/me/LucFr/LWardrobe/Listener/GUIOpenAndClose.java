package me.LucFr.LWardrobe.Listener;

import me.LucFr.LWardrobe.DataManager.Files.PlayerData;
import me.LucFr.LWardrobe.GUI.GUIItems;
import me.LucFr.LWardrobe.GUI.WardrobeMenu;
import me.LucFr.LWardrobe.LWardrobe;
import me.LucFr.LWardrobe.Utils.DataConvert;
import me.LucFr.LWardrobe.Utils.GUI.ArmorCheck;
import me.LucFr.LWardrobe.Utils.GUI.ButtonCheck;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIOpenAndClose implements Listener {

    public LWardrobe plugin;
    private Map<String, Integer> itemStacksMap = Map.of("helmet", 0, "chestplate", 1, "leggings", 2, "boots", 3);
    static HashMap<UUID, Integer> currentPage = new HashMap<>();

    public GUIOpenAndClose(LWardrobe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(@NotNull InventoryOpenEvent event) {
        if (event.getInventory() == null) return;
        if (!(event.getInventory().getHolder() instanceof WardrobeMenu)) return;
        currentPage.put(event.getPlayer().getUniqueId(), WardrobeMenu.getPlayerCurrentPage((Player) event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClose(@NotNull InventoryCloseEvent event) {
        if (event.getInventory() == null) return;
        if (!(event.getInventory().getHolder() instanceof WardrobeMenu)) return;
        PlayerData data = new PlayerData(event.getPlayer().getUniqueId());
        YamlConfiguration playerData = data.getPlayerData();
        for (int i = 0; i <= 8; i++) {
            ItemStack[] itemStacks = new ItemStack[4];
            for (int j = i; j <= 35; j+= 9) {
                if (!ArmorCheck.getArmorType(event.getInventory().getItem(j)).equals("none")) {
                    itemStacks[itemStacksMap.get(ArmorCheck.getArmorType(event.getInventory().getItem(j)))] = event.getInventory().getItem(j);
                }
            }

            int slotHolder = GUIItems.getSlotHolder(i, currentPage.get(event.getPlayer().getUniqueId()));
            playerData.set("Slot-" + slotHolder + ".Armors", DataConvert.itemStackArrayToBase64(itemStacks));
            playerData.set("Slot-" + slotHolder + ".Button", ButtonCheck.getButtonType(event.getInventory().getItem(i + 36)));
        }
        data.saveData();
    }
}
