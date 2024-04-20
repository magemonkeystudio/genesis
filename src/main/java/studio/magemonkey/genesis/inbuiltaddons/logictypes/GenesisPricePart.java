package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.core.prices.GenesisPriceType;

public class GenesisPricePart {

    private final GenesisPriceType priceType;
    private final Object           price;

    public GenesisPricePart(GenesisPriceType priceType, Object price) {
        this.price = price;
        this.priceType = priceType;
    }

    public GenesisPriceType getPriceType() {
        return priceType;
    }

    public Object getPrice() {
        return price;
    }
}
