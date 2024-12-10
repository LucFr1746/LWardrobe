package io.github.lucfr1746.LWardrobe.GUI;

import io.github.lucfr1746.LLib.Inventory.InventoryBuilderAPI;
import io.github.lucfr1746.LLib.Inventory.InventoryButton;
import io.github.lucfr1746.LLib.Inventory.InventoryListener;
import io.github.lucfr1746.LLib.Inventory.InventoryManager;
import io.github.lucfr1746.LLib.Item.ItemBuilderAPI;
import io.github.lucfr1746.LLib.Player.PlayerAPI;
import io.github.lucfr1746.LWardrobe.DataManager.PlayerData.PlayerData;
import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Config;
import io.github.lucfr1746.LWardrobe.DataManager.PluginFiles.Languages;
import io.github.lucfr1746.LWardrobe.LWardrobe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.List;

public class MenuItems {

    private final LWardrobe plugin;
    private final Player player;
    private final int page;
    private final int guiSlot;
    private final int slotHolder;
    private final PlayerData playerData;

    public MenuItems(LWardrobe plugin, Player player, int page) {
        this(plugin, player, page, -1);
    }

    public MenuItems(LWardrobe plugin, Player player, int page, int guiSlot) {
        this.plugin = plugin;
        this.player = player;
        this.page = page;
        this.guiSlot = guiSlot;
        this.slotHolder = guiSlot % 9 + ((this.page - 1) * 9) + 1;
        this.playerData = new PlayerData(plugin, player);
    }

    public InventoryButton getSlotItem(SlotType slotType) {
        if (Config.getSlotPermission(this.slotHolder) == null || Config.getSlotPermission(this.slotHolder).equals("wardrobe.slot-disabled")) {
            return getBackground();
        }
        if (!this.player.hasPermission(Config.getSlotPermission(this.slotHolder))) {
            return getLockedSlot(slotType);
        }
        if (slotType == SlotType.BUTTON) {
            return getSlotButton();
        }

        if (getEquippedSlot() != this.slotHolder) {
            ItemStack armor = getPlayerArmor(slotType);
            if (new MenuUtils().isArmorAllowed(slotType, armor)) {
                return new InventoryButton()
                        .creator(player -> armor)
                        .consumer(event -> handleArmorButton(event, slotType));
            }
        } else {
            ItemStack armor = switch (slotType) {
                case HELMET -> this.player.getInventory().getArmorContents()[3];
                case CHESTPLATE -> this.player.getInventory().getArmorContents()[2];
                case LEGGINGS -> this.player.getInventory().getArmorContents()[1];
                case BOOTS -> this.player.getInventory().getArmorContents()[0];
                default -> null;
            };
            if (new MenuUtils().isArmorAllowed(slotType, armor)) {
                return new InventoryButton()
                       .creator(player -> armor)
                       .consumer(event -> handleArmorButton(event, slotType));
            }
        }

        return new InventoryButton()
                .creator(player -> getAvailableBackground(slotType))
                .consumer(event -> handleArmorButton(event, slotType));
    }

    private void handleArmorButton(InventoryClickEvent event, SlotType slotType) {
        if (event.getClickedInventory() == null || event.getClick() == ClickType.DOUBLE_CLICK) return;

        Inventory LWardrobeMenu = event.getClickedInventory();
        ItemStack cursor = event.getCursor();
        ItemStack currentItem = event.getCurrentItem();
        MenuUtils menuUtils = new MenuUtils();
        PlayerAPI playerAPI = new PlayerAPI(event.getWhoClicked());

        InventoryBuilderAPI inventoryBuilderAPI = new InventoryManager().getActiveInventoryBuilderAPI(LWardrobeMenu);
        int clickedSlot = event.getSlot();
        int buttonSlot = clickedSlot + getAdjustedButtonSlot(slotType);

        InventoryButton currentButton = inventoryBuilderAPI.getButtonMap().get(buttonSlot);
        if (currentButton != null && currentButton.getButtonName().equals("equipped")) {
            playerAPI.sendColoredMessage(Languages.getPlayer_deniedModify());
            return;
        }

        boolean isCursorEmpty = (cursor == null || cursor.getType() == Material.AIR);
        boolean isCurrentItemAllowed = menuUtils.isArmorAllowed(slotType, currentItem);

        if (isCursorEmpty) {
            if (isCurrentItemAllowed) {
                event.setCancelled(false);
                Bukkit.getScheduler().runTask(this.plugin, () -> updateArmorSlot(LWardrobeMenu, inventoryBuilderAPI, clickedSlot));
            }
        } else if (menuUtils.isArmorAllowed(slotType, cursor)) {
            if (isCurrentItemAllowed) {
                event.setCancelled(false);
            } else {
                LWardrobeMenu.setItem(event.getSlot(), null);
                event.setCancelled(false);
                Bukkit.getScheduler().runTask(this.plugin, () -> inventoryBuilderAPI.updateButton(this.player, buttonSlot, getReadyButton()));
            }
        }
    }

    private void updateArmorSlot(Inventory LWardrobeMenu, InventoryBuilderAPI inventoryBuilderAPI, int clickedSlot) {
        inventoryBuilderAPI.updateButton(this.player, clickedSlot,
                new InventoryButton()
                        .creator(player -> getAvailableBackground(getButtonType(clickedSlot)))
                        .consumer(event -> handleArmorButton(event, getButtonType(clickedSlot)))
        );
        updateButton(LWardrobeMenu, inventoryBuilderAPI, clickedSlot + getAdjustedButtonSlot(getButtonType(clickedSlot)));
    }

    private void updateButton(Inventory LWardrobeMenu, InventoryBuilderAPI inventoryBuilderAPI, int buttonSlot) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            ItemStack[] slotArmors = new ItemStack[4];
            slotArmors[0] = LWardrobeMenu.getItem(buttonSlot - 36);
            slotArmors[1] = LWardrobeMenu.getItem(buttonSlot - 27);
            slotArmors[2] = LWardrobeMenu.getItem(buttonSlot - 18);
            slotArmors[3] = LWardrobeMenu.getItem(buttonSlot - 9);

            MenuUtils menuUtils = new MenuUtils();
            ItemStack[] definedArmors = menuUtils.defineArmors(slotArmors);

            InventoryButton newButton = menuUtils.isEmptyArmors(definedArmors) ? getEmptyButton() : getReadyButton();
            inventoryBuilderAPI.updateButton(this.player, buttonSlot, newButton);
        });
    }

    private InventoryButton getSlotButton() {
        String buttonStatus = new PlayerData(this.plugin, this.player).getButtonStatus(this.slotHolder);
        if (buttonStatus == null || buttonStatus.equals("empty") || buttonStatus.equals("locked")) return getEmptyButton();
        else if (buttonStatus.equals("ready")) return getReadyButton();
        else return getEquippedButton();
    }

    public InventoryButton getReadyButton() {
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.PINK_DYE);
                    itemBuilder.setDisplayName(Languages.getButton_ready_name().replace("%slot%", String.valueOf(this.slotHolder)), true);

                    List<String> updatedLores = Languages.getButton_ready_lore().stream()
                            .map(lore -> lore.replace("%slot%", String.valueOf(this.slotHolder)))
                            .toList();
                    itemBuilder.setLores(updatedLores);

                    return itemBuilder.build();
                })
                .consumer(event -> {
                    if (event.getClickedInventory() == null || event.getClick() == ClickType.DOUBLE_CLICK) return;

                    Inventory LWardrobeMenu = event.getClickedInventory();
                    InventoryBuilderAPI inventoryBuilderAPI = new InventoryManager().getActiveInventoryBuilderAPI(LWardrobeMenu);
                    PlayerAPI playerAPI = new PlayerAPI(event.getWhoClicked());

                    // Check if the player is wearing cursed armor
                    if (playerAPI.isWearingBindingCurseArmor()) {
                        playerAPI.sendColoredMessage(Languages.getPlayer_isWearingBindingCurseArmor());
                        return;
                    }

                    // Handle slot change and armor equip
                    if (changeEquippedSlot(LWardrobeMenu, inventoryBuilderAPI)) {
                        inventoryBuilderAPI.updateButton(this.player, event.getSlot(), getEquippedButton());
                        equipArmors(LWardrobeMenu, event.getSlot());
                        return;
                    }

                    // If no space for armors, notify the player
                    if (!playerAPI.returnPlayerArmors()) {
                        playerAPI.sendColoredMessage(Languages.getPlayer_noSpace());
                        return;
                    }
                    inventoryBuilderAPI.updateButton(this.player, event.getSlot(), getEquippedButton());
                    equipArmors(LWardrobeMenu, event.getSlot());
                })
                .name("ready");
    }

    public InventoryButton getEquippedButton() {
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.LIME_DYE);
                    itemBuilder.setDisplayName(Languages.getButton_equipped_name().replace("%slot%", String.valueOf(this.slotHolder)), true);
                    // Replace %slot% with the slot holder
                    List<String> updatedLores = Languages.getButton_equipped_lore().stream()
                            .map(lore -> lore.replace("%slot%", String.valueOf(this.slotHolder)))
                            .toList();
                    itemBuilder.setLores(updatedLores);
                    return itemBuilder.build();
                })
                .consumer(event -> {
                    if (event.getClickedInventory() == null || event.getClick() == ClickType.DOUBLE_CLICK) return;

                    Inventory LWardrobeMenu = event.getClickedInventory();
                    InventoryBuilderAPI inventoryBuilderAPI = new InventoryManager().getActiveInventoryBuilderAPI(LWardrobeMenu);

                    this.player.getInventory().setHelmet(null);
                    this.player.getInventory().setChestplate(null);
                    this.player.getInventory().setLeggings(null);
                    this.player.getInventory().setBoots(null);
                    updateButton(LWardrobeMenu, inventoryBuilderAPI, event.getSlot());
                })
                .name("equipped");
    }

    public InventoryButton getEmptyButton() {
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.GRAY_DYE);
                    itemBuilder.setDisplayName(Languages.getButton_empty_name().replace("%slot%", String.valueOf(this.slotHolder)), true);
                    // Replace %slot% with the slot holder
                    List<String> updatedLores = Languages.getButton_empty_lore().stream()
                            .map(lore -> lore.replace("%slot%", String.valueOf(this.slotHolder)))
                            .toList();
                    itemBuilder.setLores(updatedLores);
                    return itemBuilder.build();
                })
                .name("empty");
    }

    public InventoryButton getLockedSlot(SlotType slotType) {
        Material material = slotType == SlotType.BUTTON ? Material.RED_DYE : Material.BLACK_STAINED_GLASS_PANE;
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(material);
                    itemBuilder.setDisplayName(Languages.getLockedSlot_name().replace("%slot%", String.valueOf(this.slotHolder)), true);
                    if (Config.isRequirementsPrefixEnabled()) {
                        List<String> updatedLores = Languages.getLockedSlot_lore().stream()
                                .map(lore -> lore.replace("%slot%", String.valueOf(this.slotHolder))
                                        .replace("%slot_permission_require_prefix%", Config.getSlotRequirePrefix(this.slotHolder)))
                                .toList();
                        itemBuilder.setLores(updatedLores);
                    }
                    return itemBuilder.build();
                });
    }

    public InventoryButton getNextPageButton() {
        if (this.page <= 0 || this.page >= Config.getTotalPages()) return getBackground();
        return createNavigationButton(
                Languages.getButton_nextPage_name(),
                Languages.getButton_nextPage_lore(),
                this.page + 1
        );
    }

    public InventoryButton getPreviousPageButton() {
        if (this.page <= 1 || this.page > Config.getTotalPages()) return getBackground();
        return createNavigationButton(
                Languages.getButton_previousPage_name(),
                Languages.getButton_previousPage_lore(),
                this.page - 1
        );
    }

    private InventoryButton createNavigationButton(String displayName, List<String> lores, int targetPage) {
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.ARROW);
                    itemBuilder.setDisplayName(displayName, true);
                    itemBuilder.setLores(
                            lores.stream()
                                    .map(lore -> lore.replace("%next_page%", String.valueOf(this.page + 1))
                                            .replace("%previous_page%", String.valueOf(this.page - 1)))
                                    .toList()
                    );
                    return itemBuilder.build();
                })
                .consumer(event -> {
                    InventoryManager inventoryManager = new InventoryManager();
                    InventoryListener guiListener = new InventoryListener(inventoryManager);
                    Bukkit.getPluginManager().registerEvents(guiListener, this.plugin);
                    new PlayerAPI(this.player).playButtonClickSound();
                    inventoryManager.openGUI(this.plugin, new LWardrobeMenu(this.plugin, targetPage), this.player);
                });
    }

    public InventoryButton getCloseButton() {
        if (!Languages.isButton_close_enabled()) return getBackground();
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.BARRIER);
                    itemBuilder.setDisplayName(Languages.getButton_close_name(), true);
                    itemBuilder.setLores(Languages.getButton_close_lore());
                    return itemBuilder.build();
                })
                .consumer(event -> event.getWhoClicked().closeInventory());
    }

    public InventoryButton getBackground() {
        return new InventoryButton()
                .creator(player -> {
                    ItemBuilderAPI itemBuilder = new ItemBuilderAPI(Material.BLACK_STAINED_GLASS_PANE);
                    itemBuilder.setHideTooltip(true);
                    return itemBuilder.build();
                });
    }

    private ItemStack getPlayerArmor(SlotType slotType) {
        return this.playerData.getArmor(slotType, this.slotHolder);
    }

    private Material getSlotMaterial() {
        return switch (this.guiSlot) {
            case 0 -> Material.RED_STAINED_GLASS_PANE;
            case 1 -> Material.ORANGE_STAINED_GLASS_PANE;
            case 2 -> Material.YELLOW_STAINED_GLASS_PANE;
            case 3 -> Material.LIME_STAINED_GLASS_PANE;
            case 4 -> Material.GREEN_STAINED_GLASS_PANE;
            case 5 -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case 6 -> Material.BLUE_STAINED_GLASS_PANE;
            case 7 -> Material.MAGENTA_STAINED_GLASS_PANE;
            case 8 -> Material.PURPLE_STAINED_GLASS_PANE;
            default -> Material.BLACK_STAINED_GLASS_PANE;
        };
    }

    private SlotType getButtonType(int guiSlot) {
        if (guiSlot >= 0 && guiSlot <= 8) return SlotType.HELMET;
        else if (guiSlot >= 9 && guiSlot <= 17) return SlotType.CHESTPLATE;
        else if (guiSlot >= 18 && guiSlot <= 25) return SlotType.LEGGINGS;
        else if (guiSlot >= 26 && guiSlot <= 34) return SlotType.BOOTS;
        else if (guiSlot >= 36 && guiSlot <= 44) return SlotType.BUTTON;
        else return SlotType.UNKNOWN;
    }

    private int getAdjustedButtonSlot(SlotType slotType) {
        return switch (slotType) {
            case HELMET -> 36;
            case CHESTPLATE -> 27;
            case LEGGINGS -> 18;
            case BOOTS -> 9;
            default -> 0;
        };
    }

    private void equipArmors(Inventory LWardrobeMenu, int buttonSlot) {
        ItemStack[] slotArmors = getSlotArmor(LWardrobeMenu, buttonSlot);
        slotArmors = new MenuUtils().defineArmors(slotArmors);
        this.player.getInventory().setHelmet(slotArmors[0]);
        this.player.getInventory().setChestplate(slotArmors[1]);
        this.player.getInventory().setLeggings(slotArmors[2]);
        this.player.getInventory().setBoots(slotArmors[3]);
    }

    private ItemStack[] getSlotArmor(Inventory LWardrobeMenu, int buttonSlot) {
        ItemStack[] slotArmors = new ItemStack[4];
        slotArmors[0] = LWardrobeMenu.getItem(buttonSlot - 36);
        slotArmors[1] = LWardrobeMenu.getItem(buttonSlot - 27);
        slotArmors[2] = LWardrobeMenu.getItem(buttonSlot - 18);
        slotArmors[3] = LWardrobeMenu.getItem(buttonSlot - 9);
        return slotArmors;
    }

    private boolean changeEquippedSlot(Inventory wardrobeMenu, InventoryBuilderAPI inventoryBuilderAPI) {
        if (updateEquippedArmor()) return true;
        MenuUtils menuUtils = new MenuUtils();
        // Use lambda expression to find and update the first equipped button
        return inventoryBuilderAPI.getButtonMap().entrySet().stream()
                .filter(entry -> "equipped".equals(entry.getValue().getButtonName()))
                .findFirst()
                .map(entry -> {
                    int slot = entry.getKey();
                    boolean isEmpty = menuUtils.isEmptyArmors(menuUtils.defineArmors(getSlotArmor(wardrobeMenu, slot)));
                    InventoryButton newButton = isEmpty ? getEmptyButton() : getReadyButton();
                    inventoryBuilderAPI.updateButton(this.player, slot, newButton);
                    return true;
                })
                .orElse(false); // Return false if no equipped button is found
    }

    private boolean updateEquippedArmor() {
        int firstSlot = this.page * 9 - 8;
        int endSlot = this.page * 9;

        PlayerData playerData = new PlayerData(this.plugin, this.player);
        FileConfiguration playerDataConfig = playerData.getPlayerDataConfiguration();
        MenuUtils menuUtils = new MenuUtils();

        try {
            // Check and update slots outside the current page
            for (int i = 1; i <= Config.getTotalSlots(); i++) {
                if (i < firstSlot || i > endSlot) { // Focus on slots outside the current page range
                    if ("equipped".equals(playerData.getButtonStatus(i))) {
                        boolean isEmpty = menuUtils.isEmptyArmors(playerData.getArmors(i));
                        playerDataConfig.set("slot-" + i + ".button", isEmpty ? "empty" : "ready");
                        playerDataConfig.save(playerData.getPlayerFile());
                        return true; // Early return after updating the first-matched slot
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private int getEquippedSlot() {
        for (int i = 1; i <= Config.getTotalSlots(); i++) {
            if (this.playerData.getButtonStatus(i).equals("equipped")) {
                return i;
            }
        }
        return -1;
    }

    private ItemStack getAvailableBackground(SlotType slotType) {
        String name = switch (slotType) {
            case HELMET -> Languages.getAvailableSlot_helmet_name();
            case CHESTPLATE -> Languages.getAvailableSlot_chestplate_name();
            case LEGGINGS -> Languages.getAvailableSlot_leggings_name();
            case BOOTS -> Languages.getAvailableSlot_boots_name();
            default -> "";
        };

        List<String> lores = switch (slotType) {
            case HELMET -> Languages.getAvailableSlot_helmet_lore();
            case CHESTPLATE -> Languages.getAvailableSlot_chestplate_lore();
            case LEGGINGS -> Languages.getAvailableSlot_leggings_lore();
            case BOOTS -> Languages.getAvailableSlot_boots_lore();
            default -> List.of();
        };

        ItemBuilderAPI itemBuilder = new ItemBuilderAPI(getSlotMaterial());
        itemBuilder.setDisplayName(name.replace("%slot%", String.valueOf(this.slotHolder)), true);
        List<String> updatedLores = lores.stream()
                .map(lore -> lore.replace("%slot%", String.valueOf(this.slotHolder)))
                .toList();
        itemBuilder.setLores(updatedLores);
        return itemBuilder.build();
    }
}
