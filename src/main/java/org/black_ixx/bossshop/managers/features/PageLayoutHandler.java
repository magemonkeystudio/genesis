package org.black_ixx.bossshop.managers.features;

import lombok.Getter;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.ConfigLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageLayoutHandler {

    @Getter
    private List<BSBuy> items;
    @Getter
    private int maxRows = 6; //Default!
    private int reservedSlotsStart;
    private boolean showIfMultiplePagesOnly;

    public PageLayoutHandler(List<BSBuy> items, int reservedSlotsStart, boolean showIfMultiplePagesOnly) {
        this.items = items;
        this.reservedSlotsStart = reservedSlotsStart;
        this.showIfMultiplePagesOnly = showIfMultiplePagesOnly;
    }


    public PageLayoutHandler(BossShop plugin) {
        File f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/pagelayout.yml");
        try {
            FileConfiguration config = ConfigLoader.loadConfiguration(f, false);
            setup(config);

        } catch (InvalidConfigurationException e) {
            plugin.getClassManager()
                    .getBugFinder()
                    .severe("Unable to load '/plugins/" + BossShop.NAME + "/pagelayout.yml'. Reason: "
                            + e.getMessage());
        }
    }

    public PageLayoutHandler(ConfigurationSection section) {
        setup(section);
    }


    public void setup(ConfigurationSection section) {
        maxRows = Math.max(1, section.getInt("MaxRows"));
        reservedSlotsStart = section.getInt("ReservedSlotsStart");
        showIfMultiplePagesOnly = section.getBoolean("ShowIfMultiplePagesOnly");

        items = new ArrayList<>();
        if (section.isConfigurationSection("items")) {
            for (String key : section.getConfigurationSection("items").getKeys(false)) {
                List<BSBuy> buyItems = ClassManager.manager.getBuyItemHandler()
                        .loadItem(section.getConfigurationSection("items"), null, key);
                for (BSBuy buy : buyItems) {
                    if (buy != null) {
                        items.add(buy);
                    }
                }
            }
        }
    }

    public boolean showIfMultiplePagesOnly() {
        return showIfMultiplePagesOnly;
    }

    /**
     * @return display slot start: Starts with slot 1.
     */
    public int getReservedSlotsStart() {
        return reservedSlotsStart;
    }

}
