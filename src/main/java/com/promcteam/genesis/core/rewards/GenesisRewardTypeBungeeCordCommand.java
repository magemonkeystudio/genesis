package com.promcteam.genesis.core.rewards;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.external.BungeeCordManager;
import com.promcteam.genesis.managers.misc.InputReader;
import com.promcteam.genesis.managers.misc.StringManipulationLib;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GenesisRewardTypeBungeeCordCommand extends GenesisRewardType {


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
        ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        List<String> commands = (List<String>) reward;

        BungeeCordManager bc = ClassManager.manager.getBungeeCordManager();

        if (bc != null) {

            for (String s : commands) {
                bc.executeCommand(p, ClassManager.manager.getStringManager().transform(s, buy, buy.getShop(), null, p));
            }

        } else {
            ClassManager.manager.getBugFinder()
                    .severe("Was not able to execute BungeeCord commands: The BungeeCordManager was not enabled properly.");
        }


    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commandsFormatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler()
                .get("Display.BungeeCordCommand")
                .replace("%commands%", commandsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"bungeecordcommand", "bungeecordcommands", "bccommand", "bccommands"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
