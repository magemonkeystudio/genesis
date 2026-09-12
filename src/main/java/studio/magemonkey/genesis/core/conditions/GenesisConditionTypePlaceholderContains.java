package studio.magemonkey.genesis.core.conditions;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.managers.ClassManager;


public class GenesisConditionTypePlaceholderContains extends GenesisConditionType {


    public boolean contains(Player p, String searchTerm, String placeholder) {
        placeholder = ClassManager.manager.getStringManager().transform(placeholder, p);
        searchTerm = ClassManager.manager.getStringManager().transform(searchTerm, p);
        String cleanPlaceholder = ChatColor.stripColor(placeholder).trim().toLowerCase();
        String cleanSearchTerm = ChatColor.stripColor(searchTerm).trim().toLowerCase();
        return cleanPlaceholder.contains(cleanSearchTerm);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"placeholdercontains"};
    }


    @Override
    public void enableType() {
    }


    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditionType,
                                  String condition) {
        String[] parts = condition.split(":", 2);
        if (parts.length < 2) {
            warnBadFormat(shopItem, conditionType, condition);
            return false;
        }
        if (parts[0].equalsIgnoreCase("contains")) {
            return isCorrect(p, true, parts[1], conditionType);
        }
        if (parts[0].equalsIgnoreCase("notcontains")) {
            return isCorrect(p, false, parts[1], conditionType);
        }
        warnBadFormat(shopItem, conditionType, condition);
        return false;
    }

    private void warnBadFormat(GenesisBuy shopItem, String conditionType, String condition) {
        ClassManager.manager.getBugFinder()
                .warn("Unable to read placeholdercontains condition " + conditionType + ":" + condition
                        + " of shopitem " + shopItem.getName()
                        + ". It should look like following: '<Placeholder text>:<contains/notcontains>:<text>'.");
    }


    private boolean isCorrect(Player p, boolean shouldContain, String condition, String placeholder) {
        for (String searchTerm : condition.split(",")) {
            if (contains(p, searchTerm, placeholder) == shouldContain) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String[] showStructure() {
        return new String[]{"[string]:contains:[string]", "[string]:notcontains:[string]"};
    }


}
