package org.black_ixx.bossshop.inbuiltaddons.advancedshops;

import lombok.Getter;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;

@Getter
public class ActionSet {
    private BSRewardType rewardType;
    private BSPriceType  priceType;

    private Object reward;
    private Object price;

    private String message;
    private String extraPermission;

    private BSInputType inputType;
    private String      inputText;

    private boolean extraPermissionGroup;


    public ActionSet(BSRewardType rewardType,
                     BSPriceType priceType,
                     Object reward,
                     Object price,
                     String message,
                     String extraPermission,
                     BSInputType inputType,
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
                    if (group != null) {
                        ClassManager.manager.getSettings().setVaultEnabled(true);
                        this.extraPermission = group;
                        extraPermissionGroup = true;
                    }
                }
            }
        }
    }
}
