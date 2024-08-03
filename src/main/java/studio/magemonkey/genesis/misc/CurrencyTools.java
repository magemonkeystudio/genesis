package studio.magemonkey.genesis.misc;

import studio.magemonkey.genesis.core.prices.GenesisPriceType;
import studio.magemonkey.genesis.core.rewards.GenesisRewardType;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.entity.Player;


public class CurrencyTools {


    // NOTE: THIS CLASS DOES NOT INCLUDE MULTIPLIERS

    /**
     * Check if a player has enough of a specific currency
     *
     * @param p           the player to check
     * @param currency    the currency to check
     * @param value       the amount to look for
     * @param failMessage send fail message or not
     * @return has enough or not
     */
    public static boolean hasValue(Player p, GenesisCurrency currency, double value, boolean failMessage) {
        switch (currency) {
            case EXP:
                if (p.getLevel() < value) {
                    if (failMessage)
                        ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Exp", p);
                    return false;
                }
                return true;

            case MONEY:
                if (ClassManager.manager.getVaultHandler() == null) {
                    return false;
                }
                if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
                    return false;
                }
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
                    if (failMessage)
                        ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
                    return false;
                }
                if (ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName()) < value) {
                    if (failMessage)
                        ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Money", p);
                    return false;
                }
                return true;

            case POINTS:
                if (ClassManager.manager.getPointsManager().getPoints(p) < value) {
                    ClassManager.manager.getMessageHandler().sendMessage("NotEnough.Points", p);
                    return false;
                }
                return true;
        }

        return false;
    }

    /**
     * Take currency from a player
     *
     * @param p        player to modify
     * @param currency the currency to take
     * @param cost     the amount to take
     * @return the price to take from user
     */
    public static String takePrice(Player p, GenesisCurrency currency, double cost) {
        switch (currency) {
            case EXP:
                p.setLevel(p.getLevel() - (int) cost);
                int balanceExp = p.getLevel();
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Exp")
                        .replace("%levels%", MathTools.displayNumber(balanceExp, GenesisPriceType.Exp));

            case MONEY:
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
                    ClassManager.manager.getBugFinder()
                            .severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + cost
                                    + ")");
                    return "";
                }
                ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), cost);
                double balance = ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Money")
                        .replace("%money%", MathTools.displayNumber(balance, GenesisPriceType.Money));

            case POINTS:
                double balancePoints = ClassManager.manager.getPointsManager().takePoints(p, cost);
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Points")
                        .replace("%points%", MathTools.displayNumber(balancePoints, GenesisPriceType.Points));
        }

        return "";
    }

    /**
     * Give a currency reward to a player
     *
     * @param p        the player to modify
     * @param currency the currency to give
     * @param reward   the amount to give
     */
    public static void giveReward(Player p, GenesisCurrency currency, double reward) {
        switch (currency) {
            case EXP:
                p.setLevel(p.getLevel() + (int) reward);
                return;

            case MONEY:
                if (ClassManager.manager.getVaultHandler() == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Unable to give " + p.getName()
                                    + " his/her money: Vault manager not loaded. Property: "
                                    + ClassManager.manager.getSettings().getVaultEnabled());
                    return;
                }
                if (ClassManager.manager.getVaultHandler().getEconomy() == null) {
                    ClassManager.manager.getBugFinder()
                            .severe("Unable to give " + p.getName()
                                    + " his/her money: Economy manager not loaded. Property: "
                                    + ClassManager.manager.getSettings().getMoneyEnabled());
                    return;
                }
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
                    ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
                    ClassManager.manager.getBugFinder()
                            .severe("Unable to give " + p.getName()
                                    + " his/her money: He/She does not have an economy account.");
                    return;
                }
                ClassManager.manager.getVaultHandler().getEconomy().depositPlayer(p.getName(), reward);
                return;

            case POINTS:
                ClassManager.manager.getPointsManager().givePoints(p, reward);
        }
    }

    /**
     * Get the display price for something
     *
     * @param currency the currency to get
     * @param price    the price to check
     * @return display price
     */
    public static String getDisplayPrice(GenesisCurrency currency, double price) {
        switch (currency) {
            case EXP:
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Exp")
                        .replace("%levels%", MathTools.displayNumber((int) price, currency.getPriceType()));

            case MONEY:
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Money")
                        .replace("%money%", MathTools.displayNumber(price, currency.getPriceType()));

            case POINTS:
                return ClassManager.manager.getMessageHandler()
                        .get("Display.Points")
                        .replace("%points%", MathTools.displayNumber(price, currency.getPriceType()));
        }
        return null;
    }


    public enum GenesisCurrency {
        MONEY {
            @Override
            public GenesisPriceType getPriceType() {
                return GenesisPriceType.Money;
            }

            @Override
            public GenesisRewardType getRewardType() {
                return GenesisRewardType.Money;
            }

            @Override
            public double getBalance(Player p) {
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
                    ClassManager.manager.getBugFinder()
                            .severe("Unable to read balance! No economy account existing! (" + p.getName() + ")");
                    return -1;
                }
                return ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
            }
        },
        EXP {
            @Override
            public GenesisPriceType getPriceType() {
                return GenesisPriceType.Exp;
            }

            @Override
            public GenesisRewardType getRewardType() {
                return GenesisRewardType.Exp;
            }

            @Override
            public double getBalance(Player p) {
                return p.getExpToLevel();
            }
        },
        POINTS {
            @Override
            public GenesisPriceType getPriceType() {
                return GenesisPriceType.Points;
            }

            @Override
            public GenesisRewardType getRewardType() {
                return GenesisRewardType.Points;
            }

            @Override
            public double getBalance(Player p) {
                return ClassManager.manager.getPointsManager().getPoints(p);
            }
        };

        public static GenesisCurrency detectCurrency(String name) {
            for (GenesisCurrency currency : GenesisCurrency.values()) {
                if (currency.name().equalsIgnoreCase(name)) {
                    return currency;
                }
            }
            return null;
        }

        public abstract GenesisPriceType getPriceType();

        public abstract GenesisRewardType getRewardType();

        public abstract double getBalance(Player p);
    }


}
