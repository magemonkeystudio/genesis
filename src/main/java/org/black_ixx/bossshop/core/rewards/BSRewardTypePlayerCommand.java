package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class BSRewardTypePlayerCommand extends BSRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readStringList(o);
    }

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
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<String> commands = (List<String>) reward;

        for (String s : commands) {
            p.performCommand(ClassManager.manager.getStringManager().transform(s, buy, null, null, p));
        }

        if (p.getOpenInventory() != null & !ClassManager.manager.getPlugin()
                .getAPI()
                .isValidShop(p.getOpenInventory())) {
            p.updateInventory();
        }

    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commandsFormatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler()
                .get("Display.PlayerCommand")
                .replace("%commands%", commandsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"playercommand", "playercommands"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
