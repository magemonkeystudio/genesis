package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.core.rewards.GenesisRewardType;

public class GenesisRewardPart {

    private final GenesisRewardType rewardtype;
    private final Object            reward;

    public GenesisRewardPart(GenesisRewardType rewardtype, Object reward) {
        this.reward = reward;
        this.rewardtype = rewardtype;
    }

    public GenesisRewardType getRewardType() {
        return rewardtype;
    }

    public Object getReward() {
        return reward;
    }
}
