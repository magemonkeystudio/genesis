//  Copyright (C) 2022 Unp1xelt. All rights reserved.
//
//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
//  This Source Code Form is "Incompatible With Secondary Licenses", as
//  defined by the Mozilla Public License, v. 2.0.

package org.black_ixx.bossshop.misc.locales;

import org.apache.commons.io.IOUtils;
import org.black_ixx.bossshop.managers.ClassManager;

import java.io.*;

/**
 * Original Author: Unp1xelt
 * An enum of all locale codes used by Minecraft which defines the language of the
 * player's client.
 * <br>
 * This data comes from the
 * <a href="https://minecraft.fandom.com/wiki/Language#Upcoming_languages">Minecraft fandom page</a>
 * and the official <a href="https://crowdin.com/project/minecraft">Minecraft translation project</a>.
 */
public enum Locale {
    /**
     * Afrikaans (Suid-Afrika)
     */
    af_za("Afrikaans (Suid-Afrika)"),
    /**
     * Arabic
     */
    ar_sa("Arabic"),
    /**
     * Asturian
     */
    ast_es("Asturian"),
    /**
     * Azerbaijani
     */
    az_az("Azerbaijani"),
    /**
     * Bashkir
     *
     * @apiNote since v1.14.3
     */
    ba_ru("Bashkir"),
    /**
     * Bavarian
     *
     * @apiNote since v1.14
     */
    bar("Bavarian"),
    /**
     * Belarusian
     */
    be_by("Belarusian"),
    /**
     * Bulgarian
     */
    bg_bg("Bulgarian"),
    /**
     * Breton
     */
    br_fr("Breton"),
    /**
     * Brabantian
     */
    brb("Brabantian"),
    /**
     * Bosnian
     */
    bs_ba("Bosnian"),
    /**
     * Catalan
     */
    ca_es("Catalan"),
    /**
     * Czech
     */
    cs_cz("Czech"),
    /**
     * Welsh
     */
    cy_gb("Welsh"),
    /**
     * Danish
     */
    da_dk("Danish"),
    /**
     * Austrian German
     */
    de_at("Austrian German"),
    /**
     * Swiss German
     */
    de_ch("Swiss German"),
    /**
     * German
     */
    de_de("German"),
    /**
     * Greek
     */
    el_gr("Greek"),
    /**
     * Australian English
     */
    en_au("Australian English"),
    /**
     * Canadian English
     */
    en_ca("Canadian English"),
    /**
     * British English
     */
    en_gb("British English"),
    /**
     * New Zealand English
     */
    en_nz("New Zealand English"),
    /**
     * Pirate English
     */
    en_pt("Pirate English"),
    /**
     * Upside down English
     */
    en_ud("Upside down English"),
    /**
     * American English
     */
    en_us("American English"),
    /**
     * Modern English
     */
    enp(" Modern English"),
    /**
     * Early Modern English
     */
    enws("Early Modern English"),
    /**
     * Esperanto
     */
    eo_uy("Esperanto"),
    /**
     * Argentinian Spanish
     */
    es_ar("Argentinian Spanish"),
    /**
     * Chilean Spanish
     */
    es_cl("Chilean Spanish"),
    /**
     * Ecuadorian Spanish
     *
     * @apiNote since v1.16
     */
    es_ec("Ecuadorian Spanish"),
    /**
     * Spanish
     */
    es_es("Spanish"),
    /**
     * Mexican Spanish
     */
    es_mx("Mexican Spanish"),
    /**
     * Uruguayan Spanish
     */
    es_uy("Uruguayan Spanish"),
    /**
     * Venezuelan Spanish
     */
    es_ve("Venezuelan Spanish"),
    /**
     * Andalusian
     *
     * @apiNote since v1.16
     */
    esan("Andalusian"),
    /**
     * Estonian
     */
    et_ee("Estonian"),
    /**
     * Basque
     */
    eu_es("Basque"),
    /**
     * Persian
     */
    fa_ir("Persian"),
    /**
     * Finnish
     */
    fi_fi("Finnish"),
    /**
     * Filipino
     */
    fil_ph("Filipino"),
    /**
     * Faroese
     */
    fo_fo("Faroese"),
    /**
     * Canadian French
     */
    fr_ca("Canadian French"),
    /**
     * French
     */
    fr_fr("French"),
    /**
     * East Franconian
     *
     * @apiNote since v1.13.1
     */
    fra_de("East Franconian"),
    /**
     * Friulian
     */
    fur_it("Friulian"),
    /**
     * Frisian
     */
    fy_nl("Frisian"),
    /**
     * Irish
     */
    ga_ie("Irish"),
    /**
     * Scottish Gaelic
     */
    gd_gb("Scottish Gaelic"),
    /**
     * Galician
     */
    gl_es("Galician"),
    /**
     * Gothic (got)
     *
     * @apiNote available from v1.14 to v1.15
     */
    got_de("Gothic"),
    /**
     * Manx
     *
     * @apiNote only up to v1.16
     */
    gv_im("Manx"),
    /**
     * Hawaiian
     */
    haw_us("Hawaiian"),
    /**
     * Hebrew
     */
    he_il("Hebrew"),
    /**
     * Hindi
     */
    hi_in("Hindi"),
    /**
     * Croatian
     */
    hr_hr("Croatian"),
    /**
     * Hungarian
     */
    hu_hu("Hungarian"),
    /**
     * Armenian
     */
    hy_am("Armenian"),
    /**
     * Indonesian
     */
    id_id("Indonesian"),
    /**
     * Igbo
     */
    ig_ng("Igbo"),
    /**
     * Ido
     */
    io_en("Ido"),
    /**
     * Icelandic
     */
    is_is("Icelandic"),
    /**
     * Interslavic
     *
     * @apiNote since v1.16
     */
    isv("Interslavic"),
    /**
     * Italian
     */
    it_it("Italian"),
    /**
     * Japanese
     */
    ja_jp("Japanese"),
    /**
     * Lojban
     */
    jbo_en("Lojban"),
    /**
     * Georgian
     */
    ka_ge("Georgian"),
    /**
     * Kabyle (kab_dz)
     *
     * @apiNote only up to v1.15
     */
    kab_kab("Kabyle"),
    /**
     * Kazakh
     *
     * @apiNote since v1.14
     */
    kk_kz("Kazakh"),
    /**
     * Kannada
     */
    kn_in("Kannada"),
    /**
     * Korean
     */
    ko_kr("Korean"),
    /**
     * Kölsch/Ripuarian
     */
    ksh("Kölsch/Ripuarian"),
    /**
     * Cornish
     */
    kw_gb("Cornish"),
    /**
     * Latin
     */
    la_la("Latin"),
    /**
     * Luxembourgish
     */
    lb_lu("Luxembourgish"),
    /**
     * Limburgish
     */
    li_li("Limburgish"),
    /**
     * Lombard
     *
     * @apiNote since v1.18
     */
    lmo("Lombard"),
    /**
     * LOLCAT
     */
    lol_us("LOLCAT"),
    /**
     * Lithuanian
     */
    lt_lt("Lithuanian"),
    /**
     * Latvian
     */
    lv_lv("Latvian"),
    /**
     * Classical Chinese
     *
     * @apiNote since v1.17.1
     */
    lzh("Classical Chinese"),
    /**
     * Māori
     *
     * @apiNote only up to v1.16
     */
    mi_nz("Māori"),
    /**
     * Macedonian
     */
    mk_mk("Macedonian"),
    /**
     * Mongolian
     */
    mn_mn("Mongolian"),
    /**
     * Mohawk
     *
     * @apiNote only up to v1.15
     */
    moh_ca("Mohawk"),
    /**
     * Malay
     */
    ms_my("Malay"),
    /**
     * Maltese
     */
    mt_mt("Maltese"),
    /**
     * Low German
     */
    nds_de("Low German"),
    /**
     * Dutch, Flemish
     */
    nl_be("Dutch, Flemish"),
    /**
     * Dutch
     */
    nl_nl("Dutch"),
    /**
     * Norwegian Nynorsk
     */
    nn_no("Norwegian Nynorsk"),
    /**
     * Norwegian Bokmål
     */
    no_no("Norwegian Bokmål"),
    /**
     * Nuu-chah-nulth
     *
     * @apiNote only up to v1.15
     */
    nuk("Nuu-chah-nulth"),
    /**
     * Occitan
     */
    oc_fr("Occitan"),
    /**
     * Ojibwe (oji)
     *
     * @apiNote only up to v1.15
     */
    oj_ca("Ojibwe"),
    /**
     * Elfdalian
     */
    ovd("Elfdalian"),
    /**
     * Polish
     */
    pl_pl("Polish"),
    /**
     * Brazilian Portuguese
     */
    pt_br("Brazilian Portuguese"),
    /**
     * Portuguese
     */
    pt_pt("Portuguese"),
    /**
     * Quenya (Form of Elvish from LOTR)
     */
    qya_aa("Quenya (Form of Elvish from LOTR)"),
    /**
     * Romanian
     */
    ro_ro("Romanian"),
    /**
     * Russian (Pre-revolutionary)
     *
     * @apiNote since v1.17
     */
    rpr("Russian (Pre-revolutionary)"),
    /**
     * Russian
     */
    ru_ru("Russian"),
    /**
     * Sicilian
     *
     * @apiNote only available in v1.15.x
     */
    scn("Sicilian"),
    /**
     * Northern Sami
     */
    se_no("Northern Sami"),
    /**
     * Slovak
     */
    sk_sk("Slovak"),
    /**
     * Slovenian
     */
    sl_si("Slovenian"),
    /**
     * Somali
     */
    so_so("Somali"),
    /**
     * Albanian
     */
    sq_al("Albanian"),
    /**
     * Serbian (Cyrillic/Latin)
     */
    sr_sp("Serbian (Cyrillic/Latin)"),
    /**
     * Swedish
     */
    sv_se("Swedish"),
    /**
     * Allgovian German
     *
     * @apiNote only up tp v1.16.x
     */
    swg("Allgovian German"),
    /**
     * Upper Saxon German
     */
    sxu("Upper Saxon German"),
    /**
     * Silesian
     */
    szl("Silesian"),
    /**
     * Tamil
     */
    ta_in("Tamil"),
    /**
     * Thai
     */
    th_th("Thai"),
    /**
     * Tagalog
     *
     * @apiNote since v1.15.1
     */
    tl_ph("Tagalog"),
    /**
     * Klingon
     */
    tlh_aa("Klingon'"),
    /**
     * Toki Pona
     *
     * @apiNote since v1.18
     */
    tok("Toki Pona"),
    /**
     * Turkish
     */
    tr_tr("Turkish"),
    /**
     * Tatar
     *
     * @apiNote since v1.13.1
     */
    tt_ru("Tatar"),
    /**
     * Talossan
     *
     * @apiNote only up to v1.15
     */
    tzl_tzl("Talossan"),
    /**
     * Ukrainian
     */
    uk_ua("Ukrainian"),
    /**
     * Valencian
     */
    val_es("Valencian"),
    /**
     * Venetian
     */
    vec_it("Venetian"),
    /**
     * Vietnamese
     */
    vi_vn("Vietnamese"),
    /**
     * Yiddish
     *
     * @apiNote since v1.15
     */
    yi_de("Yiddish"),
    /**
     * Yoruba
     */
    yo_ng("Yoruba"),
    /**
     * Chinese Simplified (China; Mandarin)
     */
    zh_cn("Chinese Simplified (China; Mandarin)"),
    /**
     * Chinese Traditional (Hong Kong; Mix)
     *
     * @apiNote since v.1.16
     */
    zh_hk("Chinese Traditional (Hong Kong; Mix)"),
    /**
     * Chinese Traditional (Taiwan; Mandarin)
     */
    zh_tw("Chinese Traditional (Taiwan; Mandarin)"),
    /**
     * Malay (Jawi)
     *
     * @apiNote since v1.19
     */
    zlm_arab("Malay (Jawi)");

    private final String name;

    Locale(final String name) {
        this.name = name;
    }

    /**
     * Returns the language name.
     *
     * @return the language name of this locale.
     */
    public String getName() {
        return name;
    }

    public Locale createIfNotExistend() {
        File folder = new File(ClassManager.manager.getPlugin().getDataFolder(), "locales");
        File file = new File(ClassManager.manager.getPlugin().getDataFolder(), "locales/" + this + ".json");
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    // Generate the locale file with its content
                    InputStream inputStream = ClassManager.manager.getPlugin().getResource("locales/" + this + ".json");
                    if (inputStream == null) {
                        ClassManager.manager.getBugFinder().severe("Resource not found: locales/" + this + ".json");
                        return null;
                    }
                    IOUtils.copy(inputStream, outputStream);
                } catch (IOException e) {
                    ClassManager.manager.getBugFinder().severe("Failed to get content of " + this + ".json");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            ClassManager.manager.getBugFinder().severe("Failed to create locale file: " + this + ".json");
            e.printStackTrace();
        }
        return this;
    }
}
