//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package org.black_ixx.bossshop.misc.locales;

import com.google.common.base.Preconditions;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * With {@code Translate} you can translate
 * <ul>
 *     <li>{@link Translate#getBiome(Player, Biome) Biomes}
 *     <li>{@link Translate#getMaterial(Player, Material) Materials}
 *     <li>{@link Translate#getPotion(Player, PotionType, PotionSort) Potions}
 *     <li>{@link Translate#getEffect(Player, PotionEffectType) PotionEffects}
 *     <li>{@link Translate#getEnchantment(Player, Enchantment) Enchantments}
 *     <li>{@link Translate#getEntity(Player, EntityType) Entities}
 *     <li>{@link Translate#getTropicalFish(Player, TropicalFish.Pattern) TropicalFishes}
 *     <li>{@link Translate#getVillager(Player, Villager.Profession) Villager}
 * </ul>
 * with the giving {@link Locale} or {@link Player}(uses {@link Translate#getLocale(Player)})
 * <p>
 * When a translation is requested the specific {@code Locale} is cached for two
 * minutes. If in that two minutes no further requests are taken it will be
 * automatically cleared from the cache. It will also be cleared from the cache
 * if no players are using this {@code Locale} in example if a player is leaving
 * or changing his {@code Locale}.
 */
public final class Translate extends JavaPlugin {

    /**
     * Returns the current local the player is using.
     * @param p The player
     * @return {@link Locale} the player's locale
     */
    public static Locale getLocale(Player p) {
        Locale standard = Locale.valueOf(ClassManager.manager.getPlugin().getConfig()
                .getString("Locales", "en_us").replace("-", "_").toLowerCase()).createIfNotExistend();
        if(standard == null) {
            standard = Locale.en_us.createIfNotExistend();
        }
        if(p  == null) {
            return standard;
        }
        return Locale.valueOf(p.getLocale()).getName() != null ? Locale.valueOf(p.getLocale()) : standard;
    }

    /**
     * Returns the translation of the key's value.
     * @param key The key to translate
     * @param locale The locale to translate
     * @return Translation of the key or {@code null} if the key does not exist
     */
    @Nullable
    public static String getCustomValue(@NotNull String key, @NotNull Locale locale) {
        return TranslationRegistry.get(locale).getValue(key);
    }

    /**
     * Returns the translation of the key's value.
     * @param key The key to translate
     * @param p The player
     * @return Translation of the key or {@code null} if the key does not exist
     */
    @Nullable
    public static String getCustomValue(@NotNull String key, @NotNull Player p) {
        return getCustomValue(key, getLocale(p));
    }

    /**
     * Returns all keys for the locale.
     *
     * @param locale The chosen locale
     * @return An unmodifiable {@code List} containing all keys for this locale
     */
    @NotNull
    public static List<String> getAllKey(@NotNull Locale locale) {
        return TranslationRegistry.get(locale).getKeys();
    }


    @NotNull
    public static String getBiome(@NotNull Player p, @NotNull Biome biome) {
        return getBiome(getLocale(p), biome);
    }

    @NotNull
    public static String getBiome(@NotNull Locale locale, @NotNull Biome biome) {
        String name = biome.name().toLowerCase();
        if (name.equals("custom")) return "Custom";

        return TranslationRegistry.get(locale).getValue("biome.minecraft." + name);
    }


    @NotNull
    public static String getMaterial(@NotNull Player p, @NotNull Material mat) {
        Locale locale = getLocale(p);
        if(locale != null) {
            String result = getMaterial(locale, mat);
            if(result != null) {
                return result;
            }
        }
        String material = mat.name().toLowerCase().replace("_", " ");
        material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
        return material;
    }

    public static String getMaterial(@NotNull Locale locale, @NotNull Material mat) {
        LocaleReader reader = TranslationRegistry.get(locale);
        String name = mat.getKey().getKey();

        if (name.contains("wall_")) name = name.replace("wall_", "");

        if (mat.isBlock()) {
            return reader.getValue("block.minecraft." + name);
        } else {
            return reader.getValue("item.minecraft." + name);
        }
    }

    /**
     * An enum of all potion types.
     */
    public enum PotionSort {
        POTION,
        SPLASH_POTION,
        LINGERING_POTION;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @NotNull
    public static String getPotion(@NotNull Player p, @NotNull PotionType type, @NotNull PotionSort sort) {
        return getPotion(getLocale(p), type, sort);
    }

    @NotNull
    public static String getPotion(@NotNull Locale locale, @NotNull PotionType type, @NotNull PotionSort sort) {
        String name = type.name();
        switch (type) {
            case REGEN:
                name = type.getEffectType().getName();
                break;
            case INSTANT_HEAL:
                name = "HEALING";
                break;
            case INSTANT_DAMAGE:
                name = "HARMING";
                break;
            case JUMP:
                name = "LEAPING";
                break;
            case UNCRAFTABLE:
                name = "EMPTY";
                break;
            case SPEED:
                name = "SWIFTNESS";
                break;
        }

        return TranslationRegistry.get(locale).getValue("item.minecraft." + sort + ".effect." + name.toLowerCase());
    }


    @Nullable
    public static String getEffect(@NotNull Player p, @Nullable PotionEffectType type) {
        return getEffect(getLocale(p), type);
    }

    @Nullable
    public static String getEffect(@NotNull Locale locale, @Nullable PotionEffectType type) {
        if (type == null) {
            return TranslationRegistry.get(locale).getValue("effect.none");
        }

        String name = type.getName();
        switch (type.getId()) {
            case 2:
                name = "SLOWNESS";
                break;
            case 3:
                name = "HASTE";
                break;
            case 4:
                name = "MINING_FATIGUE";
                break;
            case 5:
                name = "STRENGTH";
                break;
            case 6:
                name = "INSTANT_HEALTH";
                break;
            case 7:
                name = "INSTANT_DAMAGE";
                break;
            case 8:
                name = "JUMP_BOOST";
                break;
            case 9:
                name = "NAUSEA";
                break;
            case 11:
                name = "RESISTANCE";
                break;
        }

        return TranslationRegistry.get(locale).getValue("effect.minecraft." + name.toLowerCase());
    }


    @Nullable
    public static String getEnchantment(@NotNull Player p, @NotNull Enchantment ench) {
        return getEnchantment(getLocale(p), ench);
    }

    @Nullable
    public static String getEnchantment(@NotNull Locale locale, @NotNull Enchantment ench) {
        return TranslationRegistry.get(locale).getValue("enchantment.minecraft." + ench.getKey().getKey());
    }


    @NotNull
    public static String getEntity(@NotNull Player p, @NotNull EntityType type) {
        return getEntity(getLocale(p), type);
    }

    @NotNull
    public static String getEntity(@NotNull Locale locale, @NotNull EntityType type) {
        String name = type.getName();
        if (name == null) return TranslationRegistry.get(locale).getValue("entity.notFound");

        return TranslationRegistry.get(locale).getValue("entity.minecraft." + name.toLowerCase());
    }


    @NotNull
    public static String getVillager(@NotNull Player p, @NotNull Villager.Profession type) {
        return getVillager(getLocale(p), type);
    }

    @NotNull
    public static String getVillager(@NotNull Locale locale, @NotNull Villager.Profession type) {
        return TranslationRegistry.get(locale).getValue("entity.minecraft.villager." + type.name().toLowerCase());
    }

    @Nullable
    public static String getVillager(@NotNull Player p, @NotNull Object career) {
        return getVillager(getLocale(p), career);
    }

    @Nullable
    public static String getVillager(@NotNull Locale locale, @NotNull Object career) {
        Preconditions.checkArgument(career.getClass().getSimpleName().equals("Career"), "Are you using Villager.Career ???");

        String name = career.toString().replace("_", "").toLowerCase();
        return TranslationRegistry.get(locale).getValue("entity.minecraft.villager." + name);
    }


    @NotNull
    public static String getTropicalFish(@NotNull Player p, @NotNull TropicalFish.Pattern type) {
        return getTropicalFish(getLocale(p), type);
    }

    @NotNull
    public static String getTropicalFish(@NotNull Locale locale, @NotNull TropicalFish.Pattern type) {
        return TranslationRegistry.get(locale).getValue("entity.minecraft.tropical_fish.type." + type.name().toLowerCase());
    }
}
