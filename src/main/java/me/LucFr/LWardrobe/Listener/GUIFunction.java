package me.LucFr.LWardrobe.Listener;

import me.LucFr.LWardrobe.FileManager.Lang;
import me.LucFr.LWardrobe.GUI.GUIItems;
import me.LucFr.LWardrobe.GUI.WardrobeMenu;
import me.LucFr.LWardrobe.LWardrobe;
import me.LucFr.LWardrobe.Utils.GUI.ArmorCheck;
import me.LucFr.LWardrobe.Utils.GUI.ButtonCheck;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GUIFunction implements Listener {

    public static WardrobeMenu menu;
    public LWardrobe plugin;

    public GUIFunction(LWardrobe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        menu = new WardrobeMenu();
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getInventory().getHolder() instanceof WardrobeMenu)) return;
        Player clicker = (Player) event.getWhoClicked();
        int clickedSlot = event.getSlot();
        // put in item shortcut
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            // cancel if shift-clicked item is not armor or is null
            if (event.getClick() != ClickType.SHIFT_LEFT && event.getClick() != ClickType.SHIFT_RIGHT) return;
            if (event.getCurrentItem() == null) return;
            if (ArmorCheck.getArmorType(event.getCurrentItem()).equals("none")) {
                event.setCancelled(true);
                return;
            }
            int availableSlot = ArmorCheck.getAvailableSlotForItem(event.getInventory(), event.getCurrentItem());
            if (availableSlot == -1) return;
            event.getInventory().setItem(availableSlot, event.getCurrentItem());
            event.setCurrentItem(null);
            if (ButtonCheck.getCurrentButton(event.getInventory(), availableSlot).equals("empty")) {
                event.getInventory().setItem(ButtonCheck.getButtonSlot(availableSlot),
                        GUIItems.getReadyButton(clicker, GUIItems.getSlotHolder(availableSlot + 1, WardrobeMenu.getPlayerCurrentPage(clicker))));
            }
            clicker.updateInventory();
        // wardrobe gui function
        } else {
            event.setCancelled(true);
            Inventory wardrobeInv = event.getClickedInventory();
            // armor slot
            if (clickedSlot >= 0 && clickedSlot <= 35) {
                ItemStack currentItem = wardrobeInv.getItem(clickedSlot);
                ItemStack cursorItem = event.getCursor();
                // no modify armor slot
                if (ButtonCheck.isEquipped(wardrobeInv, clickedSlot)) {
                    event.getWhoClicked().sendMessage(Lang.deniedModifyArmor);
                    return;
                }
                // take out shortcut
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    if (ArmorCheck.getArmorType(currentItem).equalsIgnoreCase("none")) return;
                    if (!ArmorCheck.enoughSpace(clicker, 1)) return;
                    clicker.getInventory().addItem(currentItem);
                    wardrobeInv.setItem(clickedSlot, GUIItems.getAvailableSlot(clicker, clickedSlot,WardrobeMenu.getPlayerCurrentPage(clicker)));
                    clicker.updateInventory();
                    if (ArmorCheck.isSlotStillHaveArmor(wardrobeInv, clickedSlot))
                        wardrobeInv.setItem(ButtonCheck.getButtonSlot(clickedSlot), GUIItems.getEmptyButton(clicker, GUIItems.getSlotHolder(clickedSlot + 1, WardrobeMenu.getPlayerCurrentPage(clicker))));
                    return;
                }
                // put armor in by click
                if (ArmorCheck.getArmorType(currentItem).equalsIgnoreCase("none")) {
                    if (ArmorCheck.getArmorType(cursorItem).equalsIgnoreCase("none")) return;
                    if (!ArmorCheck.canPlaceableSlot(cursorItem, clickedSlot)) return;
                    wardrobeInv.setItem(clickedSlot, cursorItem);
                    clicker.setItemOnCursor(null);
                    if (ButtonCheck.getCurrentButton(wardrobeInv, clickedSlot).equals("empty"))
                        wardrobeInv.setItem(ButtonCheck.getButtonSlot(clickedSlot), GUIItems.getReadyButton(clicker, GUIItems.getSlotHolder(clickedSlot + 1, WardrobeMenu.getPlayerCurrentPage(clicker))));
                } else {
                    if (ArmorCheck.getArmorType(cursorItem).equalsIgnoreCase("none")) {
                        clicker.setItemOnCursor(currentItem);
                        wardrobeInv.setItem(clickedSlot, new GUIItems((LWardrobe) LWardrobe.getInstance).getGUIItem(clicker, clickedSlot + 1, WardrobeMenu.getPlayerCurrentPage(clicker)));
                        if (ArmorCheck.isSlotStillHaveArmor(wardrobeInv, clickedSlot))
                            wardrobeInv.setItem(ButtonCheck.getButtonSlot(clickedSlot), GUIItems.getEmptyButton(clicker, GUIItems.getSlotHolder(clickedSlot + 1, WardrobeMenu.getPlayerCurrentPage(clicker))));
                        return;
                    }
                    if (currentItem.isSimilar(cursorItem)) event.setCancelled(false);
                }
            }
            // slot button function
            if (clickedSlot >= 36 && clickedSlot <= 44) {
                String currentButton = ButtonCheck.getCurrentButton(wardrobeInv, clickedSlot);
                if (!currentButton.equals("equipped") && !currentButton.equals("ready")) return;
                clicker.sendMessage("Test");
            // other buttons function
            } else if (clickedSlot >= 45 && clickedSlot <= 53) {
                ItemStack clickedItem = wardrobeInv.getItem(clickedSlot);
                Material clickedItemType = clickedItem.getType();
                if (GUIItems.lang.contains("Button.Next-Page.Slot") && clickedSlot == GUIItems.lang.getInt("Button.Next-Page.Slot") && clickedItemType == Material.ARROW) {
                    menu.openPlayerWardrobe(clicker, WardrobeMenu.playerCurrentPage.get(clicker.getUniqueId()) + 1);
                    clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 0.2F, 1);
                } else if (GUIItems.lang.getBoolean("Button.Close.Enable") && GUIItems.lang.contains("Button.Close.Slot") && clickedSlot == GUIItems.lang.getInt("Button.Close.Slot") && clickedItemType == Material.BARRIER) {
                    clicker.closeInventory();
                    clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 0.2F, 1);
                } else if (GUIItems.lang.getBoolean("Button.Go-Back.Enable") && GUIItems.lang.contains("Button.Go-Back.Slot") && clickedSlot == GUIItems.lang.getInt("Button.Go-Back.Slot") && clickedItemType == Material.ARROW && !Lang.goBackCommand.equalsIgnoreCase("none")) {
                    clicker.performCommand(Lang.goBackCommand);
                    clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 0.2F, 1);
                } else if (GUIItems.lang.contains("Button.Previous-Page.Slot") && clickedSlot == GUIItems.lang.getInt("Button.Previous-Page.Slot") && clickedItemType == Material.ARROW) {
                    menu.openPlayerWardrobe(clicker, WardrobeMenu.playerCurrentPage.get(clicker.getUniqueId()) - 1);
                    clicker.playSound(clicker.getLocation(), Sound.UI_BUTTON_CLICK, 0.2F, 1);
                }
            }
        }
    }
}
