package studio.magemonkey.genesis.core.rewards;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.managers.misc.StringManipulationLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class GenesisRewardTypeCommand extends GenesisRewardType {


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

        for (String s : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    ClassManager.manager.getStringManager().transform(s, buy, null, null, p));
        }
        if (p.getOpenInventory() != null & !ClassManager.manager.getPlugin()
                .getAPI()
                .isValidShop(p.getOpenInventory())) {
            p.updateInventory();
        }

    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commandsFormatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler()
                .get("Display.Command")
                .replace("%commands%", commandsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"command", "commands", "consolecommand", "consolecommands"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
