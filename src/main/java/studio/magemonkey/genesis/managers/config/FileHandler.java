package studio.magemonkey.genesis.managers.config;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.api.GenesisAddon;
import studio.magemonkey.genesis.api.GenesisAddonConfig;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHandler {

    public void exportConfigs(Genesis plugin) {
        copyDefaultsFromJar(plugin
                , "config.yml"
                , "messages.yml");

        if (!new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "pagelayout.yml").exists()) {
            copyFromJar(plugin, plugin, false, "pagelayout.yml");
        }

    }


    public void exportShops(Genesis plugin) {
        File folder = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "shops" + File.separator);
        if (!folder.isFile() & !folder.isDirectory()) {

            copyFromJar(plugin, plugin, true
                    , "BungeeCordServers.yml"
                    , "BuyShop.yml"
                    , "LilyPadServers.yml"
                    , "Menu.yml"
                    , "PointShop.yml"
                    , "GameShop.yml"
                    , "HugeShop.yml"
                    , "Skulls.yml"
                    , "WeekShop.yml"
                    , "PlayerCommands.yml"
                    , "Commands.yml");

        }
    }

    public void exportLanguages(Genesis plugin) {
        if (!new File(plugin.getDataFolder() + File.separator + "lang/en-us.yml").exists()) {
            plugin.saveResource("lang/en-us.yml", false);
        }
        if (!new File(plugin.getDataFolder() + File.separator + "lang/zh-cn.yml").exists()) {
            plugin.saveResource("lang/zh-cn.yml", false);
        }
    }

    public void copyDefaultsFromJar(GenesisAddon addon, String filename) {
        copyDefaultsFromJar(addon, addon.getBossShop(), filename, "addons/" + addon.getAddonName() + "/" + filename);
    }

    public void copyDefaultsFromJar(Genesis plugin, String... files) {
        for (String filename : files) {
            if (filename != null) {
                copyDefaultsFromJar(plugin, filename);
            }
        }
    }

    public void copyDefaultsFromJar(Genesis plugin, String filename) {
        copyDefaultsFromJar(plugin, plugin, filename, filename);
    }

    public void copyDefaultsFromJar(Plugin resourceHolder,
                                    Plugin folderHolder,
                                    String filename,
                                    String outputfilename) {
        File write = new File(folderHolder.getDataFolder() + File.separator + outputfilename);
        if (!write.exists()) {
            copyFromJar(resourceHolder, folderHolder, false, filename, outputfilename);
            return;
        }

        boolean change = false;
        try {
            copyFromJar(resourceHolder, folderHolder, false, filename, "temp");
            File read = new File(folderHolder.getDataFolder() + File.separator + "temp");

            if (read.exists()) {
                GenesisAddonConfig configw = new GenesisAddonConfig(resourceHolder, write);
                GenesisAddonConfig configr = new GenesisAddonConfig(resourceHolder, read);
                for (String key : configr.getConfig().getKeys(true)) {
                    if (!configw.getConfig().contains(key)) {
                        configw.getConfig().addDefault(key, configr.getConfig().get(key));
                        change = true;
                    }
                }
                if (change) {
                    configw.getConfig().options().copyDefaults(true);
                    configw.save();
                }

                read.delete();
            }

        } catch (Exception e) {
            ClassManager.manager.getBugFinder().warn("Unable to load config lines of file " + filename);
        }
    }

    public void copyFromJar(Plugin resourceHolder, Plugin folderHolder, boolean shop, String... files) {
        for (String filename : files) {
            if (filename != null) {
                copyFromJar(resourceHolder, folderHolder, shop, filename, filename);
            }
        }
    }

    public void copyFromJar(Plugin resourceHolder,
                            Plugin folderHolder,
                            boolean shop,
                            String filename,
                            String outputfilename) {
        String additional = shop ? "shops" + File.separator : "";
        File   file       = new File(folderHolder.getDataFolder() + File.separator + additional + outputfilename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        InputStream in = resourceHolder.getResource(filename);
        try {
            OutputStream out = new FileOutputStream(file);
            byte[]       buf = new byte[1024];
            int          len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void copyFromJar(GenesisAddon addon, String filename) {
        copyFromJar(addon, addon.getBossShop(), false, filename, "/addons/" + addon.getAddonName() + "/" + filename);
    }

}
