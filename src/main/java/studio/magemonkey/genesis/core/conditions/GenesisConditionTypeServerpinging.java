package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.managers.serverpinging.ServerInfo;
import studio.magemonkey.genesis.managers.serverpinging.ServerPingingManager;
import org.bukkit.entity.Player;

public class GenesisConditionTypeServerpinging extends GenesisConditionTypeNumber {


    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        if (conditiontype.equalsIgnoreCase("online")) {
            ServerPingingManager m = ClassManager.manager.getServerPingingManager();
            if (m != null) {
                ServerInfo connector = m.getFirstInfo(shopItem);
                boolean    b         = InputReader.getBoolean(condition, true);
                if (connector != null) {
                    return connector.isOnline() == b;
                }
            }
            return false;
        }

        return super.meetsCondition(holder, shopItem, p, conditiontype, condition);
    }


    @Override
    public double getNumber(GenesisBuy shopItem, GenesisShopHolder holder, Player p) {
        ServerPingingManager m = ClassManager.manager.getServerPingingManager();
        if (m != null) {
            ServerInfo connector = ClassManager.manager.getServerPingingManager().getFirstInfo(shopItem);
            if (connector != null) {
                if (connector.isOnline()) {
                    return connector.getPlayers();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"serverpinging", "serverping", "pinging", "ping"};
    }


    @Override
    public String[] showStructure() {
        return new String[]{"online", "over:[double]", "under:[double]", "equals:[double]", "between:[double]-[double]"};
    }


    @Override
    public void enableType() {
    }


}
