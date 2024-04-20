package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class GenesisRewardTypeAnd extends GenesisRewardType {

    @Override
    public Object createObject(Object o, boolean forceFinalState) {
        List<GenesisRewardPart> rewardparts = new ArrayList<GenesisRewardPart>();

        ConfigurationSection rewards = (ConfigurationSection) o;
        for (int i = 1; rewards.contains("RewardType" + i); i++) {
            String rewardType   = rewards.getString("RewardType" + i);
            Object rewardObject = rewards.get("Reward" + i);

            GenesisRewardType rewardT = GenesisRewardType.detectType(rewardType);

            if (rewardT == null) {
                ClassManager.manager.getBugFinder()
                        .severe("Invalid RewardType '" + rewardType
                                + "' inside reward list of shopitem with rewardtype AND.");
                ClassManager.manager.getBugFinder().severe("Valid RewardTypes:");
                for (GenesisRewardType type : GenesisRewardType.values()) {
                    ClassManager.manager.getBugFinder().severe("-" + type.name());
                }
                continue;
            }

            Object rewardO = rewardT.createObject(rewardObject, true);

            if (!rewardT.validityCheck("?", rewardO)) {
                ClassManager.manager.getBugFinder()
                        .severe("Invalid Reward '" + rewardO + "' (RewardType= " + rewardType
                                + ") inside reward list of shopitem with rewardtype AND.");
                continue;
            }
            rewardT.enableType();

            GenesisRewardPart part = new GenesisRewardPart(rewardT, rewardO);
            rewardparts.add(part);

        }
        return rewardparts;
    }

    @Override
    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a list of reward-blocks. Every rewardblock needs to contain reward and rewardtype.");
        return false;
    }

    @Override
    public void enableType() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        List<GenesisRewardPart> rewardparts = (List<GenesisRewardPart>) reward;
        for (GenesisRewardPart part : rewardparts) {
            if (!part.getRewardType().canBuy(p, buy, messageIfNoSuccess, part.getReward(), clickType)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        List<GenesisRewardPart> rewardparts = (List<GenesisRewardPart>) reward;
        for (GenesisRewardPart part : rewardparts) {
            part.getRewardType().giveReward(p, buy, part.getReward(), clickType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        String sep = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String s   = "";

        List<GenesisRewardPart> rewardparts = (List<GenesisRewardPart>) reward;
        for (int i = 0; i < rewardparts.size(); i++) {
            GenesisRewardPart part = rewardparts.get(i);
            s += part.getRewardType().getDisplayReward(p, buy, part.getReward(), clickType) + (
                    i < rewardparts.size() - 1 ? sep : "");
        }
        return s;
    }

    @Override
    public String[] createNames() {
        return new String[]{"and"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
