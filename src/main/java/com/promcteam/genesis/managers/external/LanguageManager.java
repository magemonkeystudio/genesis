package com.promcteam.genesis.managers.external;


import com.meowj.langutils.lang.LanguageHelper;
import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LanguageManager {

    private final String locale;

    public LanguageManager() {
        locale = ClassManager.manager.getMessageHandler().getRaw("Locale");
    }

    public String getDisplayNameItem(ItemStack i) {
        return LanguageHelper.getItemDisplayName(i, locale);
    }

    public String getDisplayNameEnchantment(Enchantment e) {
        return LanguageHelper.getEnchantmentName(e, locale);
    }


}
