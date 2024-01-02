package org.black_ixx.bossshop.core.rewards;

import net.md_5.bungee.api.chat.BaseComponent;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class BSRewardTypeMessage extends BSRewardType{

    @Override
    public Object createObject(Object o, boolean force_final_state) {
        return InputReader.readStringList(o);
    }

    @Override
    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a list of commands (text lines).");
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
        List<String> messages = (List<String>) reward;

        for (String s : messages) {
            BaseComponent baseComponent = InputReader.readChatComponent(s);
            if (baseComponent == null) {
                p.sendMessage(s);
            } else {
                p.spigot().sendMessage(baseComponent);
            }
        }

        p.getOpenInventory();
        if (!ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
            p.updateInventory();
        }
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        return ClassManager.manager.getMessageHandler().get("Display.Message")
                .replace("%messages%", StringManipulationLib.formatList((List<String>) reward));
    }

    @Override
    public String[] createNames() {
        return new String[]{"message", "messages", "playermessage", "playermessages"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }
}
