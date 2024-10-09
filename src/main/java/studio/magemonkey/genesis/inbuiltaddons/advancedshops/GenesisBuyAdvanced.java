package studio.magemonkey.genesis.inbuiltaddons.advancedshops;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisInputType;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.core.conditions.GenesisCondition;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;

import java.util.Map;

public class GenesisBuyAdvanced extends GenesisBuy {

    private final Map<ClickType, ActionSet> actions;


    public GenesisBuyAdvanced(GenesisRewardType rewardT,
                              GenesisPriceType priceT,
                              Object reward,
                              Object price,
                              String msg,
                              int location,
                              String permission,
                              String name,
                              GenesisCondition condition,
                              GenesisInputType inputType,
                              String inputmessage,
                              Map<ClickType, ActionSet> actions) {
        super(rewardT, priceT, reward, price, msg, location, permission, name, condition, inputType, inputmessage);
        this.actions = actions;
    }

    @Override
    public GenesisRewardType getRewardType(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getRewardType();
            }
        }
        return super.getRewardType(clickType);
    }

    @Override
    public GenesisPriceType getPriceType(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getPriceType();
            }
        }
        return super.getPriceType(clickType);
    }

    @Override
    public Object getReward(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getReward();
            }
        }
        return super.getReward(clickType);
    }

    @Override
    public Object getPrice(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getPrice();
            }
        }
        return super.getPrice(clickType);
    }

    @Override
    public String getMessage(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getMessage();
            }
        }
        return super.getMessage(clickType);
    }

    @Override
    public GenesisInputType getInputType(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getInputType();
            }
        }
        return super.getInputType(clickType);
    }

    @Override
    public String getInputText(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getInputText();
            }
        }
        return super.getInputText(clickType);
    }

    @Override
    public boolean isExtraPermissionGroup(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).isExtraPermissionGroup();
            }
        }
        return super.isExtraPermissionGroup(clickType);
    }

    @Override
    public String getExtraPermission(ClickType clickType) {
        if (actions != null) {
            if (actions.containsKey(clickType)) {
                return actions.get(clickType).getExtraPermission();
            }
        }
        return super.getExtraPermission(clickType);
    }

    @Override
    public String transformMessage(String msg, GenesisShop shop, Player p) {
        msg = super.transformMessage(msg, shop, p);
        if (msg == null) {
            return null;
        }
        if (msg.length() == 0) {
            return msg;
        }


        if (actions != null) {
            for (ClickType clickType : actions.keySet()) {
                ActionSet action = actions.get(clickType);
                String    s      = clickType.name().toLowerCase();

                String tp = "%price_" + s + "%";
                String tr = "%reward_" + s + "%";

                if (msg.contains(tp) || msg.contains(tr)) {
                    String rewardMessage = action.getRewardType().isPlayerDependend(this, clickType) ? null
                            : action.getRewardType().getDisplayReward(p, this, action.getReward(), clickType);
                    String priceMessage = action.getPriceType().isPlayerDependend(this, clickType) ? null
                            : action.getPriceType().getDisplayPrice(p, this, action.getPrice(), clickType);


                    if (shop != null) { //Does shop need to be customizable and is not already?
                        if (!shop.isCustomizable()) {
                            boolean hasPriceVariable =
                                    (msg.contains(tp) && (action.getPriceType().isPlayerDependend(this, clickType)));
                            boolean hasRewardVariable =
                                    (msg.contains(tr) && (action.getRewardType().isPlayerDependend(this, clickType)));
                            if (hasPriceVariable || hasRewardVariable) {
                                shop.setCustomizable(true);
                                shop.setDisplaying(true);
                            }
                        }
                    }

                    boolean possiblyCustomizable = shop == null || shop.isCustomizable();
                    if (possiblyCustomizable) {
                        if (p != null) { //When shop is customizable, the variables needs to be adapted to the player
                            rewardMessage =
                                    action.getRewardType().getDisplayReward(p, this, action.getReward(), clickType);
                            priceMessage = action.getPriceType().getDisplayPrice(p, this, action.getPrice(), clickType);
                        }
                    }


                    if (priceMessage != null && !priceMessage.isEmpty()) {
                        msg = msg.replace(tp, priceMessage);
                    }
                    if (rewardMessage != null && !rewardMessage.isEmpty()) {
                        msg = msg.replace(tr, rewardMessage);
                    }
                }

            }
        }

        return msg;
    }

}
