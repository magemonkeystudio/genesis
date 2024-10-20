package studio.magemonkey.genesis.core.conditions;

import org.bukkit.entity.Player;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShopHolder;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;

public class GenesisConditionTypePlaceholderNumber extends GenesisConditionType {


    @Override
    public boolean meetsCondition(GenesisShopHolder holder,
                                  GenesisBuy shopItem,
                                  Player p,
                                  String conditiontype,
                                  String condition) {
        String[] realparts = condition.split(":", 2);
        if (realparts.length < 2) {
            ClassManager.manager.getBugFinder()
                    .warn("Unable to read placeholdernumber condition " + condition + ":" + condition + " of shopitem "
                            + shopItem.getName()
                            + ". It should look like following: '<Placeholder text>:<conditiontype>:<condition>'.");
            return false;
        }

        double n = getNumber(shopItem, p, conditiontype);

        conditiontype = realparts[0];
        condition = realparts[1];

        if (condition.contains("#") && condition.contains("%")) {
            String[] parts = condition.split("#", 2);
            condition = parts[0];
            int divisor = InputReader.getInt(parts[1].replace("%", ""), 1);
            n %= divisor;
        }

        // Basic operations
        if (conditiontype.equalsIgnoreCase("over") || conditiontype.equalsIgnoreCase(">")) {
            double d = InputReader.getDouble(condition, -1);
            return n > d;
        }
        if (conditiontype.equalsIgnoreCase("under") || conditiontype.equalsIgnoreCase("<")
                || conditiontype.equalsIgnoreCase("below")) {
            double d = InputReader.getDouble(condition, -1);
            return n < d;
        }
        if (conditiontype.equalsIgnoreCase("equals") || conditiontype.equalsIgnoreCase("=")) {
            return equals(n, condition.split(","));
        }

        if (conditiontype.equalsIgnoreCase("between") || conditiontype.equalsIgnoreCase("inbetween")) {
            String[] parts = condition.split("-");
            if (parts.length == 2) {
                double start = InputReader.getDouble(parts[0], -1);
                double end   = InputReader.getDouble(parts[1], -1);
                return n >= start && n <= end;
            } else {
                ClassManager.manager.getBugFinder()
                        .warn("Unable to read condition '" + condition
                                + "' of conditiontype 'between'. It has to look like following: '<number1>-<number2>'.");
                return false;
            }
        }


        return false;
    }


    private boolean equals(double n, String[] options) {
        for (String option : options) {
            double d = InputReader.getDouble(option, -1);
            if (d == n) {
                return true;
            }
        }
        return false;
    }


    public double getNumber(GenesisBuy shopItem, Player p, String placeholder) {
        String transformedPlaceholder =
                ClassManager.manager.getStringManager().transform(placeholder, shopItem, shopItem.getShop(), null, p);
        return InputReader.getDouble(transformedPlaceholder.trim(), 0);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"placeholdernumber"};
    }


    @Override
    public void enableType() {
    }


    @Override
    public String[] showStructure() {
        return new String[]{"[string]:over:[double]", "[string]:under:[double]", "[string]:equals:[double]", "[string]:between:[double]-[double]"};
    }

}
