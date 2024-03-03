package org.black_ixx.bossshop.inbuiltaddons.logictypes;

import org.black_ixx.bossshop.core.prices.BSPriceType;

public class BSPricePart {

    private BSPriceType priceType;
    private Object      price;

    public BSPricePart(BSPriceType priceType, Object price) {
        this.price = price;
        this.priceType = priceType;
    }

    public BSPriceType getPriceType() {
        return priceType;
    }

    public Object getPrice() {
        return price;
    }
}
