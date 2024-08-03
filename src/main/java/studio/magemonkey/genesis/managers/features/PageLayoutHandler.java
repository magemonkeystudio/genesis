package studio.magemonkey.genesis.managers.features;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.config.ConfigLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PageLayoutHandler {
    @Getter
    private List<GenesisBuy> items;
    @Getter
    private int              maxRows = 6; //Default!
    /**
     * -- GETTER --
     *
     * @return display slot start: Starts with slot 1.
     */
    @Getter
    private int              reservedSlotsStart;
    private boolean          showIfMultiplePagesOnly;

    public PageLayoutHandler(List<GenesisBuy> items, int reservedSlotsStart, boolean showIfMultiplePagesOnly) {
        this.items = items;
        this.reservedSlotsStart = reservedSlotsStart;
        this.showIfMultiplePagesOnly = showIfMultiplePagesOnly;
    }


    public PageLayoutHandler(Genesis plugin) {
        File f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/pagelayout.yml");
        try {
            FileConfiguration config = ConfigLoader.loadConfiguration(f, false);
            setup(config);
        } catch (InvalidConfigurationException e) {
            plugin.getClassManager()
                    .getBugFinder()
                    .severe("Unable to load '/plugins/" + Genesis.NAME + "/pagelayout.yml'. Reason: "
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
        if (!section.isConfigurationSection("items")) return;

        for (String key : section.getConfigurationSection("items").getKeys(false)) {
            List<GenesisBuy> buyItems = ClassManager.manager.getBuyItemHandler()
                    .loadItem(section.getConfigurationSection("items"), null, key);
            for (GenesisBuy buy : buyItems) {
                if (buy != null) {
                    items.add(buy);
                }
            }
        }
    }

    public boolean showIfMultiplePagesOnly() {
        return showIfMultiplePagesOnly;
    }
}
