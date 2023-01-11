package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public class BSRewardTypeTeleport extends BSRewardType{
    @Override
    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readString(o, true);
    }

    @Override
    public boolean validityCheck(String item_name, Object o) {
        String str = o.toString();
        if (str != null&&!str.isBlank()){
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs no to be a empty string.");
        return false;
    }

    @Override
    public void enableType() {}

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        String[] strings = reward.toString().split("#");
        if (strings.length==1){
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) p.teleport(w.getSpawnLocation());
            else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting",p);
        } else if (strings.length==4) {
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) {
                p.teleport(new Location(w, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3])));
            }
            else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting",p);
        } else if (strings.length==6) {
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) {
                p.teleport(new Location(w, Double.parseDouble(strings[1]), Double.parseDouble(strings[2]), Double.parseDouble(strings[3]),
                        Float.parseFloat(strings[4]), Float.parseFloat(strings[5])));
            }
            else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting",p);
        } else {
            ClassManager.manager.getBugFinder().warn("Unable to teleport "+p.getName()+" to "+reward+". Maybe the string use wrong format.");
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return null;
    }

    @Override
    public String[] createNames() {
        return new String[]{"teleport","tp"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }
}