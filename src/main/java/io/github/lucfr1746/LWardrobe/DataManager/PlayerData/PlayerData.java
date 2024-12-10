package io.github.lucfr1746.LWardrobe.DataManager.PlayerData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.lucfr1746.LLib.Inventory.InventoryButton;
import io.github.lucfr1746.LLib.Utils.Config.ConfigAPI;
import io.github.lucfr1746.LWardrobe.GUI.MenuUtils;
import io.github.lucfr1746.LWardrobe.GUI.SlotType;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import io.github.lucfr1746.Shade.nbtapi.NBT;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public class PlayerData {

    private final LWardrobe plugin;
    private final Player player;
    private final File playerFile;
    private final FileConfiguration playerDataConfiguration;

    public PlayerData(LWardrobe plugin, Player player) {
        this.plugin = plugin;
        this.player = player;

        ConfigAPI configAPI = new ConfigAPI(plugin);
        File playerFolder = configAPI.createFolder(plugin.getDataFolder().getPath(), "player-data");
        this.playerDataConfiguration = configAPI.getOrCreateYamlConfiguration(playerFolder.getPath(), this.player.getUniqueId() + ".yml");
        this.playerFile = configAPI.getFile(playerFolder.getPath(), this.player.getUniqueId() + ".yml");
    }

    public FileConfiguration getPlayerDataConfiguration() {
        return this.playerDataConfiguration;
    }

    public File getPlayerFile() {
        return this.playerFile;
    }

    public String getButtonStatus(int page, int slot) {
        return getButtonStatus(getSlotHolder(page, slot));
    }

    public String getButtonStatus(int slotHolder) {
        return getPlayerDataConfiguration().getString("slot-" + slotHolder + ".button", "empty");
    }

    public ItemStack[] getArmors(int page, int slot) {
        return getArmors(getSlotHolder(page, slot));
    }

    public ItemStack[] getArmors(int slotHolder) {
        String nbtString = this.playerDataConfiguration.getString("slot-" + slotHolder + ".armors");
        return parseArmorsFromNBT(nbtString);
    }

    public ItemStack getArmor(SlotType slotType, int page, int slot) {
        return getArmor(slotType, getSlotHolder(page, slot));
    }

    public ItemStack getArmor(SlotType slotType, int slotHolder) {
        ItemStack[] armors = getArmors(slotHolder);
        return switch (slotType) {
            case HELMET -> armors[0];
            case CHESTPLATE -> armors[1];
            case LEGGINGS -> armors[2];
            case BOOTS -> armors[3];
            default -> null;
        };
    }

    public void saveData(Inventory inv, int page, Map<Integer, InventoryButton> buttonMap) {
        saveArmors(inv, page);
        saveButton(page, buttonMap);
        savePlayerData();
    }

    private ItemStack[] parseArmorsFromNBT(String nbtString) {
        if (nbtString == null || nbtString.trim().isEmpty()) return new ItemStack[4];
        return NBT.itemStackArrayFromNBT(NBT.parseNBT(nbtString));
    }

    private void savePlayerData() {
        try {
            this.playerDataConfiguration.save(this.playerFile);
        } catch (IOException exception) {
            this.plugin.getLogger().warning("Could not save " + this.player.getDisplayName() + " data: " + this.playerFile);
            throw new RuntimeException(exception);
        }
    }

    private void saveArmors(Inventory inv, int page) {
        MenuUtils menuUtils = new MenuUtils();
        for (int i = 0; i <= 8; i++) {
            ItemStack[] itemStacks = new ItemStack[4];
            itemStacks[0] = getValidArmor(inv.getItem(i), menuUtils::isAllowHelmet);
            itemStacks[1] = getValidArmor(inv.getItem(i + 9), menuUtils::isAllowChestplate);
            itemStacks[2] = getValidArmor(inv.getItem(i + 18), menuUtils::isAllowLeggings);
            itemStacks[3] = getValidArmor(inv.getItem(i + 27), menuUtils::isAllowBoots);

            String nbtString = NBT.itemStackArrayToNBT(itemStacks).toString();
            String prettyJson = formatNBTToJson(nbtString);
            this.playerDataConfiguration.set("slot-" + getSlotHolder(page, i) + ".armors", prettyJson);
        }
    }

    private ItemStack getValidArmor(ItemStack item, Function<ItemStack, Boolean> isAllowed) {
        return isAllowed.apply(item) ? item : null;
    }

    private String formatNBTToJson(String nbtString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(nbtString);
        return gson.toJson(jsonElement);
    }

    private void saveButton(int page, Map<Integer, InventoryButton> buttonMap) {
        for (Map.Entry<Integer, InventoryButton> entry : buttonMap.entrySet()) {
            int slot = entry.getKey();
            if (slot >= 36 && slot <= 44) {
                InventoryButton button = entry.getValue();
                String buttonName = button.getButtonName() == null ? "locked" : button.getButtonName();
                this.playerDataConfiguration.set("slot-" + getSlotHolder(page, slot) + ".button", buttonName);
            }
        }
    }

    private int getSlotHolder(int page, int slot) {
        return slot % 9 + ((page - 1) * 9) + 1;
    }
}