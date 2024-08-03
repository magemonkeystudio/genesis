package studio.magemonkey.genesis.core.rewards;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public abstract class GenesisRewardType {


    public static GenesisRewardType
            BungeeCordCommand,
            BungeeCordServer,
            Close,
            Command,
            Custom,
            Enchantment,
            Exp,
            Item,
            ItemAll,
            Message,
            Money,
            Nothing,
            Permission,
            PlayerCommand,
            PlayerCommandOp,
            Points,
            Shop,
            ShopBack,
            ShopPage,
            Teleport;

    private static List<GenesisRewardType> types;
    private        String[]                names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();

        BungeeCordCommand = registerType(new GenesisRewardTypeBungeeCordCommand());
        BungeeCordServer = registerType(new GenesisRewardTypeBungeeCordServer());
        Close = registerType(new GenesisRewardTypeClose());
        Custom = registerType(new GenesisRewardTypeCustom());
        Command = registerType(new GenesisRewardTypeCommand());
        Enchantment = registerType(new GenesisRewardTypeEnchantment());
        Exp = registerType(new GenesisRewardTypeExp());
        Item = registerType(new GenesisRewardTypeItem());
        ItemAll = registerType(new GenesisRewardTypeItemAll());
        Message = registerType(new GenesisRewardTypeMessage());
        Money = registerType(new GenesisRewardTypeMoney());
        Nothing = registerType(new GenesisRewardTypeNothing());
        Permission = registerType(new GenesisRewardTypePermission());
        PlayerCommand = registerType(new GenesisRewardTypePlayerCommand());
        PlayerCommandOp = registerType(new GenesisRewardTypePlayerCommandOp());
        Points = registerType(new GenesisRewardTypePoints());
        Shop = registerType(new GenesisRewardTypeShop());
        ShopPage = registerType(new GenesisRewardTypeShopPage());
        Teleport = registerType(new GenesisRewardTypeTeleport());
    }

    public static GenesisRewardType registerType(GenesisRewardType type) {
        types.add(type);
        return type;
    }

    public static GenesisRewardType detectType(String s) {
        if (s != null) {
            for (GenesisRewardType type : types) {
                if (type.isType(s)) {
                    return type;
                }
            }
        }
        return GenesisRewardType.Nothing;
    }

    public static List<GenesisRewardType> values() {
        return types;
    }

    public boolean isType(String s) {
        if (names != null) {
            for (String name : names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        GenesisRewardType.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }

    public void updateNames() {
        names = createNames();
    }


    public abstract Object createObject(Object o,
                                        boolean forceFinalState); // Used to transform the config input into a functional object

    public abstract boolean validityCheck(String itemName, Object o); // Used to check if the object is valid

    public abstract void enableType(); // Here you can register classes that the type depends on

    public abstract boolean canBuy(Player p,
                                   GenesisBuy buy,
                                   boolean messageIfNoSuccess,
                                   Object reward,
                                   ClickType clickType);

    public abstract void giveReward(Player p, GenesisBuy buy, Object reward, ClickType clickType);

    public abstract String getDisplayReward(Player p, GenesisBuy buy, Object reward, ClickType clickType);

    public abstract String[] createNames();


    public boolean logTransaction() {
        return true; // Can be overwritten
    }

    public boolean isPlayerDependend(GenesisBuy buy, ClickType clickType) {
        return supportsMultipliers() && ClassManager.manager.getMultiplierHandler().hasMultipliers() || (
                buy.getRewardType(clickType) == GenesisRewardType.ItemAll && ClassManager.manager.getSettings()
                        .getItemAllShowFinalReward());
    }

    public boolean supportsMultipliers() {
        return false; // can be overwritten
    }

    /**
     * If set to true sound will be played when purchasing
     */
    public boolean isActualReward() {
        return true; // can be overwritten
    }

    public boolean allowAsync() {
        return false; // can be overwritten
    }

    public boolean overridesPrice() {
        return false; // Can be overwritten
    }

    public String getPriceReturnMessage(Player p, GenesisBuy buy, Object price, ClickType clickType) {
        return null; // Can be overwritten in case of overriding the price
    }


    public abstract boolean mightNeedShopUpdate();


}
