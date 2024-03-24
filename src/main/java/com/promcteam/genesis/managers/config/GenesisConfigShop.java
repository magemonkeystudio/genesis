package com.promcteam.genesis.managers.config;

import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.core.rewards.GenesisRewardType;
import lombok.Getter;
import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShop;
import com.promcteam.genesis.core.GenesisShops;
import com.promcteam.genesis.core.prices.GenesisPriceType;
import com.promcteam.genesis.events.GenesisLoadShopItemsEvent;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GenesisConfigShop extends GenesisShop {
    @Getter
    private String               ymlName;
    private File                 f;
    @Getter
    private FileConfiguration    config;
    private ConfigurationSection section;

    //////////////////////////////////

    public GenesisConfigShop(int shopId, String ymlName, GenesisShops shopHandler) {
        this(shopId,
                new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName),
                shopHandler);
        this.ymlName = ymlName;
    }

    public GenesisConfigShop(int shopId, File f, GenesisShops shopHandler) {
        this(shopId, f, shopHandler, null);
    }

    public GenesisConfigShop(int shopId, File f, GenesisShops shopHandler, ConfigurationSection sectionOptional) {
        super(shopId);

        this.f = f;

        try {
            config = ConfigLoader.loadConfiguration(f, true);

        } catch (InvalidConfigurationException e) {
            ClassManager.manager.getBugFinder()
                    .severe("Invalid Configuration! File: /shops/" + ymlName + " Cause: " + e.getMessage());
            String name = ymlName.replace(".yml", "");
            setSignText("[" + name + "]");
            setNeedPermToCreateSign(true);
            setShopName(name);

            ItemStack i = new ItemStack(Material.WHITE_WOOL, 1);
            ItemMeta  m = i.getItemMeta();
            m.setDisplayName(ChatColor.RED + "Your Config File contains mistakes! (" + ymlName + ")");
            List<String> lore = new ArrayList<String>();
            lore.add(ChatColor.YELLOW + "For more information check /plugins/" + Genesis.NAME + "/BugFinder.yml out!");
            m.setLore(lore);
            i.setItemMeta(m);
            addShopItem(new GenesisBuy(GenesisRewardType.Command,
                    GenesisPriceType.Nothing,
                    "tell %player% the config file (" + ymlName + ") contains mistakes...",
                    null,
                    "",
                    0,
                    "",
                    name), i, ClassManager.manager);
            finishedAddingItems();
            return;
        }

        setup(shopHandler, sectionOptional == null ? config : sectionOptional);

    }

    public void setup(GenesisShops shopHandler, ConfigurationSection section) {
        this.section = section;

        //Add defaults if not existing already
        addDefaults();

        setShopName(section.getString("ShopName"));
        setDisplayName(section.getString("DisplayName"));
        setSignText(section.getString("signs.text"));
        setNeedPermToCreateSign(section.getBoolean("signs.NeedPermissionToCreateSign"));
        setManualInventoryRows(section.getInt("InventoryRows", -1));

        String commands = section.getString("Command");
        if (commands != null) {
            setCommands(commands.split(":"));
        }

        ClassManager.manager.getSettings().update(this);

        //Load Items
        loadItems();
        GenesisLoadShopItemsEvent event = new GenesisLoadShopItemsEvent(shopHandler, this);
        Bukkit.getPluginManager().callEvent(event);
        finishedAddingItems();
    }

    //////////////////////////////////

    public ConfigurationSection getConfigurationSection() {
        return section;
    }

    //////////////////////////////////

    public void saveConfig() {
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reloadConfig() {
        f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName);
        config = YamlConfiguration.loadConfiguration(f);
        InputStream defConfigStream = ClassManager.manager.getPlugin().getResource(f.getName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    //////////////////////////////////

    public void addDefault(String name,
                           String rewardType,
                           String priceType,
                           Object reward,
                           Object price,
                           List<String> menuitem,
                           String message,
                           int loc,
                           String permission) {
        ConfigurationSection section = this.section.getConfigurationSection("shop").createSection(name);
        section.set("RewardType", rewardType);
        section.set("PriceType", priceType);
        section.set("Price", price);
        section.set("Reward", reward);
        section.set("MenuItem", menuitem);
        section.set("Message", message);
        section.set("InventoryLocation", loc);
        section.set("ExtraPermission", permission);
    }

    public void addDefaults() {
        section.addDefault("ShopName", "ExtraShop");
        section.addDefault("signs.text", "[ExtraShop]");
        section.addDefault("signs.NeedPermissionToCreateSign", false);

        if (section.getConfigurationSection("shop") == null && section.getConfigurationSection("itemshop") == null) {
            section.createSection("shop");

            List<String> menuItem = new ArrayList<String>();
            menuItem.add("type:STONE");
            menuItem.add("amount:1");
            menuItem.add("name:&8Example");
            List<String> cmd = new ArrayList<String>();
            cmd.add("tell %name% Example");
            addDefault("Example", "command", "money", cmd, 5000, menuItem, "", 1, "");
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    //////////////////////////////////

    @Override
    public int getInventorySize() {
        if (section.getInt("InventorySize") != 0) {
            return section.getInt("InventorySize");
        }
        return super.getInventorySize();
    }

    ///////////////////////////////////////// <- Load Config-Items


    public void loadItems() {
        ConfigurationSection c = section.getConfigurationSection("shop");
        if (c != null) {
            for (String key : c.getKeys(false)) {
                ClassManager.manager.getBuyItemHandler().loadItem(c, this, key);
            }
        }

        if (config.isSet("InventoryFill")) {
            Material material;
            try {
                material = Material.valueOf(config.getString("InventoryFill").toUpperCase(Locale.ENGLISH));
            } catch (Exception ignored) {
                material = Material.BLACK_STAINED_GLASS_PANE;
                Genesis.log("Your InventoryFill parameter is invalid and was replaced with its default: "
                        + config.getString("InventoryFill"));
            }
            for (int i = 0; i < getInventorySize(); i++) {
                if (isFilled(i))
                    continue;
                GenesisBuy buy = new GenesisBuy(GenesisRewardType.Nothing,
                        GenesisPriceType.Nothing,
                        null,
                        null,
                        null,
                        i,
                        null,
                        "internal_placeholder_" + i,
                        null,
                        null,
                        null);
                ItemStack fillerItem = new ItemStack(material);

                // Maybe there already is an existing method for it?
                ItemMeta meta = fillerItem.getItemMeta();
                meta.setDisplayName(" ");
                fillerItem.setItemMeta(meta);
                buy.setItem(fillerItem, false);

                this.addShopItem(buy, buy.getItem(), ClassManager.manager);
            }
        }
    }

    private boolean isFilled(int location) {
        return this.getItems().stream().anyMatch(buy -> buy.getInventoryLocation() == location);
    }

    @Override
    public void reloadShop() {
        reloadConfig();
    }

}
