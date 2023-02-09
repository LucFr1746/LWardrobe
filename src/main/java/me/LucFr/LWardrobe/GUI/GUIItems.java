package me.LucFr.LWardrobe.GUI;

import me.LucFr.LWardrobe.DataManager.Files.PlayerData;
import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.NMS.NMSMaterial;
import me.LucFr.LWardrobe.Utils.DataConvert;
import me.LucFr.LWardrobe.Utils.GUI.ArmorCheck;
import me.LucFr.LWardrobe.Utils.ServerVersion;
import me.LucFr.LWardrobe.Utils.TextWork;
import me.LucFr.LWardrobe.LWardrobe;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIItems {
    static ItemStack disabledBackground = null;
    final HashMap<Integer, String> order = new HashMap<>();

    public static FileConfiguration config;
    public static FileConfiguration lang;
    LWardrobe plugin;
    PlayerData data;
    YamlConfiguration playerData;


    public GUIItems() {
        config = LWardrobe.config.getConfig();
        lang = LWardrobe.lang.getConfig();
        order.put(1, "Helmet");
        order.put(2, "Leggings");
        order.put(3, "Chestplate");
        order.put(4, "Boots");
        disabledBackground = getDisableBackground();
    }
    public GUIItems(LWardrobe plugin) {
        this.plugin = plugin;
    }
    public GUIItems(Player player) {
        this.data = new PlayerData(player.getPlayer().getUniqueId());
        this.playerData = data.getPlayerData();
    }

    public ItemStack getGUIItem(Player player, /* slot is gui slot + 1 */ int slot, int page) {
        // ex: slot = 2; page = 3 => slotHolder = 20
        int slotHolder = getSlotHolder(slot, page);
        if (slot >= 45 && slot <= 53) {
            // previous button
            if (page > 1) {
                if (lang.contains("Button.Previous-Page.Slot") && lang.getInt("Button.Previous-Page.Slot") == slot) {
                    if (lang.getInt("Button.Previous-Page.Slot") >= 45 || lang.getInt("Button.Previous-Page.Slot") <= 53)
                        return getPreviousPageButton(player, page);
                    else throw new NumberFormatException("Previous Page button slot must be between 45 and 53");
                }
            }
            // go back button
            if (lang.getBoolean("Button.Go-Back.Enable") && lang.contains("Button.Go-Back.Slot") && lang.getInt("Button.Go-Back.Slot") == slot) {
                if (lang.getInt("Button.Go-Back.Slot") >= 45 || lang.getInt("Button.Go-Back.Slot") <= 53)
                    return getGoBackButton(player);
                else throw new NumberFormatException("Go Back button slot must be between 45 and 53");
            }
            // close button
            if (lang.getBoolean("Button.Close.Enable") && lang.contains("Button.Close.Slot") && lang.getInt("Button.Close.Slot") == slot) {
                if (lang.getInt("Button.Close.Slot") >= 45 || lang.getInt("Button.Close.Slot") <= 53)
                    return getCloseButton(player);
                else throw new NumberFormatException("Close button slot must be between 45 and 53");
            }
            // next page
            if (page < Config.totalPage) {
                if (lang.contains("Button.Next-Page.Slot") && lang.getInt("Button.Next-Page.Slot") == slot) {
                    if (lang.getInt("Button.Next-Page.Slot") >= 45 || lang.getInt("Button.Next-Page.Slot") <= 53)
                        return getNextPageButton(player, page);
                    else throw new NumberFormatException("Next Page button slot must be between 45 and 53");
                }
            }
            return disabledBackground;
        }
        if (Config.permissions.get(slotHolder) == null || Config.permissions.get(slotHolder).equals("wardrobe.slot-disabled")) return disabledBackground;
        // armors slot
        if (slot >= 0 && slot <= 35) {
            if (!player.hasPermission(Config.permissions.get(slotHolder)))
                return getLockedBackground(player, slotHolder);
            if (getArmor(slot, slotHolder) != null)
                return getArmor(slot, slotHolder);
            return getAvailableSlot(player, slot, page);
        // button
        } else if (slot >= 36 && slot <= 44) {
            if (!player.hasPermission(Config.permissions.get(slotHolder)))
                return getLockedButton(player, slotHolder);
            if (this.playerData.getString("Slot-" + slotHolder + ".Button").equals("equipped")) return getEquippedButton(player, slotHolder);
            if (this.playerData.getString("Slot-" + slotHolder + ".Button").equals("ready")) return getReadyButton(player, slotHolder);
            return getEmptyButton(player, slotHolder);
        }
        return null;
    }

    public ItemStack getArmor(int slot, int slotHolder) {
        int armorSlot = Math.floorDiv(slot, 9);
        String slotBase64 = this.playerData.getString("Slot-" + slotHolder + ".Armors");
        try {
            return DataConvert.itemStackArrayFromBase64(slotBase64)[armorSlot];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getEquippedButton(Player player, int slotHolder) {
        ItemStack button;
        // base
        if (ServerVersion.isNewerThan("1.13"))
            button = new ItemStack(Material.valueOf(NMSMaterial.Material_1_14_newer_Button.get("Equipped")));
        else {
            button = new ItemStack(Material.valueOf("INK_SACK"));
            button.setDurability(NMSMaterial.Material_1_12_older_Button.get("Equipped"));
        }
        ItemMeta buttonMeta = button.getItemMeta();
        // name
        if (!lang.contains("Button.Equipped.Name") || lang.getString("Button.Equipped.Name").length() == 0 || lang.getString("Button.Equipped.Name").equalsIgnoreCase("none"))
            buttonMeta.setDisplayName(" ");
        else
            buttonMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Equipped.Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> buttonLore = new ArrayList<>();
        if (lang.contains("Button.Equipped.Lore") && !lang.getString("Button.Equipped.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Equipped.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                buttonLore.add(TextWork.Convert(player, lore));
            }
        buttonMeta.setLore(buttonLore);
        button.setItemMeta(buttonMeta);
        return button;
    }
    @SuppressWarnings("deprecation")
    public static ItemStack getReadyButton(Player player, int slotHolder) {
        ItemStack button;
        // base
        if (ServerVersion.isNewerThan("1.13"))
            button = new ItemStack(Material.valueOf(NMSMaterial.Material_1_14_newer_Button.get("Ready")));
        else {
            button = new ItemStack(Material.valueOf("INK_SACK"));
            button.setDurability(NMSMaterial.Material_1_12_older_Button.get("Ready"));
        }
        ItemMeta buttonMeta = button.getItemMeta();
        // name
        if (!lang.contains("Button.Ready.Name") || lang.getString("Button.Ready.Name").length() == 0 || lang.getString("Button.Ready.Name").equalsIgnoreCase("none"))
            buttonMeta.setDisplayName(" ");
        else
            buttonMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Ready.Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> buttonLore = new ArrayList<>();
        if (lang.contains("Button.Ready.Lore") && !lang.getString("Button.Ready.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Ready.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                buttonLore.add(TextWork.Convert(player, lore));
            }
        buttonMeta.setLore(buttonLore);
        button.setItemMeta(buttonMeta);
        return button;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getEmptyButton(Player player, int slotHolder) {
        ItemStack button;
        // base
        if (ServerVersion.isNewerThan("1.13"))
            button = new ItemStack(Material.valueOf(NMSMaterial.Material_1_14_newer_Button.get("Empty")));
        else {
            button = new ItemStack(Material.valueOf("INK_SACK"));
            button.setDurability(NMSMaterial.Material_1_12_older_Button.get("Empty"));
        }
        ItemMeta buttonMeta = button.getItemMeta();
        // name
        if (!lang.contains("Button.Empty.Name") || lang.getString("Button.Empty.Name").length() == 0 || lang.getString("Button.Empty.Name").equalsIgnoreCase("none"))
            buttonMeta.setDisplayName(" ");
        else
            buttonMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Empty.Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> buttonLore = new ArrayList<>();
        if (lang.contains("Button.Empty.Lore") && !lang.getString("Button.Empty.Lore").equalsIgnoreCase("none")) {
            for (String lore : lang.getString("Button.Empty.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                buttonLore.add(TextWork.Convert(player, lore));
            }
        }
        buttonMeta.setLore(buttonLore);
        button.setItemMeta(buttonMeta);
        return button;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getLockedButton(Player player, int slotHolder) {
        ItemStack button;
        // base
        if (ServerVersion.isVersion("1.13"))
            button = new ItemStack(Material.valueOf(NMSMaterial.Material_1_13_newer_Button.get("Locked")));
        else if (ServerVersion.isNewerThan("1.14"))
            button = new ItemStack(Material.valueOf(NMSMaterial.Material_1_14_newer_Button.get("Locked")));
        else {
            button = new ItemStack(Material.valueOf("INK_SACK"));
            button.setDurability(NMSMaterial.Material_1_12_older_Button.get("Locked"));
        }
        String requirePrefix = Config.requirePrefix.get(slotHolder);
        ItemMeta buttonMeta = button.getItemMeta();
        // name
        if (!lang.contains("Button.Locked.Name") || lang.getString("Button.Locked.Name").length() == 0 || lang.getString("Button.Locked.Name").equalsIgnoreCase("none"))
            buttonMeta.setDisplayName(" ");
        else
            buttonMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Locked.Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> buttonLore = new ArrayList<>();
        if (lang.contains("Button.Locked.Lore") && !lang.getString("Button.Locked.Lore").equalsIgnoreCase("none")) {
            for (String lore : lang.getString("Button.Locked.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                else lore = lore.replace("%Permission_Require_Prefix%", requirePrefix);
                buttonLore.add(TextWork.Convert(player, lore));
            }
        }
        buttonMeta.setLore(buttonLore);
        button.setItemMeta(buttonMeta);
        return button;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getAvailableSlot(Player player, int slot, int page) {
        int slotHolder = getSlotHolder(slot, page);
        ItemStack backGround;
        // base
        if (ServerVersion.isNewerThan("1.13"))
            backGround = new ItemStack(Material.valueOf(NMSMaterial.Material_1_13_newer.get(slotHolder - ((page - 1) * 9))));
        else {
            backGround = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"));
            backGround.setDurability(NMSMaterial.Material_1_12_older.get(slotHolder - ((page - 1) * 9)));
        }
        ItemMeta backGroundMeta = backGround.getItemMeta();
        String type = ArmorCheck.getSlotType(slot - 1);
        // name
        if (!lang.contains("Available-Slot." + type + ".Name") || lang.getString("Available-Slot." + type + ".Name").length() == 0 || lang.getString("Available-Slot." + type + ".Name").equalsIgnoreCase("none"))
            backGroundMeta.setDisplayName(" ");
        else
            backGroundMeta.setDisplayName(TextWork.Convert(player, lang.getString("Available-Slot." + type + ".Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> backGroundLore = new ArrayList<>();
        if (lang.contains("Available-Slot." + type + ".Lore") && !lang.getString("Available-Slot." + type + ".Lore").equalsIgnoreCase("none")) {
            for (String lore : lang.getString("Available-Slot." + type + ".Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                backGroundLore.add(TextWork.Convert(player, lore));
            }
        }
        backGroundMeta.setLore(backGroundLore);
        backGround.setItemMeta(backGroundMeta);
        return backGround;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getLockedBackground(Player player, int slotHolder) {
        ItemStack backGround;
        // base
        if (ServerVersion.isNewerThan("1.13")) backGround = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        else {
            backGround = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"));
            backGround.setDurability((short) 15);
        }
        String requirePrefix = Config.requirePrefix.get(slotHolder);
        ItemMeta backGroundMeta = backGround.getItemMeta();
        // name
        if (!lang.contains("Locked-Slot.Name") || lang.getString("Locked-Slot.Name").length() == 0 || lang.getString("Locked-Slot.Name").equalsIgnoreCase("none"))
            backGroundMeta.setDisplayName(" ");
        else
            backGroundMeta.setDisplayName(TextWork.Convert(player, lang.getString("Locked-Slot.Name").replace("%Slot%", Integer.toString(slotHolder))));
        // lore
        List<String> backGroundLore = new ArrayList<>();
        if (lang.contains("Locked-Slot.Lore") && !lang.getString("Locked-Slot.Lore").equalsIgnoreCase("none")) {
            for (String lore : lang.getString("Locked-Slot.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                else lore = lore.replace("%Permission_Require_Prefix%", requirePrefix);
                backGroundLore.add(TextWork.Convert(player, lore));
            }
        }
        backGroundMeta.setLore(backGroundLore);
        backGround.setItemMeta(backGroundMeta);
        return backGround;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getDisableBackground() {
        ItemStack backGround;
        if (ServerVersion.isNewerThan("1.13")) backGround = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        else {
            backGround = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"));
            backGround.setDurability((short) 15);
        }
        ItemMeta backGroundMeta = backGround.getItemMeta();
        backGroundMeta.setDisplayName(" ");
        backGround.setItemMeta(backGroundMeta);
        return backGround;

    }

    public static ItemStack getNextPageButton(Player player, int page) {
        // base
        ItemStack nextPage = new ItemStack(Material.ARROW);
        ItemMeta nextPageMeta = nextPage.getItemMeta();
        // name
        if (!lang.contains("Button.Next-Page.Name") || lang.getString("Button.Next-Page.Name").length() == 0 || lang.getString("Button.Next-Page.Name").equalsIgnoreCase("none"))
            nextPageMeta.setDisplayName(" ");
        else
            nextPageMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Next-Page.Name").replace("%Next_Page%", Integer.toString(page + 1))));
        // lore
        List<String> nextPageLore = new ArrayList<>();
        if (lang.contains("Button.Next-Page.Lore") && !lang.getString("Button.Next-Page.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Next-Page.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                lore = lore.replace("%Next_Page%", Integer.toString(page + 1));
                nextPageLore.add(TextWork.Convert(player, lore));
            }
        nextPageMeta.setLore(nextPageLore);
        nextPage.setItemMeta(nextPageMeta);
        return nextPage;
    }

    public static ItemStack getCloseButton(Player player) {
        // base
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        // name
        if (!lang.contains("Button.Close.Name") || lang.getString("Button.Go-Back.Name").length() == 0 || lang.getString("Button.Go-Back.Name").equalsIgnoreCase("none"))
            closeMeta.setDisplayName(" ");
        else
            closeMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Close.Name")));
        // lore
        List<String> closeLore = new ArrayList<>();
        if (lang.contains("Button.Close.Lore") && !lang.getString("Button.Close.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Close.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                closeLore.add(TextWork.Convert(player, lore));
            }
        closeMeta.setLore(closeLore);
        close.setItemMeta(closeMeta);
        return close;
    }

    public static ItemStack getGoBackButton(Player player) {
        // base
        ItemStack goBack = new ItemStack(Material.ARROW);
        ItemMeta goBackMeta = goBack.getItemMeta();
        // name
        if (!lang.contains("Button.Go-Back.Name") || lang.getString("Button.Go-Back.Name").length() == 0 || lang.getString("Button.Go-Back.Name").equalsIgnoreCase("none"))
            goBackMeta.setDisplayName(" ");
        else
            goBackMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Go-Back.Name")));
        // lore
        List<String> goBackLore = new ArrayList<>();
        if (lang.contains("Button.Go-Back.Lore") && !lang.getString("Button.Go-Back.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Go-Back.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                goBackLore.add(TextWork.Convert(player, lore));
            }
        goBackMeta.setLore(goBackLore);
        goBack.setItemMeta(goBackMeta);
        return goBack;
    }

    public static ItemStack getPreviousPageButton(Player player, int page) {
        // base
        ItemStack previousPage = new ItemStack(Material.ARROW);
        ItemMeta previousPageMeta = previousPage.getItemMeta();
        // name
        if (!lang.contains("Button.Previous-Page.Name") || lang.getString("Button.Previous-Page.Name").length() == 0 || lang.getString("Button.Previous-Page.Name").equalsIgnoreCase("none"))
            previousPageMeta.setDisplayName(" ");
        else 
            previousPageMeta.setDisplayName(TextWork.Convert(player, lang.getString("Button.Previous-Page.Name").replace("%Previous_Page%", Integer.toString(page - 1))));
        // lore
        List<String> previousPageLore = new ArrayList<>();
        if (lang.contains("Button.Previous-Page.Lore") && !lang.getString("Button.Previous-Page.Lore").equalsIgnoreCase("none"))
            for (String lore : lang.getString("Button.Previous-Page.Lore").replace("[", "").replace("]", "").split(", ")) {
                if (lore.length() == 0) lore = " ";
                lore = lore.replace("%Previous_Page%", Integer.toString(page -1));
                previousPageLore.add(TextWork.Convert(player, lore));
            }
        previousPageMeta.setLore(previousPageLore);
        previousPage.setItemMeta(previousPageMeta);
        return previousPage;
    }

    public static int getSlotHolder(int slot, int page) {
        return slot % 9 + ((page - 1) * 9) + 1;
    }
}
