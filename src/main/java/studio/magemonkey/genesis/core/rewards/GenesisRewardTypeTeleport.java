package studio.magemonkey.genesis.core.rewards;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;

public class GenesisRewardTypeTeleport extends GenesisRewardType {
    @Override
    public Object createObject(Object o, boolean forceFinalState) {
        return InputReader.readString(o, true);
    }

    @Override
    public boolean validityCheck(String itemName, Object o) {
        String str = o.toString();
        if (str != null && !str.isBlank()) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object cannot be an empty string.");
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
        String[] strings = reward.toString().split("#");
        if (strings.length == 1) {
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) p.teleport(w.getSpawnLocation());
            else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting", p);
        } else if (strings.length == 4) {
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) {
                p.teleport(new Location(w,
                        Double.parseDouble(strings[1]),
                        Double.parseDouble(strings[2]),
                        Double.parseDouble(strings[3])));
            } else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting", p);
        } else if (strings.length == 6) {
            World w = Bukkit.getWorld(strings[0]);
            if (w != null) {
                p.teleport(new Location(w,
                        Double.parseDouble(strings[1]),
                        Double.parseDouble(strings[2]),
                        Double.parseDouble(strings[3]),
                        Float.parseFloat(strings[4]),
                        Float.parseFloat(strings[5])));
            } else ClassManager.manager.getMessageHandler().sendMessage("Main.WorldNotExisting", p);
        } else {
            ClassManager.manager.getBugFinder()
                    .warn("Unable to teleport " + p.getName() + " to " + reward
                            + ". The string's format may be incorrect.");
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        return null;
    }

    @Override
    public String[] createNames() {
        return new String[]{"teleport", "tp"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }
}