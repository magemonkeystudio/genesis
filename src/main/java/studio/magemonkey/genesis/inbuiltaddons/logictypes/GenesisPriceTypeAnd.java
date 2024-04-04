package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class GenesisPriceTypeAnd extends GenesisPriceType {

    @Override
    public Object createObject(Object o, boolean forceFinalState) {
        List<GenesisPricePart> parts = new ArrayList<GenesisPricePart>();

        ConfigurationSection prices = (ConfigurationSection) o;
        for (int i = 1; prices.contains("PriceType" + i); i++) {
            String priceType   = prices.getString("PriceType" + i);
            Object priceObject = prices.get("Price" + i);

            GenesisPriceType priceT = GenesisPriceType.detectType(priceType);

            if (priceT == null) {
                ClassManager.manager.getBugFinder()
                        .severe("Invalid PriceType '" + priceType
                                + "' inside price list of shopitem with pricetype AND.");
                ClassManager.manager.getBugFinder().severe("Valid PriceTypes:");
                for (GenesisPriceType type : GenesisPriceType.values()) {
                    ClassManager.manager.getBugFinder().severe("-" + type.name());
                }
                continue;
            }
            priceT.enableType();

            Object priceO = priceT.createObject(priceObject, true);

            if (!priceT.validityCheck("?", priceO)) {
                ClassManager.manager.getBugFinder()
                        .severe("Invalid Price '" + priceO + "' (PriceType= " + priceType
                                + ") inside price list of shopitem with pricetype AND.");
                continue;
            }

            GenesisPricePart part = new GenesisPricePart(priceT, priceO);
            parts.add(part);

        }
        return parts;
    }

    @Override
    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The price object needs to be a list of price-blocks. Every priceblock needs to contain price and pricetype.");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public String[] createNames() {
        return new String[]{"and"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasPrice(Player p, GenesisBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        List<GenesisPricePart> priceparts = (List<GenesisPricePart>) price;
        for (GenesisPricePart part : priceparts) {
            if (!part.getPriceType().hasPrice(p, buy, part.getPrice(), clickType, messageOnFailure)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String takePrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        String                 sep        = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String                 s          = "";
        List<GenesisPricePart> priceparts = (List<GenesisPricePart>) price;
        for (int i = 0; i < priceparts.size(); i++) {
            GenesisPricePart part = priceparts.get(i);
            s += part.getPriceType().takePrice(p, buy, part.getPrice(), clickType) + (i < priceparts.size() - 1 ? sep
                    : "");
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDisplayPrice(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        String sep = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String s   = "";

        List<GenesisPricePart> priceparts = (List<GenesisPricePart>) price;
        for (int i = 0; i < priceparts.size(); i++) {
            GenesisPricePart part = priceparts.get(i);
            s += part.getPriceType().getDisplayPrice(p, buy, part.getPrice(), clickType) + (i < priceparts.size() - 1
                    ? sep : "");
        }
        return s;
    }

}
