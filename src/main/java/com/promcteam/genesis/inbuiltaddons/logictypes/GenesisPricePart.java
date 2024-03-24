package com.promcteam.genesis.inbuiltaddons.logictypes;

import com.promcteam.genesis.core.prices.GenesisPriceType;

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
