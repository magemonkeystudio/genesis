package studio.magemonkey.genesis.core.conditions;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;
import studio.magemonkey.genesis.misc.Misc;

public class GenesisConditionTypeEnchantmentLevel extends GenesisConditionType {

    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        // conditiontype = enchantment name (e.g., "EFFICIENCY")
        // condition     = "over:5" / "under:3" / "equals:10" / "between:5-10"

        String[] realparts = condition.split(":", 2);
        if (realparts.length < 2) {
            ClassManager.manager.getBugFinder()
                    .warn("Unable to read enchantmentlevel condition '" + conditiontype + ":" + condition
                            + "'. It should look like: '<enchantment>:<operator>:<level>'. "
                            + "Example: 'EFFICIENCY:equals:10'.");
            return false;
        }

        String operator = realparts[0];
        String value    = realparts[1];
        double level    = getEnchantmentLevel(p, conditiontype);

        if (operator.equalsIgnoreCase("over") || operator.equalsIgnoreCase(">")) {
            return level > InputReader.getDouble(value, -1);
        }
        if (operator.equalsIgnoreCase("under") || operator.equalsIgnoreCase("<")
                || operator.equalsIgnoreCase("below")) {
            return level < InputReader.getDouble(value, -1);
        }
        if (operator.equalsIgnoreCase("equals") || operator.equalsIgnoreCase("=")) {
            for (String option : value.split(",")) {
                if (level == InputReader.getDouble(option.trim(), -1)) {
                    return true;
                }
            }
            return false;
        }
        if (operator.equalsIgnoreCase("between") || operator.equalsIgnoreCase("inbetween")) {
            String   separator = value.contains(":") ? ":" : "-";
            String[] parts     = value.split(separator);
            if (parts.length == 2) {
                double start = InputReader.getDouble(parts[0], -1);
                double end   = InputReader.getDouble(parts[1], -1);
                return level >= start && level <= end;
            } else {
                ClassManager.manager.getBugFinder()
                        .warn("Unable to read enchantmentlevel condition '" + conditiontype + ":" + condition
                                + "' of conditiontype 'between'. "
                                + "It has to look like following: '<enchantment>:between:<level1>-<level2>'.");
                return false;
            }
        }

        return false;
    }

    /**
     * Returns the level of the given enchantment on the item in the player's main hand,
     * or 0 if the item does not have the enchantment.
     */
    private double getEnchantmentLevel(Player p, String enchantmentName) {
        ItemStack item = Misc.getItemInMainHand(p);
        if (item == null || item.getType().isAir()) {
            return 0;
        }

        Enchantment enchantment = InputReader.readEnchantment(enchantmentName);
        if (enchantment == null) {
            ClassManager.manager.getBugFinder()
                    .warn("Unknown enchantment '" + enchantmentName + "' in enchantmentlevel condition.");
            return 0;
        }

        // Check regular item enchantments first
        if (item.getEnchantments().containsKey(enchantment)) {
            return item.getEnchantments().get(enchantment);
        }

        // Fallback: check enchantment books (EnchantmentStorageMeta)
        if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            if (meta.getStoredEnchants().containsKey(enchantment)) {
                return meta.getStoredEnchants().get(enchantment);
            }
        }

        return 0;
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"enchantmentlevel", "enchlevel", "enchantlevel"};
    }

    @Override
    public void enableType() {
    }

    @Override
    public String[] showStructure() {
        return new String[]{
                "[enchantment]:over:[int]",
                "[enchantment]:under:[int]",
                "[enchantment]:equals:[int]",
                "[enchantment]:between:[int]-[int]"
        };
    }

}
