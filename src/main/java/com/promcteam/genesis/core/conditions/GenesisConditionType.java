package com.promcteam.genesis.core.conditions;

import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShopHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public abstract class GenesisConditionType {


    public static GenesisConditionType
            SERVERPINGING,
            MONEY,
            POINTS,
            GROUP,
            HEALTH,
            HUNGER,
            PERMISSION,
            TIME,
            ITEM,
            HANDITEM,
            EXP,
            SHOPPAGE,
            REALYEAR,
            REALMONTH,
            REALWEEK,
            REALMONTHDAY,
            REALWEEKDAY,
            REALHOUR,
            REALMINUTE,
            REALSECOND,
            REALMILLISECOND,
            LIGHTLEVEL,
            LOCATIONX,
            LOCATIONY,
            LOCATIONZ,
            WORLD,
            WEATHER,
            PLACEHOLDERNUMBER,
            PLACEHOLDERMATCH;


    private static List<GenesisConditionType> types;
    private final  String[]                   names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();
        SERVERPINGING = registerType(new GenesisConditionTypeServerpinging());
        MONEY = registerType(new GenesisConditionTypeMoney());
        POINTS = registerType(new GenesisConditionTypePoints());
        GROUP = registerType(new GenesisConditionTypeGroup());
        HEALTH = registerType(new GenesisConditionTypeHealth());
        HUNGER = registerType(new GenesisConditionTypeHunger());
        PERMISSION = registerType(new GenesisConditionTypePermission());
        TIME = registerType(new GenesisConditionTypeTime());
        ITEM = registerType(new GenesisConditionTypeItem());
        HANDITEM = registerType(new GenesisConditionTypeHandItem());
        EXP = registerType(new GenesisConditionTypeExp());
        SHOPPAGE = registerType(new GenesisConditionTypeShopPage());
        REALYEAR = registerType(new GenesisConditionTypeRealYear());
        REALMONTH = registerType(new GenesisConditionTypeRealMonth());
        REALWEEK = registerType(new GenesisConditionTypeRealWeek());
        REALMONTHDAY = registerType(new GenesisConditionTypeRealMonthDay());
        REALWEEKDAY = registerType(new GenesisConditionTypeRealWeekDay());
        REALHOUR = registerType(new GenesisConditionTypeRealHour());
        REALMINUTE = registerType(new GenesisConditionTypeRealMinute());
        REALSECOND = registerType(new GenesisConditionTypeRealSecond());
        REALMILLISECOND = registerType(new GenesisConditionTypeRealMillisecond());
        LIGHTLEVEL = registerType(new GenesisConditionTypeLightlevel());
        LOCATIONX = registerType(new GenesisConditionTypeLocationX());
        LOCATIONY = registerType(new GenesisConditionTypeLocationY());
        LOCATIONZ = registerType(new GenesisConditionTypeLocationZ());
        WORLD = registerType(new GenesisConditionTypeWorld());
        WEATHER = registerType(new GenesisConditionTypeWeather());
        PLACEHOLDERNUMBER = registerType(new GenesisConditionTypePlaceholderNumber());
        PLACEHOLDERMATCH = registerType(new GenesisConditionTypePlaceholderMatch());
    }

    public static GenesisConditionType registerType(GenesisConditionType type) {
        types.add(type);
        return type;
    }

    public static GenesisConditionType detectType(String s) {
        for (GenesisConditionType type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return null;
    }

    public static List<GenesisConditionType> values() {
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

    public String[] getNames() {
        return names;
    }

    public void register() {
        GenesisConditionType.registerType(this);
    }

    public String name() {
        return names[0].toUpperCase();
    }

    public abstract void enableType(); // Here you can register classes that the type depends on

    public abstract boolean meetsCondition(GenesisShopHolder holder,
                                           GenesisBuy shopItem,
                                           Player p,
                                           String conditiontype,
                                           String condition);

    public abstract String[] createNames();

    public abstract String[] showStructure();

    public abstract boolean dependsOnPlayer();


}
