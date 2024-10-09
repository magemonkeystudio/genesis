package studio.magemonkey.genesis.core.rewards;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;

import java.util.List;

public class GenesisRewardTypeItem extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        if (forceFinalState) {
            return InputReader.readItemList(o, false);
        } else {
            return InputReader.readStringListList(o);
        }
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-rewardtypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        if (!ClassManager.manager.getSettings().getInventoryFullDropItems()) {
            List<ItemStack> items = (List<ItemStack>) reward;
            if (!ClassManager.manager.getItemStackChecker().hasFreeSpace(p, items)) {
                if (messageIfNoSuccess) {
                    ClassManager.manager.getMessageHandler()
                            .sendMessage("Main.InventoryFull", p, null, p, buy.getShop(), null, buy);
                }
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        List<ItemStack> items = (List<ItemStack>) reward;

        for (ItemStack i : items) {
            ClassManager.manager.getItemStackCreator().giveItem(p, buy, i, i.getAmount(), true);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        List<ItemStack> items          = (List<ItemStack>) reward;
        String          itemsFormatted = ClassManager.manager.getItemStackTranslator().getFriendlyText(items);
        return ClassManager.manager.getMessageHandler().get("Display.Item").replace("%items%", itemsFormatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"item", "items"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

    @Override
    public boolean allowAsync() {
        return false;
    }

}
