package io.github.lucfr1746.LWardrobe.DataManager.PluginFiles;

import io.github.lucfr1746.LLib.Utils.Config.ConfigAPI;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class Config {

    private final LWardrobe plugin;
    private FileConfiguration config;

    private static boolean      updateCheckerEnabled = true;
    private static String                   language = "en_US";
    private static String            adminPermission = "lwardrobe.admin";
    private static String                   guiTitle = "Wardrobe (%current_page%/%total_page%)";
    private static int                    totalSlots = 18;
    private static int                    totalPages = 2;
    private static boolean requirementsPrefixEnabled = true;

    private static final Map<Integer, String>   slotsPermissions = new HashMap<>();
    private static final Map<Integer, String> slotsRequirePrefix = new HashMap<>();
    private static final List<Material>       allowedHelmetTypes = new ArrayList<>();
    private static final List<Material>   allowedChestplateTypes = new ArrayList<>();
    private static final List<Material>     allowedLeggingsTypes = new ArrayList<>();
    private static final List<Material>        allowedBootsTypes = new ArrayList<>();
    private static String                          allowedNbtKey = "category";
    private static String                        allowedNbtValue = "HELMET";
    private static String                   allowIfHelmetHasLore = "!none!";

    private static final List<String>   deniedEnchantments = new ArrayList<>();
    private static String                     deniedNbtKey = "!none!";
    private static String                   deniedNbtValue = "!none!";
    private static String                         denyLore = "!none!";

    public Config(LWardrobe plugin) {
        this.plugin = plugin;
        initializeConfig();
        plugin.getLogger().info("  |- Config file done!");
    }

    public static boolean isEnabledUpdateChecker() {
        return updateCheckerEnabled;
    }

    public static String getLanguage() {
        return language;
    }

    public static String getAdminPermission() {
        return adminPermission;
    }

    public static String getGuiTitle() {
        return guiTitle;
    }

    public static int getTotalSlots() {
        return totalSlots;
    }

    public static int getTotalPages() {
        return totalPages;
    }

    public static boolean isRequirementsPrefixEnabled() {
        return requirementsPrefixEnabled;
    }

    public static String getSlotPermission(int slot) {
        return slotsPermissions.get(slot);
    }

    public static String getSlotRequirePrefix(int slot) {
        return slotsRequirePrefix.get(slot);
    }

    public static List<Material> getAllowedHelmetTypes() {
        return Collections.unmodifiableList(allowedHelmetTypes);
    }

    public static List<Material> getAllowedChestplateTypes() {
        return Collections.unmodifiableList(allowedChestplateTypes);
    }

    public static List<Material> getAllowedLeggingsTypes() {
        return Collections.unmodifiableList(allowedLeggingsTypes);
    }

    public static List<Material> getAllowedBootsTypes() {
        return Collections.unmodifiableList(allowedBootsTypes);
    }

    public static String getAllowIfHelmetHasLore() {
        return allowIfHelmetHasLore;
    }

    public static List<String> getDeniedEnchantments() {
        return Collections.unmodifiableList(deniedEnchantments);
    }

    public static String getDenyLore() {
        return denyLore;
    }

    public static String getAllowedNbtKey() {
        return allowedNbtKey;
    }

    public static String getAllowedNbtValue() {
        return allowedNbtValue;
    }

    public static String getDeniedNbtKey() {
        return deniedNbtKey;
    }

    public static String getDeniedNbtValue() {
        return deniedNbtValue;
    }

    private void initializeConfig() {
        config = new ConfigAPI(plugin).createDefaultYamlFileConfiguration(plugin.getDataFolder().getPath(), "", "config.yml");
        readConfig();
    }

    private void readConfig() {
        updateCheckerEnabled = config.getBoolean("update-checker");
        language = config.getString("language", "en_US");
        this.plugin.getLogger().info("  |- en_US language file selected!");
        adminPermission = config.getString("admin-permission", "lwardrobe.admin");
        guiTitle = config.getString("wardrobe-gui-title", "Wardrobe (%current_page%/%total_page%)");
        totalSlots = config.getInt("total-slots");
        totalPages = (int) Math.ceil((double) totalSlots / 9);
        requirementsPrefixEnabled = config.getBoolean("requirements-prefix");

        loadSlotPermissionsAndPrefixes();
        loadAllowedItems();
        loadDeniedItems();
    }

    private void loadSlotPermissionsAndPrefixes() {
        slotsPermissions.clear();
        slotsRequirePrefix.clear();

        ConfigurationSection section = config.getConfigurationSection("slots-permission");
        if (section != null) {
            section.getKeys(false).forEach(path -> {
                String[] range = path.split("-");
                int start = Integer.parseInt(range[0]);
                int end = range.length > 1 ? Integer.parseInt(range[1]) : start;

                for (int i = start; i <= end; i++) {
                    slotsPermissions.put(i, section.getString(path + ".permission", "wardrobe.slot-disabled"));
                    slotsRequirePrefix.put(i, section.getString(path + ".requirements-prefix", "wardrobe.slot-disabled"));
                }
            });
        } else {
            assignDefaultSlotPermissions();
        }
        for (int i = totalSlots + 1; i <= slotsPermissions.size(); i++) {
            slotsPermissions.put(i, "wardrobe.slot-disabled");
            slotsRequirePrefix.put(i, "wardrobe.slot-disabled");
        }
    }

    private void assignDefaultSlotPermissions() {
        String[] groups = {"group.default", "group.vip", "group.vip+", "group.mvp", "group.mvp+"};
        String[] prefixes = {"&7DEFAULT", "&aVIP", "&aVIP+", "&bMVP", "&bMVP+"};
        int[] slotsPerGroup = {2, 3, 4, 4, 5};

        int slotIndex = 1;
        for (int i = 0; i < groups.length; i++) {
            for (int j = 0; j < slotsPerGroup[i]; j++) {
                slotsPermissions.put(slotIndex, groups[i]);
                slotsRequirePrefix.put(slotIndex, prefixes[i]);
                slotIndex++;
            }
        }
    }

    private void loadAllowedItems() {
        loadAllowedMaterials("allow-item.helmet-slots.types", allowedHelmetTypes);
        loadAllowedMaterials("allow-item.chestplate-slots.types", allowedChestplateTypes);
        loadAllowedMaterials("allow-item.leggings-slots.types", allowedLeggingsTypes);
        loadAllowedMaterials("allow-item.boots-slots.types", allowedBootsTypes);

        allowIfHelmetHasLore = config.getString("allow-item.helmet-slots.special-checks.lore", "!none!");
        allowedNbtKey = config.getString("allow-item.helmet-slots.special-checks.nbt-tag.key", "!none!");
        allowedNbtValue = config.getString("allow-item.helmet-slots.special-checks.nbt-tag.value", "!none!");
    }

    private void loadAllowedMaterials(String path, List<Material> targetList) {
        targetList.clear();
        config.getStringList(path).forEach(materialName -> {
            try {
                targetList.add(Material.valueOf(materialName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("[LWardrobe] Invalid material in config: " + materialName);
            }
        });
    }

    private void loadDeniedItems() {
        deniedEnchantments.clear();
        deniedEnchantments.addAll(config.getStringList("deny-item.black-list-enchantments"));
        denyLore = config.getString("deny-item.lore", "!none!");

        deniedNbtKey = config.getString("deny-item.nbt-tag.key", "!none!");
        deniedNbtValue = config.getString("deny-item.nbt-tag.value", "!none!");
    }
}
