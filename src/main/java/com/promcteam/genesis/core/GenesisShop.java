package com.promcteam.genesis.core;

import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.managers.ClassManager;
import lombok.Getter;
import lombok.Setter;
import com.promcteam.genesis.events.GenesisChoosePageLayoutEvent;
import com.promcteam.genesis.managers.features.PageLayoutHandler;
import com.promcteam.genesis.misc.Misc;
import com.promcteam.genesis.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class GenesisShop {

    public final static int ROWS_LIMIT_CURRENT = ClassManager.manager.getPageLayoutHandler().getMaxRows();
    // By default 6
    public final static int ROWS_LIMIT_TOTAL   = 6;
    public final static int ROW_ITEMS          = 9;

    //////////////////////////// <- Variables

    @Getter
    @Setter
    private String shopName = "BossShop";
    @Getter
    @Setter
    private String signText = "[BossShop]";

    @Getter
    private String   displayName;
    @Getter
    @Setter
    private String[] commands;

    @Setter
    private boolean needPermToCreateSign = true;

    @Setter
    private boolean customizable = !ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly();
    // Automatically customizable when there special PageLayout components are shown
    @Setter
    private boolean displaying   = false; // When displaying custom variables

    @Getter
    private       int inventorySize = 9;
    @Getter
    @Setter
    private       int manualInventoryRows;
    @Getter
    private final int shopId;

    private int highestPage; // Might not be correct but is used in case of a fix inventory having multiple pages


    @Getter
    private final Set<GenesisBuy> items = new LinkedHashSet<>();

    //////////////////////////// <- Constructor

    public GenesisShop(int shopId,
                       String shopName,
                       String signText,
                       boolean needPermToCreateSign,
                       Genesis plugin,
                       String displayName,
                       int manualInventoryRows,
                       String[] commands) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.signText = signText;
        this.manualInventoryRows = manualInventoryRows;
        this.needPermToCreateSign = needPermToCreateSign;
        setCommands(commands);

        setDisplayName(displayName);
    }

    public GenesisShop(int shopId) {
        this.shopId = shopId;
    }

    //////////////////////////// <- Methods to get main Variables

    public void setDisplayName(String displayName) {
        if (displayName != null) {
            this.displayName = ClassManager.manager.getStringManager().transform(displayName, null, this, null, null);
            if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, this.displayName)) {
                customizable = true;
                displaying = true;
            }
        } else {
            this.displayName = shopName;
        }
    }

    public String getValidDisplayName(Player p, GenesisShopHolder holder) {
        return ClassManager.manager.getStringManager().transform(this.displayName, null, this, holder, p);
    }

    public boolean needPermToCreateSign() {
        return needPermToCreateSign;
    }

    /**
     * Checks whether anything within the shop is player-dependent.
     *
     * @return true if shop contains anything player-dependent, like placeholders, conditions, multiple pages and more.
     */
    public boolean isCustomizable() {
        return customizable;
    }


    //////////////////////////// <- Methods to set main Variables

    /**
     * Checks whether the shop contains player-dependent placeholders.
     *
     * @return true if shop contains player-dependent placeholders.
     */
    public boolean isDisplaying() {
        return displaying;
    }

    //////////////////////////// <- Methods to get Items

    public GenesisBuy getItem(String name) {
        for (GenesisBuy buy : items) {
            if (buy.getName().equalsIgnoreCase(name)) {
                return buy;
            }
        }
        for (GenesisBuy buy : items) {
            if (buy.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return buy;
            }
        }
        return null;
    }


    //////////////////////////// <- Other Methods

    public void addShopItem(GenesisBuy buy, ItemStack menuItem, ClassManager manager) {
        buy.updateShop(this, menuItem, manager, true);
    }

    public void removeShopItem(GenesisBuy buy) {
        items.remove(buy);
    }


    public Inventory createInventory(Player p,
                                     ClassManager manager,
                                     int page,
                                     int highestPage,
                                     GenesisShopHolder oldShopHolder) {
        return manager.getShopCustomizer().createInventory(this, items, p, manager, page, highestPage, oldShopHolder);

    }

    public void updateInventory(Inventory i,
                                GenesisShopHolder holder,
                                Player p,
                                ClassManager manager,
                                int page,
                                int highestPage,
                                boolean autoRefresh) {
        if (holder.getPage() != page) {
            Misc.playSound(p,
                    ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_CHANGE_PAGE, this, null));
        }
        holder.setPage(page);
        holder.setHighestPage(highestPage);
        if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, getDisplayName())
                & !getValidDisplayName(p, holder).equals(p.getOpenInventory().getTitle())
                & !autoRefresh) { // Title is customizable as well but shall only be updated through main thread to prevent errors
            Inventory created = manager.getShopCustomizer()
                    .createInventory(this, items, p, manager, page, highestPage, holder.getPreviousShopHolder());
            p.openInventory(created);
            return;
        }
        Inventory inventory =
                manager.getShopCustomizer().createInventory(this, items, p, manager, i, page, highestPage);
        if (inventory != i) {
            p.openInventory(inventory);
        }
    }

    public void loadInventorySize() {
        PageLayoutHandler            layout = ClassManager.manager.getPageLayoutHandler();
        GenesisChoosePageLayoutEvent event  = new GenesisChoosePageLayoutEvent(this, getShopName(), layout);
        Bukkit.getPluginManager().callEvent(event);
        layout = event.getLayout();

        if (!layout.showIfMultiplePagesOnly()) {
            inventorySize = ROW_ITEMS * layout.getMaxRows();
            return;
        }
        Set<Integer> usedSlots            = new HashSet<>();
        int          highest              = 0;
        int          differentSlotsAmount = 0;
        for (GenesisBuy b : items) {
            if (b != null) {
                // if choosing specific slot -> store all different slots and add them in the end
                if (b.getInventoryLocation() == -1) { // If picking the next slot -> increasing slot number
                    differentSlotsAmount++;
                } else usedSlots.add(b.getInventoryLocation());
                if (b.getInventoryLocation() > highest) {
                    highest = b.getInventoryLocation();
                }
            }
        }
        differentSlotsAmount += usedSlots.size();
        inventorySize = getInventorySize(Math.max(highest,
                differentSlotsAmount
                        - 1)); // Use either the highest slot or the number of different possible slots in order to make sure the inventory is big enough
    }

    @Deprecated
    public int getInventorySize(int i) { // i as highest slot
        i++;
        int rest = i % ROW_ITEMS;
        if (rest > 0) {
            i += ROW_ITEMS - i % ROW_ITEMS;
        }

        int maxSlotsPerPage = ROWS_LIMIT_CURRENT * ROW_ITEMS;

        if (!ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly() || i > maxSlotsPerPage) {
            highestPage =
                    i / (ClassManager.manager.getPageLayoutHandler().getReservedSlotsStart() - 1); // Not tested yet!
        } else {
            highestPage = 0;
        }

        return Math.min(maxSlotsPerPage, Math.max(i, ROW_ITEMS * manualInventoryRows));
    }

    public void openInventory(Player p) {
        openInventory(p, 0, true);
    }

    public void openInventory(Player p, boolean rememberCurrentShop) {
        openInventory(p, 0, rememberCurrentShop);
    }

    public void openInventory(Player p, int page, boolean rememberCurrentShop) {
        GenesisShopHolder oldShopHolder = null;

        if (rememberCurrentShop) {
            InventoryView openinventory = p.getOpenInventory();
            if (openinventory.getTopInventory().getHolder() instanceof GenesisShopHolder) {
                oldShopHolder = (GenesisShopHolder) openinventory.getTopInventory().getHolder();
            }
        }

        ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShop", p, null, p, this, null, null);
        if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
            Misc.playSound(p,
                    ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_CHANGE_SHOP, this, null));
        } else {
            Misc.playSound(p,
                    ClassManager.manager.getSettings().getPropertyString(Settings.SOUND_SHOP_OPEN, this, null));
        }
        p.openInventory(createInventory(p, ClassManager.manager, page, highestPage, oldShopHolder));
        ClassManager.manager.getPlayerDataHandler()
                .openedShop(p, this);// TODO: only store previous shop, not current shop somehow.
    }

    public void close() {
        for (Player p : Bukkit.getOnlinePlayers()) { //NEW!
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                GenesisShopHolder holder = ((GenesisShopHolder) p.getOpenInventory().getTopInventory().getHolder());
                if (holder.getShop() == this) {
                    p.closeInventory();
                }
            }
        }
    }

    public boolean isBeingAccessed(Player exclusion) {
        for (Player p : Bukkit.getOnlinePlayers()) { //NEW!
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                GenesisShopHolder holder = ((GenesisShopHolder) p.getOpenInventory().getTopInventory().getHolder());
                if (holder.getShop() == this) {
                    if (p != exclusion) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //////////////////////////// <- Load Methods

    public void finishedAddingItems() {
        loadInventorySize();
    }

    //////////////////////////// <- Abstract

    public abstract void reloadShop();


}
