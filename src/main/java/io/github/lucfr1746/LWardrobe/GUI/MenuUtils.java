package io.github.lucfr1746.LWardrobe.GUI;

import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Config;
import io.github.lucfr1746.Shade.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MenuUtils {

    public boolean isAllowHelmet(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDeniedItem(itemStack)) {
            return false;
        }

        // Check if the item's type is in the allowed helmet types
        if (Config.getAllowedHelmetTypes().contains(itemStack.getType())) {
            return true;
        }

        // Check if the item has lore containing the allowed string
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            List<String> lores = itemMeta.getLore();
            if (lores != null && lores.stream().anyMatch(lore -> lore.contains(Config.getAllowIfHelmetHasLore()))) {
                return true;
            }
        }

        // Check if the item's NBT contains the allowed key-value pair
        return NBT.modify(itemStack, nbt ->
                nbt.hasTag(Config.getAllowedNbtKey()) &&
                        Config.getAllowedNbtValue().equals(nbt.getString(Config.getAllowedNbtKey()))
        );
    }

    public boolean isAllowChestplate(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDeniedItem(itemStack)) {
            return false;
        }

        // Check if the item's type is in the allowed chestplate types
        return Config.getAllowedChestplateTypes().contains(itemStack.getType());
    }

    public boolean isAllowLeggings(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDeniedItem(itemStack)) {
            return false;
        }

        // Check if the item's type is in the allowed leggings types
        return Config.getAllowedLeggingsTypes().contains(itemStack.getType());
    }

    public boolean isAllowBoots(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || isDeniedItem(itemStack)) {
            return false;
        }

        // Check if the item's type is in the allowed boots types
        return Config.getAllowedBootsTypes().contains(itemStack.getType());
    }

    public boolean isDeniedItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return true;
        }
        // Check if the item has enchantments in the denied list
        for (String enchant : Config.getDeniedEnchantments()) {
            if (itemStack.getEnchantments().containsKey(Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchant)))) return true;
        }

        // Check if the item has lore containing the allowed string
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            List<String> lores = itemMeta.getLore();
            if (lores != null && lores.stream().anyMatch(lore -> lore.contains(Config.getDenyLore()))) {
                return true;
            }
        }

        // Check if the item's NBT contains the allowed key-value pair
        return NBT.modify(itemStack, nbt ->
                nbt.hasTag(Config.getDeniedNbtKey()) &&
                        Config.getDeniedNbtValue().equals(nbt.getString(Config.getDeniedNbtKey()))
        );
    }

    public ItemStack[] defineArmors(ItemStack[] armors) {
        armors[0] =     isAllowHelmet(armors[0]) ? armors[0] : null;
        armors[1] = isAllowChestplate(armors[1]) ? armors[1] : null;
        armors[2] =   isAllowLeggings(armors[2]) ? armors[2] : null;
        armors[3] =      isAllowBoots(armors[3]) ? armors[3] : null;
        return armors;
    }

    public boolean isEmptyArmors(ItemStack[] armors) {
        for (ItemStack armor : armors) {
            if (armor != null && armor.getType() != Material.AIR) return false;
        }
        return true;
    }

    public boolean isArmorAllowed(SlotType slotType, ItemStack armor) {
        if (isDeniedItem(armor)) return false;
        return switch (slotType) {
            case HELMET -> isAllowHelmet(armor);
            case CHESTPLATE -> isAllowChestplate(armor);
            case LEGGINGS -> isAllowLeggings(armor);
            case BOOTS -> isAllowBoots(armor);
            default -> false;
        };
    }
}
