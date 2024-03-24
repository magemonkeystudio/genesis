package com.promcteam.genesis.core.rewards;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import com.promcteam.genesis.managers.misc.StringManipulationLib;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GenesisRewardTypePlayerCommandOp extends GenesisRewardType {


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
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        List<String> commands = (List<String>) reward;

        if (p.isOp()) {
            executeCommands(p, buy, commands);
        } else {
            try {
                p.setOp(true);
                executeCommands(p, buy, commands);
            } catch (Exception e) {
                ClassManager.manager.getBugFinder()
                        .severe("Catch an exception while executing opcommands on item " + buy.getName()
                                + "! Please check it. Details:");
                e.printStackTrace();
            } finally {
                p.setOp(false);
            }
        }

        if (p.getOpenInventory() != null & !ClassManager.manager.getPlugin()
                .getAPI()
                .isValidShop(p.getOpenInventory())) {
            p.updateInventory();
        }

    }

    private void executeCommands(Player p, GenesisBuy buy, List<String> commands) {
        for (String s : commands) {
            p.performCommand(ClassManager.manager.getStringManager().transform(s, buy, null, null, p));
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commandsFormatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler()
                .get("Display.PlayerCommandOp")
                .replace("%commands%", commandsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"playercommandop", "playercommandsop", "opcommands", "opcommand"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
