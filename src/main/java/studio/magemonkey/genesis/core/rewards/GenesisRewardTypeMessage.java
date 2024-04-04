package studio.magemonkey.genesis.core.rewards;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.managers.misc.StringManipulationLib;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GenesisRewardTypeMessage extends GenesisRewardType {

    @Override
    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readStringList(o);
    }

    @Override
    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a list of commands (text lines).");
        return false;
    }

    @Override
    public void enableType() {}

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
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
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
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
