package org.black_ixx.bossshop.core;

import lombok.Getter;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

public class BSMultiplier {

    public final static int RANGE_ALL = 0;
    public final static int RANGE_PRICE_ONLY = 1;
    public final static int RANGE_REWARD_ONLY = 2;

    @Getter
    private String permission = "Permission.Node";
    @Getter
    private BSPriceType type = BSPriceType.Nothing;
    @Getter
    private double multiplier = 1.0;
    private int range = RANGE_ALL;

    // Optional filters for the multipliers to only apply to specific shops and items
    @Getter
    private String shopName = null;
    @Getter
    private String itemId = null;


    public BSMultiplier(String configLine) {
        String[] parts = configLine.split(":", 6);

        if (parts.length < 3) {
            ClassManager.manager.getBugFinder()
                    .warn("Invalid Multiplier Group Line... " + configLine
                            + "! It should look like this: Permission.Node:<type>:<multiplier>:<price/reward/both>:[shop]:[itemId]");
            return;
        }

        String permission = parts[0].trim();

        if (parts[1].trim().equalsIgnoreCase("<type>")) {
            return;
        }

        BSPriceType type = BSPriceType.detectType(parts[1].trim());
        double multiplier;
        int range = RANGE_ALL;

        if (type == null || !type.supportsMultipliers()) {
            ClassManager.manager.getBugFinder()
                    .warn("Invalid Multiplier Group Line... \"" + configLine
                            + "\"! It should look like this: \"Permission.Node:<type>:<multiplier>:<price/reward/both>\". '"
                            + parts[1].trim() + "' does not support multipliers!");
            return;
        }
        try {
            multiplier = Double.parseDouble(parts[2].trim());
        } catch (Exception e) {
            ClassManager.manager.getBugFinder()
                    .warn("Invalid Multiplier Group Line... \"" + configLine
                            + "\"! It should look like this: \"Permission.Node:<type>:<multiplier>:<price/reward/both>\". '"
                            + parts[2].trim()
                            + "' is no valid multiplier... What you can use instead (examples): 0.25, 0.3, 0.75, 1.0, 1.5, 2.0 etc.!");
            return;
        }

        if (parts.length >= 4) {
            String rs = parts[3].trim();
            // If range is available, select between 'price' or 'reward'
            if (rs.equalsIgnoreCase("price")) {
                range = RANGE_PRICE_ONLY;
            } else if (rs.equalsIgnoreCase("reward")) {
                range = RANGE_REWARD_ONLY;
            }
            // Else take 'both' and start to search for shop and item ids
            else if (parts.length <= 5) {
                setShopDetails(parts, 3);
            }

            // If the shop and item ids are not found yet, take a last attempt to see if they are set after the range
            if (parts.length >= 6 && shopName == null && itemId == null) {
                setShopDetails(parts, 4);
            }
        }

        setup(permission, type, multiplier, range);

    }

    private void setShopDetails(String[] args, int fromPost) {
        shopName = args[fromPost].trim();
        if (args.length >= fromPost + 2)
            itemId = args[fromPost + 1].trim();
    }

    public BSMultiplier(String permission, BSPriceType type, double multiplier, int range) {
        setup(permission, type, multiplier, range);
    }

    public void setup(String permission, BSPriceType type, double multiplier, int range) {
        this.permission = permission;
        this.type = type;
        this.multiplier = multiplier;
        this.range = range;
    }

    public boolean isValid() {
        return type.supportsMultipliers();
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(permission);
    }

    public double calculateValue(Player p, BSPriceType type, double d, int range) {
        if (isMultiplierActive(p, type, range)) {
            switch (this.range) {
                case RANGE_ALL: // Multiplier supports both types -> buy price is multiplied and sell reward is divided
                    switch (range) {
                        case RANGE_ALL:
                            return d * this.multiplier;
                        case RANGE_PRICE_ONLY:
                            return d * this.multiplier;
                        case RANGE_REWARD_ONLY:
                            return d / this.multiplier;
                    }

                    // If Multiplier supports one of both types in both cases the value is multiplied
                case RANGE_REWARD_ONLY:
                    if (range == RANGE_ALL || range == RANGE_REWARD_ONLY) {
                        d *= this.multiplier;
                    }
                    break;

                case RANGE_PRICE_ONLY:
                    if (range == RANGE_ALL || range == RANGE_PRICE_ONLY) {
                        d *= this.multiplier;
                    }
                    break;

            }
        }

        return d;
    }


    public boolean isMultiplierActive(Player p, BSPriceType type, int range) {
        if (this.type == type) {
            if (hasPermission(p)) {
                return isInRange(range);
            }
        }
        return false;
    }

    public boolean isInRange(int range) {
        switch (range) {
            case RANGE_ALL:
                return true;
            case RANGE_REWARD_ONLY:
                return this.range == RANGE_REWARD_ONLY || this.range == RANGE_ALL;
            case RANGE_PRICE_ONLY:
                return this.range == RANGE_PRICE_ONLY || this.range == RANGE_ALL;
        }
        return false;
    }

    public boolean isAcceptedShopItem(BSBuy buy) {
        if(shopName == null && itemId == null) {
            return true;
        } else if(shopName != null && itemId == null) {
            return shopName.equalsIgnoreCase(buy.getShop().getShopName());
        } else if(shopName != null) {
            return shopName.equalsIgnoreCase(buy.getShop().getShopName()) && itemId.equalsIgnoreCase(buy.getName());
        }
        return true;
    }

    public double calculateWithMultiplier(double d) {
        return d * multiplier;
    }

    public int calculateWithMultiplier(int d) {
        return (int) (d * multiplier);
    }


}
