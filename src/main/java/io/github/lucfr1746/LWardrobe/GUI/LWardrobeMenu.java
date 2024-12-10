package io.github.lucfr1746.LWardrobe.GUI;

import io.github.lucfr1746.LLib.Inventory.InventoryBuilderAPI;
import io.github.lucfr1746.LLib.Inventory.InventoryButton;
import io.github.lucfr1746.LLib.Utils.TextAPI;
import io.github.lucfr1746.LWardrobe.DataManager.PlayerData.PlayerData;
import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Config;
import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Languages;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

public class LWardrobeMenu extends InventoryBuilderAPI {

    private final LWardrobe plugin;

    public LWardrobeMenu(LWardrobe plugin, int page) {
        this.plugin = plugin;
        this.setRows(6);
        this.setCurrentPage(page);
        this.setLockMode(LockMode.GUI_LOCKED);
        this.setTitle(getFinalTitle());
    }

    @Override
    public void decorate(Player player) {
        MenuItems menuItems = new MenuItems(this.plugin, player, getCurrentPage());
        for (int i = 45; i <= 53; i++) {
            this.addButton(i, menuItems.getBackground());
        }
        this.addButton(Languages.getButton_previousPage_slot(), menuItems.getPreviousPageButton());
        this.addButton(Languages.getButton_close_slot(), menuItems.getCloseButton());
        this.addButton(Languages.getButton_nextPage_slot(), menuItems.getNextPageButton());

        for (int slot = 0; slot <= 8; slot++) {
            menuItems = new MenuItems(this.plugin, player, getCurrentPage(), slot);
            this.addButton(slot, menuItems.getSlotItem(SlotType.HELMET));
            this.addButton(slot +  9, menuItems.getSlotItem(SlotType.CHESTPLATE));
            this.addButton(slot + 18, menuItems.getSlotItem(SlotType.LEGGINGS));
            this.addButton(slot + 27, menuItems.getSlotItem(SlotType.BOOTS));
            this.addButton(slot + 36, menuItems.getSlotItem(SlotType.BUTTON));
        }

        super.decorate(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory() instanceof PlayerInventory) {
            return;
        }
        event.setCancelled(true);

        int slot = event.getSlot();
        InventoryButton button = this.getButtonMap().get(slot);
        if (button != null && button.getEventConsumer() != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
        new PlayerData(this.plugin, (Player) event.getPlayer()).saveData(this.getInventory(), this.getCurrentPage(), this.getButtonMap());
    }

    private String getFinalTitle() {
        String title = Config.getGuiTitle();
               title = title.replace("%current_page%", Integer.toString(this.getCurrentPage()));
               title = title.replace("%total_page%", Integer.toString(Config.getTotalPages()));
               title = new TextAPI(title).convert().build();
        return title;
    }
}
