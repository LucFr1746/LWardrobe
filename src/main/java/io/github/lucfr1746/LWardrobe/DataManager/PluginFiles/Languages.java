package io.github.lucfr1746.LWardrobe.DataManager.PluginFiles;

import io.github.lucfr1746.LLib.Utils.Config.ConfigAPI;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Languages {

    private final LWardrobe plugin;
    private FileConfiguration langConfig;

    private static String commandExecuteByPlayer = "&cThis command must be executed by player!";
    private static String     inGamePluginPrefix = "&a[LWardrobe] ";

    private static String admin_playerNotFound = "&cCan not find that player!";

    private static boolean player_preventProbeCommandEnabled = true;
    private static String         player_preventProbeCommand = "&fUnknown command. Type \"/help\" for help.";
    private static String              player_invalidCommand = "&fUnknown command. Type \"/wardrobe help\" for help.";
    private static String            player_permissionDenied = "&cYou do not have permission to perform this command.";
    private static String            player_checking_pending = "&cAn admin is checking your wardrobe right now!";
    private static String              player_notEnoughSpace = "&cYou don't have enough space in your inventory!";
    private static String                player_deniedModify = "&cYou cannot modify your equipped armor set!";
    private static String  player_isWearingBindingCurseArmor = "&cYou are wearing binding curse armor, try to remove it and try again later!";

    private static boolean   button_close_enabled = true;
    private static int          button_close_slot = 49;
    private static String       button_close_name = "&cClose";
    private static List<String> button_close_lore = new ArrayList<>();

    private static int          button_nextPage_slot = 53;
    private static String       button_nextPage_name = "&aNext Page";
    private static List<String> button_nextPage_lore = List.of("&ePage %next_page%");

    private static int          button_previousPage_slot = 45;
    private static String       button_previousPage_name = "&aPrevious Page";
    private static List<String> button_previousPage_lore = List.of("&ePage %previous_page%");

    private static String       button_empty_name = "&7Slot %slot%: &cEmpty";
    private static List<String> button_empty_lore = List.of("&7This wardrobe slot contains no", "", "&7armor.");

    private static String       button_ready_name = "&7Slot %slot%: &aReady";
    private static List<String> button_ready_lore = List.of("&7This wardrobe slot is ready to", "&7be equipped.", "", "&eClick to equip this armor set!");

    private static String       button_equipped_name = "&7Slot %slot%: &aEquipped";
    private static List<String> button_equipped_lore = List.of("&7This wardrobe slot contains your", "&7current armor set.", "", "&eClick to unequip this armor set!");

    private static String       lockedSlot_name = "&7Slot %slot%: &cLocked";
    private static List<String> lockedSlot_lore = List.of("&7This wardrobe slot is locked and", "&7cannot be used.", "", "&cRequires %slot_permission_require_prefix%");

    private static String       availableSlot_helmet_name = "&aSlot %slot% Helmet";
    private static List<String> availableSlot_helmet_lore = List.of("&7Place a helmet here to add it", "&7to the armor set.");

    private static String       availableSlot_chestplate_name = "&aSlot %slot% Chestplate";
    private static List<String> availableSlot_chestplate_lore = List.of("&7Place a chestplate here to add", "&7it to the armor set.");

    private static String       availableSlot_leggings_name = "&aSlot %slot% Leggings";
    private static List<String> availableSlot_leggings_lore = List.of("&7Place a pair of leggings here", "&7to add them to the armor set.");

    private static String       availableSlot_boots_name = "&aSlot %slot% Boots";
    private static List<String> availableSlot_boots_lore = List.of("&7Place a pair of boots here to", "&7add them to the armor set.");

    public Languages(LWardrobe plugin) {
        this.plugin = plugin;
        initializeLanguages();
        plugin.getLogger().info("  |- Languages files done!");
    }

    public static String getPlayer_noSpace() {
        return player_notEnoughSpace;
    }

    public static String getPlayer_deniedModify() {
        return player_deniedModify;
    }

    public static String getPlayer_isWearingBindingCurseArmor() {
        return player_isWearingBindingCurseArmor;
    }

    public static boolean isButton_close_enabled() {
        return button_close_enabled;
    }

    public static int getButton_close_slot() {
        return button_close_slot;
    }

    public static String getButton_close_name() {
        return button_close_name;
    }

    public static List<String> getButton_close_lore() {
        return button_close_lore;
    }

    public static int getButton_nextPage_slot() {
        return button_nextPage_slot;
    }

    public static String getButton_nextPage_name() {
        return button_nextPage_name;
    }

    public static List<String> getButton_nextPage_lore() {
        return button_nextPage_lore;
    }

    public static int getButton_previousPage_slot() {
        return button_previousPage_slot;
    }

    public static String getButton_previousPage_name() {
        return button_previousPage_name;
    }

    public static List<String> getButton_previousPage_lore() {
        return button_previousPage_lore;
    }

    public static String getLockedSlot_name() {
        return lockedSlot_name;
    }

    public static List<String> getLockedSlot_lore() {
        return lockedSlot_lore;
    }

    public static String getAvailableSlot_helmet_name() {
        return availableSlot_helmet_name;
    }

    public static List<String> getAvailableSlot_helmet_lore() {
        return availableSlot_helmet_lore;
    }

    public static String getButton_empty_name() {
        return button_empty_name;
    }

    public static List<String> getButton_empty_lore() {
        return button_empty_lore;
    }

    public static String getButton_ready_name() {
        return button_ready_name;
    }

    public static List<String> getButton_ready_lore() {
        return button_ready_lore;
    }

    public static String getButton_equipped_name() {
        return button_equipped_name;
    }

    public static List<String> getButton_equipped_lore() {
        return button_equipped_lore;
    }

    public static String getAvailableSlot_chestplate_name() {
        return availableSlot_chestplate_name;
    }

    public static List<String> getAvailableSlot_chestplate_lore() {
        return availableSlot_chestplate_lore;
    }

    public static String getAvailableSlot_leggings_name() {
        return availableSlot_leggings_name;
    }

    public static List<String> getAvailableSlot_leggings_lore() {
        return availableSlot_leggings_lore;
    }

    public static String getAvailableSlot_boots_name() {
        return availableSlot_boots_name;
    }

    public static List<String> getAvailableSlot_boots_lore() {
        return availableSlot_boots_lore;
    }

    private void initializeLanguages() {
        File languagesFolder = new ConfigAPI(plugin).createFolder(plugin.getDataFolder().getPath(), "languages");
        new ConfigAPI(plugin).createDefaultYamlFileConfiguration(languagesFolder.getPath(), "languages/", "en_US.yml");

        langConfig = new ConfigAPI(plugin).getYamlConfiguration(languagesFolder.getPath(), Config.getLanguage() + ".yml");
        readLanguageConfig();
    }

    private void readLanguageConfig() {
        configureCommandSettings();
        configurePreviousPageButton();
        configureCloseButton();
        configureNextPageButton();
        configureLockedSlot();
        configureEmptyButton();
        configureReadyButton();
        configureEquippedButton();
        configureHelmetSlot();
    }

    private void configureCommandSettings() {
        commandExecuteByPlayer = langConfig.getString("command-execute-by-player");
        inGamePluginPrefix = langConfig.getString("in-game-plugin-prefix");
        admin_playerNotFound = langConfig.getString("admin.player-not-found");

        player_preventProbeCommandEnabled = langConfig.getBoolean("player.prevent-probe-command-toggle");
        player_preventProbeCommand = langConfig.getString("player.prevent-probe-command");
        player_invalidCommand = langConfig.getString("player.invalid-command");
        player_permissionDenied = langConfig.getString("player.permission-denied");
        player_checking_pending = langConfig.getString("player.checking-pending");
        player_notEnoughSpace = langConfig.getString("player.not-enough-space");
        player_deniedModify = langConfig.getString("player.denied-modify");
        player_isWearingBindingCurseArmor = langConfig.getString("player.is-wearing-binding-curse-armor");
    }

    private void configurePreviousPageButton() {
        button_previousPage_slot = langConfig.getInt("button.previous-page.slot");

        String previousPageButtonName = langConfig.getString("button.previous-page.name");
        button_previousPage_name = "!none!".equals(previousPageButtonName) ? " " : previousPageButtonName;

        if (langConfig.isList("button.previous-page.lore")) {
            button_previousPage_lore = langConfig.getStringList("button.previous-page.lore");
        } else {
            String previousPageButtonLore = langConfig.getString("button.previous-page.lore");
            button_previousPage_lore = "!none!".equals(previousPageButtonLore)
                    ? new ArrayList<>()
                    : Collections.singletonList(previousPageButtonLore);
        }
    }

    private void configureCloseButton() {
        button_close_enabled = langConfig.getBoolean("button.close.enabled");
        button_close_slot = langConfig.getInt("button.close.slot");

        String closeButtonName = langConfig.getString("button.close.name");
        button_close_name = "!none!".equals(closeButtonName) ? " " : closeButtonName;

        if (langConfig.isList("button.close.lore")) {
            button_close_lore = langConfig.getStringList("button.close.lore");
        } else {
            String closeButtonLore = langConfig.getString("button.close.lore");
            button_close_lore = "!none!".equals(closeButtonLore)
                    ? new ArrayList<>()
                    : Collections.singletonList(closeButtonLore);
        }
    }

    private void configureNextPageButton() {
        button_nextPage_slot = langConfig.getInt("button.next-page.slot");

        String nextPageButtonName = langConfig.getString("button.next-page.name");
        button_nextPage_name = "!none!".equals(nextPageButtonName) ? " " : nextPageButtonName;

        if (langConfig.isList("button.next-page.lore")) {
            button_nextPage_lore = langConfig.getStringList("button.next-page.lore");
        } else {
            String nextPageButtonLore = langConfig.getString("button.next-page.lore");
            button_nextPage_lore = "!none!".equals(nextPageButtonLore)
                    ? new ArrayList<>()
                    : Collections.singletonList(nextPageButtonLore);
        }
    }

    private void configureLockedSlot() {
        String lockedSlotName = langConfig.getString("locked-slot.name");
        lockedSlot_name = "!none!".equals(lockedSlotName) ? " " : lockedSlotName;

        if (langConfig.isList("locked-slot.lore")) {
            lockedSlot_lore = langConfig.getStringList("locked-slot.lore");
        } else {
            String lockedSlotLore = langConfig.getString("locked-slot.lore");
            lockedSlot_lore = "!none!".equals(lockedSlotLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(lockedSlotLore);
        }
    }

    private void configureEmptyButton() {
        String emptyButtonName = langConfig.getString("button.empty.name");
        button_empty_name = "!none!".equals(emptyButtonName) ? " " : emptyButtonName;

        if (langConfig.isList("button.empty.lore")) {
            button_empty_lore = langConfig.getStringList("button.empty.lore");
        } else {
            String emptyButtonLore = langConfig.getString("button.empty.lore");
            button_empty_lore = "!none!".equals(emptyButtonLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(emptyButtonLore);
        }
    }

    private void configureReadyButton() {
        String readyButtonName = langConfig.getString("button.ready.name");
        button_ready_name = "!none!".equals(readyButtonName) ? " " : readyButtonName;

        if (langConfig.isList("button.ready.lore")) {
            button_ready_lore = langConfig.getStringList("button.ready.lore");
        } else {
            String readyButtonLore = langConfig.getString("button.ready.lore");
            button_ready_lore = "!none!".equals(readyButtonLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(readyButtonLore);
        }
    }

    private void configureEquippedButton() {
        String equippedButtonName = langConfig.getString("button.equipped.name");
        button_equipped_name = "!none!".equals(equippedButtonName) ? " " : equippedButtonName;

        if (langConfig.isList("button.equipped.lore")) {
            button_equipped_lore = langConfig.getStringList("button.equipped.lore");
        } else {
            String equippedButtonLore = langConfig.getString("button.equipped.lore");
            button_equipped_lore = "!none!".equals(equippedButtonLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(equippedButtonLore);
        }
    }

    private void configureHelmetSlot() {
        String helmetSlotName = langConfig.getString("available-slot.helmet.name");
        availableSlot_helmet_name = "!none!".equals(helmetSlotName) ? " " : helmetSlotName;

        if (langConfig.isList("available-slot.helmet.lore")) {
            availableSlot_helmet_lore = langConfig.getStringList("available-slot.helmet.lore");
        } else {
            String helmetSlotLore = langConfig.getString("available-slot.helmet.lore");
            availableSlot_helmet_lore = "!none!".equals(helmetSlotLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(helmetSlotLore);
        }
    }

    private void configureChestplateSlot() {
        String chestplateSlotName = langConfig.getString("available-slot.chestplate.name");
        availableSlot_chestplate_name = "!none!".equals(chestplateSlotName) ? " " : chestplateSlotName;

        if (langConfig.isList("available-slot.chestplate.lore")) {
            availableSlot_chestplate_lore = langConfig.getStringList("available-slot.chestplate.lore");
        } else {
            String chestplateSlotLore = langConfig.getString("available-slot.chestplate.lore");
            availableSlot_chestplate_lore = "!none!".equals(chestplateSlotLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(chestplateSlotLore);
        }
    }

    private void configureLeggingsSlot() {
        String leggingsSlotName = langConfig.getString("available-slot.leggings.name");
        availableSlot_leggings_name = "!none!".equals(leggingsSlotName) ? " " : leggingsSlotName;

        if (langConfig.isList("available-slot.leggings.lore")) {
            availableSlot_leggings_lore = langConfig.getStringList("available-slot.leggings.lore");
        } else {
            String leggingsSlotLore = langConfig.getString("available-slot.leggings.lore");
            availableSlot_leggings_lore = "!none!".equals(leggingsSlotLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(leggingsSlotLore);
        }
    }

    private void configureBootsSlot() {
        String bootsSlotName = langConfig.getString("available-slot.boots.name");
        availableSlot_boots_name = "!none!".equals(bootsSlotName) ? " " : bootsSlotName;

        if (langConfig.isList("available-slot.boots.lore")) {
            availableSlot_boots_lore = langConfig.getStringList("available-slot.boots.lore");
        } else {
            String bootsSlotLore = langConfig.getString("available-slot.boots.lore");
            availableSlot_boots_lore = "!none!".equals(bootsSlotLore)
                   ? new ArrayList<>()
                    : Collections.singletonList(bootsSlotLore);
        }
    }
}
