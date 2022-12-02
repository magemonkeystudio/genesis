package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.MessageHandler;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ShopCreator implements Listener {
    private String name = "";
    private String title;
    private final MessageHandler mh;
    private final BossShop plugin;
    public ShopCreator(BossShop plugin, MessageHandler handler){
        this.plugin = plugin;
        this.mh = handler;
        Bukkit.getServer().getPluginManager().registerEvents(this,plugin);
    }
    public void startCreate(Player p,String shopName,String title){
        setName(shopName);
        this.title = title;
        String inv_title = this.mh.get("ShopCreate.Title").replace("%shop%",getName());
        Inventory inv = Bukkit.createInventory(null,54,inv_title);
        p.openInventory(inv);
    }
    @EventHandler
    private void Listen(InventoryCloseEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player p = (Player) e.getPlayer();
            if (p.getOpenInventory().getTitle().startsWith(this.mh.get("ShopCreate.TitleHead"))) {
                if (p.hasPermission("BossShop.create") || e.getPlayer().isOp()) {
                    Inventory inv = e.getInventory();
                    if (inv.getSize() == 54) {
                        SaveAsFile(inv, p, getName(),getTitle());
                    }
                }
            }
        }
    }
    private void SaveAsFile(Inventory inv, Player p, String shopName, String title) {
        if(Objects.equals(shopName, "")){
            return;
        }
        File f = new File(plugin.getDataFolder(),"shops"+File.separator+shopName+".yml");
        if(f.exists()){
            mh.sendMessage("ShopCreate.FileExists",p);
            f.delete();
        }
        YamlConfiguration shop = YamlConfiguration.loadConfiguration(f);
        shop.set("ShopName",shopName);
        shop.set("DisplayName",title);
        ConfigurationSection c = shop.createSection("signs");
        c.set("text","["+shopName+"]");
        c.set("NeedPermissionToCreateSign",true);
        ConfigurationSection shopItems = shop.createSection("shop");
        for(int real_slot=0;real_slot<53;real_slot++){
            ItemStack is = inv.getItem(real_slot);
            if(is==null){
                continue;
            }
            ConfigurationSection item = shopItems.createSection(String.valueOf(real_slot+1));
            item.set("MenuItem", ItemDataPart.readItem(is));
            item.set("RewardType","nothing");
            item.set("PriceType", "nothing");
            item.set("ExtraPermission", "");
            item.set("Message","");
            item.set("InventoryLocation", real_slot+1);
        }
        try {shop.save(f);
        } catch (IOException e) {throw new RuntimeException(e);}
        setName("");
        mh.sendMessage("ShopCreate.Success",p);
        if(ClassManager.manager.getSettings().getReloadAfterCreateShop()){
            ClassManager.manager.getPlugin().reloadPlugin(p);
        }else {
            ClassManager.manager.getMessageHandler().sendMessage("ShopCreate.Success2",p);
        }
    }

    private String getName(){
        return name;
    }
    private String getTitle(){
        return title;
    }
    private void setName(String name){
        this.name = name;
    }
}
