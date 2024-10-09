package studio.magemonkey.genesis.core.rewards;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.prices.GenesisPriceTypeNumber;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.misc.CurrencyTools.GenesisCurrency;

public class GenesisRewardTypeItemAll extends GenesisRewardType {
    public Object createObject(Object o, boolean forceFinalState) {
        if (forceFinalState) {
            ItemStack i = InputReader.readItem(o, false);
            i.setAmount(1);
            return i;
        } else {
            return InputReader.readStringList(o);
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

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageOnFailure, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        if (!ClassManager.manager.getItemStackChecker().hasFreeSpace(p, item)) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler()
                        .sendMessage("Main.InventoryFull", p, null, p, buy.getShop(), null, buy);
            }
            return false;
        }

        int itemsAmountPossibleSpace =
                ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, item);
        GenesisCurrency priceCurrency            = GenesisCurrency.detectCurrency(buy.getPriceType(clickType).name());
        double          pricePerItem             = (double) buy.getPrice(clickType);
        int             itemsAmountPossibleMoney = (int) (priceCurrency.getBalance(p) / pricePerItem);
        int itemsAmount =
                Math.max(1, Math.min(itemsAmountPossibleSpace, itemsAmountPossibleMoney));

        GenesisPriceTypeNumber priceType = (GenesisPriceTypeNumber) buy.getPriceType(clickType);
        return priceType.hasPrice(p, buy, buy.getPrice(clickType), clickType, itemsAmount, messageOnFailure);
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;

        int itemsAmountPossibleSpace =
                ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(p, item);
        GenesisCurrency priceCurrency            = GenesisCurrency.detectCurrency(buy.getPriceType(clickType).name());
        double          pricePerItem             = (double) buy.getPrice(clickType);
        int             itemsAmountPossibleMoney = (int) (priceCurrency.getBalance(p) / pricePerItem);
        int itemsAmount =
                Math.max(1, Math.min(itemsAmountPossibleSpace, itemsAmountPossibleMoney));

        GenesisPriceTypeNumber priceType = (GenesisPriceTypeNumber) buy.getPriceType(clickType);
        priceType.takePrice(p, buy, buy.getPrice(clickType), clickType, itemsAmount);


        ClassManager.manager.getItemStackCreator().giveItem(p, buy, item, itemsAmount, true);
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        ItemStack item     = (ItemStack) reward;
        String    itemName = ClassManager.manager.getItemStackTranslator().readMaterial(p, item);
        return ClassManager.manager.getMessageHandler().get("Display.ItemAllBuy").replace("%item%", itemName);
    }

    @Override
    public String[] createNames() {
        return new String[]{"itemall", "buyall"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean allowAsync() {
        return false;
    }


    @Override
    public boolean overridesPrice() {
        return true;
    }

    @Override
    public String getPriceReturnMessage(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        GenesisPriceTypeNumber priceType = (GenesisPriceTypeNumber) buy.getPriceType(clickType);
        return priceType.getDisplayBalance(p, buy, price, clickType);
    }
}
