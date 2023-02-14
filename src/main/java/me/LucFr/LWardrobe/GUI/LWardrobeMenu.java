package me.LucFr.LWardrobe.GUI;

import me.LucFr.LWardrobe.DataManager.Files.PlayerData;
import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.Utils.DataConverter;
import me.LucFr.LWardrobe.Utils.GUI.ButtonCheck;
import me.LucFr.LWardrobe.Utils.TextWork;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class LWardrobeMenu implements InventoryHolder {

    public static HashMap<UUID, Integer> playerCurrentPage = new HashMap<>();

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }

    public LWardrobeMenu() {
        new GUIItems();
    }

    public void openPlayerWardrobe(Player player, int page) {
        String name = Config.title.replace("%Current_Page%", Integer.toString(page));
        name = name.replace("%Total_Page%", Integer.toString(Config.totalPage));
        name = TextWork.Convert(player, name);
        Inventory GUI = Bukkit.createInventory(this, 54, name);

        PlayerData data = new PlayerData(player.getPlayer().getUniqueId());
        YamlConfiguration playerData = data.getPlayerData();
        if (ButtonCheck.getEquippedSlot(playerData) != -1) {
            playerData.set("Slot-" + ButtonCheck.getEquippedSlot(playerData) + ".Armors", DataConverter.itemStackArrayToBase64(player.getInventory().getArmorContents()));
            data.saveData();
        }

        GUIItems guiItems = new GUIItems(player);
        for (int slot = 0; slot <= 8; slot++) {
            ArrayList<Integer> armorSlots = new ArrayList<>(Arrays.asList(slot, slot + 9, slot + 18, slot + 27));
            for (Integer armorSlot : armorSlots) {
                ItemStack guiItem = guiItems.getGUIItem(player, armorSlot, page);
                GUI.setItem(armorSlot, guiItem);
            }
            GUI.setItem(slot + 36, guiItems.getGUIItem(player, slot + 36, page));
            GUI.setItem(slot + 45, guiItems.getGUIItem(player, slot + 45, page));
        }
        playerCurrentPage.put(player.getUniqueId(), page);
        player.openInventory(GUI);
    }

    public static int getPlayerCurrentPage(Player player) {
        return playerCurrentPage.get(player.getUniqueId());
    }
}
