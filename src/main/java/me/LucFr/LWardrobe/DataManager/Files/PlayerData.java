package me.LucFr.LWardrobe.DataManager.Files;


import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.LWardrobe;
import me.LucFr.LWardrobe.Utils.DataConvert;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    HashMap<UUID, File> playerFile = new HashMap<>();
    HashMap<UUID, YamlConfiguration> playerYAML = new HashMap<>();
    UUID targetUUID;
    public PlayerData(UUID targetUUID) {
        this.targetUUID = targetUUID;
        createPlayerYamlFileIfNotExists();
    }
    private void createPlayerYamlFileIfNotExists() {
        playerFile.put(this.targetUUID, new File(LWardrobe.getInstance.getDataFolder(), "data/" + this.targetUUID + ".yml"));
        playerYAML.put(this.targetUUID, YamlConfiguration.loadConfiguration(playerFile.get(this.targetUUID)));
        if (playerFile.get(this.targetUUID).length() == 0) prepareData();
    }
    public void saveData() {
        try {
            playerYAML.get(targetUUID).save(playerFile.get(targetUUID));
        } catch (IOException exception) {
            LWardrobe.getInstance.getLogger().warning("Could not save data to " + playerFile.get(targetUUID));
        }
    }
    public YamlConfiguration getPlayerData() {
        return playerYAML.get(targetUUID);
    }
    public String getPlayerDataPath() {
        if (playerFile.containsKey(targetUUID) && playerFile.get(targetUUID).exists() && playerFile.get(targetUUID).isFile())
            return playerFile.get(targetUUID).getPath();
        return null;
    }
    public String getPlayerDataFileName() {
        if (playerFile.containsKey(targetUUID) && playerFile.get(targetUUID).exists() && playerFile.get(targetUUID).isFile())
            return playerFile.get(targetUUID).toString();
        return null;
    }
    public void prepareData() {
        YamlConfiguration playerData = playerYAML.get(this.targetUUID);
        for (int i = 1; i <= Config.totalSlot; i++) {
            playerData.set("Slot-" + i + ".Armors", DataConvert.itemStackArrayToBase64(new ItemStack[4]));
            playerData.set("Slot-" + i + ".Button", "empty");
        }
    }
}
