package com.promcteam.genesis.core.rewards;


import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.InputReader;
import com.promcteam.genesis.misc.Enchant;
import com.promcteam.genesis.misc.Misc;
import com.promcteam.genesis.settings.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class GenesisRewardTypeEnchantment extends GenesisRewardType {


    public Object createObject(Object o, boolean forceFinalState) {
        if (forceFinalState) {
            return InputReader.readEnchant(o);
        } else {
            return InputReader.readString(o, false);
        }
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder()
                .severe("Was not able to create ShopItem " + itemName
                        + "! The reward object needs to be a text line looking like this: '<enchantmenttype/enchantmentid>#<level>'.");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, GenesisBuy buy, boolean messageIfNoSuccess, Object reward, ClickType clickType) {
        Enchant enchant = (Enchant) reward;

        ItemStack item = Misc.getItemInMainHand(p);
        if (item == null || item.getType() == Material.AIR) {
            if (messageIfNoSuccess) {
                ClassManager.manager.getMessageHandler().sendMessage("Enchantment.EmptyHand", p);
            }
            return false;
        }

        if (!ClassManager.manager.getSettings().getPropertyBoolean(Settings.ALLOW_UNSAFE_ENCHANTMENTS, buy)) {
            if (!(enchant.getType().canEnchantItem(item))) {
                if (messageIfNoSuccess) {
                    ClassManager.manager.getMessageHandler().sendMessage("Enchantment.Invalid", p);
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        Enchant   enchant = (Enchant) reward;
        ItemStack item    = Misc.getItemInMainHand(p);
        if (item != null && item.getType() != Material.AIR) {
            item.addUnsafeEnchantment(enchant.getType(), enchant.getLevel());
        }
    }

    @Override
    public String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType) {
        Enchant enchant = (Enchant) reward;
        return ClassManager.manager.getMessageHandler()
                .get("Display.Enchantment")
                .replace("%type%", ClassManager.manager.getItemStackTranslator().readEnchantment(enchant.getType()))
                .replace("%level%", String.valueOf(enchant.getLevel()));
    }

    @Override
    public String[] createNames() {
        return new String[]{"enchantment", "enchant"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return false;
    }

}
