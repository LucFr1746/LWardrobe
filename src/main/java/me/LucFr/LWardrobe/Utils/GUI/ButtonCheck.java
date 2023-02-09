package me.LucFr.LWardrobe.Utils.GUI;

import me.LucFr.LWardrobe.FileManager.Config;
import me.LucFr.LWardrobe.Utils.ServerVersion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ButtonCheck {

    public static int getEquippedSlot(YamlConfiguration playerData) {
        for (int i = 1; i <= Config.totalSlot; i++) {
            if (playerData.getString("Slot-" + i + ".Button").equals("equipped")) return i;
        }
        return -1;
    }
    public static int getEquippedSlotInInventory(Inventory inv) {
        for (int i = 0; i < 8; i++) {
            if (isEquipped(inv, i)) return getButtonSlot(i);
        }
        return -1;
    }

    public static boolean isEquipped(Inventory inv, int clickedSlot) {
        return getButtonType(inv, getButtonSlot(clickedSlot)).equals("equipped");
    }

    public static String getCurrentButton(Inventory inv, int clickedSlot) {
        return getButtonType(inv, getButtonSlot(clickedSlot));
    }

    public static int getButtonSlot(int clickedSlot) {
        if (clickedSlot >= 0 && clickedSlot <= 8) return clickedSlot + 36;
        if (clickedSlot >= 9 && clickedSlot <= 17) return clickedSlot + 27;
        if (clickedSlot >= 18 && clickedSlot <= 26) return clickedSlot + 18;
        if (clickedSlot >= 27 && clickedSlot <= 35) return clickedSlot + 9;
        if (clickedSlot >= 36 && clickedSlot <= 44) return clickedSlot;
        return -1;
    }

    private static String getButtonType(Inventory inv, int buttonSlot) {
        return getButtonType(inv.getItem(buttonSlot));
    }
    public static String getButtonType(ItemStack itemStack) {
        String buttonType;
        if (ServerVersion.isNewerThan("1.13")) buttonType = itemStack.getType().toString();
        else buttonType = itemStack.getData().toString();
        if (buttonType.equals("GRAY_DYE") || buttonType.contains("GRAY DYE")) return "empty";
        if (buttonType.equals("PINK_DYE") || buttonType.contains("PINK DYE")) return "ready";
        if (buttonType.equals("LIME_DYE") || buttonType.contains("LIME DYE")) return "equipped";
        if (buttonType.equals("RED_DYE") || buttonType.contains("RED DYE") || buttonType.contains("ROSE RED") || buttonType.contains("ROSE_RED"))  return "locked";
        if (buttonType.equals("BLACK_STAINED_GLASS_PANE") || buttonType.equals("STAINED_GLASS_PANE(15)")) return "disable";
        return "none";
    }
}
