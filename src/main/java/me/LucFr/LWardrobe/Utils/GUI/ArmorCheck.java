package me.LucFr.LWardrobe.Utils.GUI;

import me.LucFr.LWardrobe.GUI.GUIItems;
import me.LucFr.LWardrobe.Utils.ServerVersion;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ArmorCheck {

    public static boolean enoughSpace(Player player, int needSpace) {
        int Amount = 0;
        if (player.getInventory().getHelmet() == null) Amount--;
        if (player.getInventory().getChestplate() == null) Amount--;
        if (player.getInventory().getLeggings() == null) Amount--;
        if (player.getInventory().getBoots() == null) Amount--;
        if (ServerVersion.isNewerThan("1.9"))
            if (player.getInventory().getItemInOffHand() != null) Amount--;
        for (ItemStack item : player.getInventory()) {
            if (item == null) Amount++;
            if (Amount >= needSpace) return true;
        }
        return false;
    }

    public static int getAvailableSlotForItem(Inventory inv, ItemStack itemStack) {
        if (getArmorType(itemStack).equals("helmet")) return getAvailableSlot(inv, 0, 8);
        if (getArmorType(itemStack).equals("chestplate")) return getAvailableSlot(inv, 9, 17);
        if (getArmorType(itemStack).equals("leggings")) return getAvailableSlot(inv, 18, 26);
        if (getArmorType(itemStack).equals("boots")) return getAvailableSlot(inv, 27, 35);
        return -1;
    }

    public static String getArmorType(ItemStack itemStack) {
        if (itemStack == null) return "none";
        String type = itemStack.getType().name();
        if (type.contains("STAINED_GLASS_PANE")) return "none";
        if (!blackList(itemStack)) return "none";
        if (type.endsWith("_HELMET") || type.endsWith("_SKULL") || type.endsWith("_HEAD") || type.endsWith("SKULL_ITEM")) return "helmet";
        else if (type.endsWith("_CHESTPLATE") || type.equals("ELYTRA")) return "chestplate";
        else if (type.endsWith("_LEGGINGS")) return "leggings";
        else if (type.endsWith("_BOOTS")) return "boots";
        if (check(itemStack)) return "helmet";
        return "none";
    }

    public static String getSlotType(int slot) {
        String type = "none";
        if (slot >= 0 && slot <= 8) type = "Helmet";
        else if (slot >= 9 && slot <= 17) type = "Chestplate";
        else if (slot >= 18 && slot <= 26) type = "Leggings";
        else if (slot >= 27 && slot <= 35) type = "Boots";
        else if (slot >= 36 && slot <= 44) type = "Button";
        else if (slot >= 45 && slot <= 53) type = "Background";
        return type;
    }

    public static boolean isSlotStillHaveArmor(Inventory inv, int clickedSlot) {
        if (!getArmorType(inv.getItem(ButtonCheck.getButtonSlot(clickedSlot) - 9)).equals("none")) return false;
        if (!getArmorType(inv.getItem(ButtonCheck.getButtonSlot(clickedSlot) - 18)).equals("none")) return false;
        if (!getArmorType(inv.getItem(ButtonCheck.getButtonSlot(clickedSlot) - 27)).equals("none")) return false;
        return getArmorType(inv.getItem(ButtonCheck.getButtonSlot(clickedSlot) - 36)).equals("none");
    }

    public static boolean canPlaceableSlot(ItemStack itemStack, int clickedSlot) {
        if (getArmorType(itemStack).equals("helmet") && clickedSlot >= 0 && clickedSlot <= 8) return true;
        if (getArmorType(itemStack).equals("chestplate") && clickedSlot >= 9 && clickedSlot <= 17) return true;
        if (getArmorType(itemStack).equals("leggings") && clickedSlot >= 18 && clickedSlot <= 26) return true;
        return getArmorType(itemStack).equals("boots") && clickedSlot >= 27 && clickedSlot <= 35;
    }

    private static boolean check(ItemStack itemStack) {
        if (!GUIItems.config.getString("Allow-Item.Helmet.Lore").equals("none") && itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null) {
            List<String> itemStackLore = itemStack.getItemMeta().getLore();
            for (String lore : itemStackLore) {
                if (lore.contains(GUIItems.config.getString("Allow-Item.Helmet.Lore"))) return true;
            }
        }
        if (GUIItems.config.getString("Allow-Item.Helmet.Type").equals("none")) return false;
        List<String> itemStackType = GUIItems.config.getStringList("Allow-Item.Helmet.Type");
        for (String type : itemStackType) {
            if (itemStack.getType().name().contains(type)) return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private static boolean blackList(ItemStack itemStack) {
        if (!GUIItems.config.getString("Black-List.Lore").equals("none")) {
            List<String> itemStackLore = itemStack.getItemMeta().getLore();
            for (String lore : itemStackLore) {
                if (lore.contains(GUIItems.config.getString("Black-List.Lore"))) return false;
            }
        }
        if (!GUIItems.config.getString("Black-List.Enchantment").equals("none")) {
            for (String enchantment : GUIItems.config.getStringList("Black-List.Enchantment")) {
                if (itemStack.containsEnchantment(Enchantment.getByName(enchantment))) return false;
            }
        }
        return true;
    }

    private static int getAvailableSlot(Inventory inv, int from, int to) {
        for (int i = from; i <= to; i++) {
            if (inv.getItem(i).isSimilar(GUIItems.getDisableBackground())) continue;
            if (getArmorType(inv.getItem(i)).equals("none")) return i;
        }
        return -1;
    }
}
