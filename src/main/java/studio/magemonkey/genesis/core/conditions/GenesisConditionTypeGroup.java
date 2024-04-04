package studio.magemonkey.genesis.core.conditions;

import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.entity.Player;

public class GenesisConditionTypeGroup extends GenesisConditionTypeMatch {


    @Override
    public boolean matches(Player p, String singleCondition) {
        return ClassManager.manager.getVaultHandler().getPermission().playerInGroup(p, singleCondition);
    }


    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"group", "permissionsgroup"};
    }


    @Override
    public void enableType() {
        ClassManager.manager.getSettings().setVaultEnabled(true);
        ClassManager.manager.getSettings().setPermissionsEnabled(true);
    }


}
