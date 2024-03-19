package org.black_ixx.bossshop.misc.locales;

import java.util.Map;
import java.util.TreeMap;

public class TranslationRegistry {

    public static Map<Locale, LocaleReader> Locales = new TreeMap<>();

    public static LocaleReader get(Locale locale) {
        if(!Locales.containsKey(locale))
            Locales.put(locale, new LocaleReader(locale));
        return Locales.get(locale);
    }
}
