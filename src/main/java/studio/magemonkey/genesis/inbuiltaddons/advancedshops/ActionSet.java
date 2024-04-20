package studio.magemonkey.genesis.inbuiltaddons.advancedshops;

import lombok.Getter;
import studio.magemonkey.genesis.core.GenesisInputType;
import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;

@Getter
public class ActionSet {
    private final GenesisRewardType rewardType;
    private final GenesisPriceType  priceType;

    private final Object reward;
    private final Object price;

    private final String message;

    private final GenesisInputType inputType;
    private final String           inputText;

    private String  extraPermission;
    private boolean extraPermissionGroup;


    public ActionSet(GenesisRewardType rewardType,
                     GenesisPriceType priceType,
                     Object reward,
                     Object price,
                     String message,
                     String extraPermission,
                     GenesisInputType inputType,
                     String inputText) {
        this.rewardType = rewardType;
        this.priceType = priceType;
        this.reward = reward;
        this.price = price;
        this.message = message;
        this.inputType = inputType;
        this.inputText = inputText;

        if (extraPermission != null && extraPermission != "") {
            this.extraPermission = extraPermission;
            if (this.extraPermission.startsWith("[") && this.extraPermission.endsWith("]")) {
                if (this.extraPermission.length() > 2) {
                    String group = this.extraPermission.substring(1, this.extraPermission.length() - 1);
                    ClassManager.manager.getSettings().setVaultEnabled(true);
                    this.extraPermission = group;
                    extraPermissionGroup = true;
                }
            }
        }
    }
}
