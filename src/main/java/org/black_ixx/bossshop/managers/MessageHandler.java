package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.config.FileHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class MessageHandler {
    private final BossShop plugin;
    private String fileName = "lang" + File.separator + "en-us.yml";
    private FileConfiguration config;

    public MessageHandler(final BossShop plugin) {
        this.plugin = plugin;
        setupLocate();
    }

    /**
     * Get the config file
     *
     * @return config
     */
    public FileConfiguration getConfig() {
        if (config == null) reloadConfig();
        return config;
    }

    /**
     * Reload the config file
     */
    public void reloadConfig() {
        setupLocate();
        InputStream defConfigStream = plugin.getResource("lang/" + fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    /**
     * Send message from config to player
     *
     * @param node   path of message
     * @param sender sender to send to
     */
    public void sendMessage(String node, CommandSender sender) {
        sendMessage(node, sender, null, null, null, null, null);
    }

    /**
     * Send message from config to player
     *
     * @param node          path of message
     * @param sender        sender to send to
     * @param offlineTarget offline target
     */
    public void sendMessage(String node, CommandSender sender, String offlineTarget) {
        sendMessage(node, sender, offlineTarget, null, null, null, null);
    }

    /**
     * Send message from config to player
     *
     * @param node   path of message
     * @param sender sender to send to
     * @param target player target
     */
    public void sendMessage(String node, CommandSender sender, Player target) {
        sendMessage(node, sender, null, target, null, null, null);
    }

    /**
     * Send a message to a player
     *
     * @param node          the path of message
     * @param sender        the sender to send to
     * @param offlineTarget offline target
     * @param target        player target
     * @param shop          shop to send to
     * @param holder        the holder of the shop
     * @param item          the item in the shop
     */
    public void sendMessage(String node,
                            CommandSender sender,
                            String offlineTarget,
                            Player target,
                            BSShop shop,
                            BSShopHolder holder,
                            BSBuy item) {
        if (sender != null) {

            if (node == null || node.equals("")) {
                return;
            }

            String message = get(node, target, shop, holder, item);

            if (message == null || message.length() < 2) {
                return;
            }

            if (offlineTarget != null) {
                message = message.replace("%player%", offlineTarget)
                        .replace("%name%", offlineTarget)
                        .replace("%target%", offlineTarget);
            }

            sendMessageDirect(message, sender);
        }
    }

    /**
     * Send message directly to CommandSender
     *
     * @param message the message to sender
     * @param sender  the sender to send to
     */
    public void sendMessageDirect(String message, CommandSender sender) {
        if (sender != null) {

            if (message == null || message.length() < 2) {
                return;
            }

            String colors = "";
            for (String line : message.split("\\\\n")) {
                sender.sendMessage(colors + line);
                colors = ChatColor.getLastColors(line);
            }
        }
    }


    /**
     * Get a string from the config
     *
     * @param node path of node
     * @return string
     */
    public String get(String node) {
        return get(node, null, null, null, null);
    }

    /**
     * Get a raw string from config
     *
     * @param node path of node
     * @return raw string
     */
    public String getRaw(String node) {
        return config.getString(node, node);
    }

    private String get(String node, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return replace(config.getString(node, node), target, shop, holder, item);
    }

    private String replace(String message, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return ClassManager.manager.getStringManager().transform(message, item, shop, holder, target);
    }

    public void setupLocate() {
        String LangCode = ClassManager.manager.getSettings().getLanguage();
        if (Objects.equals(LangCode, null) || LangCode.equals("")) {
            LangCode = "en-us";
            plugin.getConfig().set("Language", "en-us");
        }
        fileName = "lang/" + LangCode + ".yml";
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            LangCode = "en-us";
            plugin.getConfig().set("Language", "en-us");
            FileHandler fh = new FileHandler();
            File lang = new File(plugin.getDataFolder(), "lang" + File.separator + "en-us.yml");
            if (!lang.exists()) {
                fh.exportLanguages(plugin);
                fileName = "lang/" + LangCode + ".yml";
                file = new File(plugin.getDataFolder(), "lang" + File.separator + fileName);
                config = YamlConfiguration.loadConfiguration(file);
                return;
            }
            fileName = "lang/" + LangCode + ".yml";
            file = new File(plugin.getDataFolder(), "lang" + File.separator + fileName);
            ClassManager.manager.getBugFinder()
                    .warn("The corresponding message file cannot be found and fallback to en-us. (maybe you didn't put the message file in the plugin folder, or didn't have the message file)");

        }
        config = YamlConfiguration.loadConfiguration(file);
    }
}