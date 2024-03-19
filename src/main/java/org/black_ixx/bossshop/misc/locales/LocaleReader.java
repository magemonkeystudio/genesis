//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package org.black_ixx.bossshop.misc.locales;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.black_ixx.bossshop.managers.ClassManager;
import org.jetbrains.annotations.NotNull;
import org.black_ixx.bossshop.misc.locales.Locale;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * LocalReader is used for loading a {@code Locale} from the resources folder and
 * accessing their keys and values.
 *
 * <p> All keys are in American English. Their represent
 * <ul>
 *     <li>*Advancements
 *     <li>*Arguments for Commands
 *     <li>*Attributes
 *     <li>Biomes <b>({@link org.bukkit.block.Biome})</b>
 *     <li>Blocks and Items <b>({@link org.bukkit.Material})</b>
 *     <li>*Commands
 *     <li>Effects <b>({@link org.bukkit.potion.PotionEffectType})</b>
 *     <li>Enchantments <b>({@link org.bukkit.enchantments.Enchantment})</b>
 *     <li>Entities <b>({@link org.bukkit.entity.EntityType})</b>
 *     <li>*GameRules
 *     <li>*GUI of player client
 *     <li>{@code PotionSort} <b>({@link org.bukkit.potion.PotionType})</b>
 *     <li>*Subtitles
 *     <li>TropicalFishes <b>({@link org.bukkit.entity.TropicalFish.Pattern})</b>
 *     <li>Villager <b>({@link org.bukkit.entity.Villager.Profession})</b>
 * </ul>
 *
 * <p> The values are the translation of the {@code Locale} for the specific
 * object that the key represents.
 *
 * @see Translate.PotionSort
 * @see java.util.Locale
 */
public class LocaleReader {

    private final JsonObject json;
    private final Locale locale;

    /**
     * Constructs an LocaleReader for the given locale.
     *
     * @param locale The locale used
     */
    LocaleReader(@NotNull Locale locale) {
        try {
            this.locale = locale;
            String fileName = locale + ".json";
            File file = new File(ClassManager.manager.getPlugin().getDataFolder(), "locales/" + fileName);
            InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            Gson gson = new Gson();

            this.json = gson.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not load locale " + locale, e);
        }
    }

    /**
     * Returns the value with the specific key.
     *
     * If the key does not exist, first it will check if an English translation
     * is available if not it returns null.
     *
     * @param key Name of the key that is requested.
     * @return Value as {@code String}. If this key does not exist {@code null}
     *         is returned.
     */
    String getValue(@NotNull String key) {
        JsonElement element = json.get(key);
        if (element == null) {
            if (locale == Locale.en_us) {
                return null;
            }

            return Translate.getCustomValue(key, Locale.en_us);
        }
        return element.getAsString();
    }

    /**
     * Returns all keys for the locale.
     *
     * @return An unmodifiable {@code List} containing all keys for this locale
     * @see Collections#unmodifiableList(List)
     */
    List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
            keys.add(entry.getKey());
        }
        return Collections.unmodifiableList(keys);
    }
}
