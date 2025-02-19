/*
 * Copyright (C) 2012 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package net.zorgblub.typhonkai;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.preference.PreferenceManager;
import androidx.core.content.ContextCompat;

import net.nightwhistler.htmlspanner.FontFamily;
import net.zorgblub.typhonkai.activity.ReadingActivity;
import net.zorgblub.typhonkai.activity.TyphonActivity;
import net.zorgblub.typhonkai.dto.HighLight;
import net.zorgblub.typhonkai.dto.PageOffsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jedi.option.Option;

import static jedi.functional.Coercions.asList;
import static jedi.functional.FunctionalPrimitives.firstOption;
import static jedi.functional.FunctionalPrimitives.isEmpty;
import static jedi.functional.FunctionalPrimitives.select;
import static jedi.option.Options.none;
import static jedi.option.Options.option;
import static net.zorgblub.typhonkai.CustomOPDSSite.fromJSON;

/**
 * Application configuration class which provides a friendly API to the various
 * settings available.
 *
 * @author Alex Kuiper
 */
public class Configuration {

    private SharedPreferences settings;
    private Context context;

    private Map<String, FontFamily> fontCache = new HashMap<>();

    public enum ScrollStyle {
        ROLLING_BLIND, PAGE_TIMER
    }

    public enum AnimationStyle {
        CURL, SLIDE, NONE
    }

    public enum OrientationLock {
        PORTRAIT, LANDSCAPE, REVERSE_PORTRAIT, REVERSE_LANDSCAPE, NO_LOCK
    }

    public enum ColourProfile {
        DAY, NIGHT
    }

    public enum LibraryView {
        BOOKCASE, LIST
    }

    public enum CoverLabelOption {
        ALWAYS, NEVER, WITHOUT_COVER
    }

    public enum LibrarySelection {
        BY_LAST_READ, LAST_ADDED, UNREAD, BY_TITLE, BY_AUTHOR
    }

    public enum ReadingDirection {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    public enum LongShortPressBehaviour {
        NORMAL, REVERSED
    }


    public static final String BASE_OPDS_FEED = "http://www.pageturner-reader.org/opds/feeds.xml";
    public static final String BASE_SYNC_URL = "http://api.pageturner-reader.org/progress/";
    public static final String KEY_SYNC_SERVER = "sync_server";

    public static final String KEY_POS = "offset:";
    public static final String KEY_IDX = "index:";
    public static final String KEY_NAV_TAP_V = "nav_tap_v";
    public static final String KEY_NAV_TAP_H = "nav_tap_h";
    public static final String KEY_NAV_SWIPE_H = "nav_swipe_h";
    public static final String KEY_NAV_SWIPE_V = "nav_swipe_v";
    public static final String KEY_NAV_VOL = "nav_vol";
    public static final String KEY_NAV_VOL_CHAPTERS = "nav_vol_chapters";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_FULL_SCREEN = "full_screen";
    public static final String KEY_COPY_TO_LIB = "copy_to_library";
    public static final String KEY_STRIP_WHITESPACE = "strip_whitespace";
    public static final String KEY_SCROLLING = "scrolling";

    // Rikai
    public static final String KEY_RIKAI = "rikai";
    public static final String KEY_DICTIONARY_VERSION = "dictionary version";
    private static final String KEY_DICTIONARY_SETTINGS = "dictionary_settings";
    private static final String KEY_ANKI_DECK_NAME = "anki_deck_name";

    public static final String KEY_LAST_FILE = "last_file";
    public static final String KEY_DEVICE_NAME = "device_name";
    public static final String KEY_TEXT_SIZE = "itext_size";

    public static final String KEY_MARGIN_H = "margin_h";
    public static final String KEY_MARGIN_V = "margin_v";

    public static final String KEY_LINE_SPACING = "line_spacing";

    public static final String KEY_NIGHT_MODE = "night_mode";
    public static final String KEY_SCREEN_ORIENTATION = "screen_orientation";
    public static final String KEY_FONT_FACE = "font_face";
    public static final String KEY_SERIF_FONT = "serif_font";
    public static final String KEY_SANS_SERIF_FONT = "sans_serif_font";

    public static final String PREFIX_DAY = "day";
    public static final String PREFIX_NIGHT = "night";

    public static final String KEY_BRIGHTNESS = "bright";
    public static final String KEY_BACKGROUND = "bg";
    public static final String KEY_LINK = "link";
    public static final String KEY_TEXT = "text";
    public static final String KEY_HIGHLIGHT = "highlight";

    public static final String KEY_BRIGHTNESS_CTRL = "set_brightness";
    public static final String KEY_SCROLL_STYLE = "scroll_style";
    public static final String KEY_SCROLL_SPEED = "scroll_speed";

    public static final String KEY_H_ANIMATION = "h_animation";
    public static final String KEY_V_ANIMATION = "v_animation";

    public static final String KEY_LIB_VIEW = "library_view";
    public static final String KEY_LIB_SEL = "library_selection";

    public static final String ACCESS_KEY = "access_key";
    public static final String CALIBRE_SERVER = "calibre_server";
    public static final String CALIBRE_USER = "calibre_user";
    public static final String CALIBRE_PASSWORD = "calibre_password";

    public static final String KEY_COVER_LABELS = "cover_labels";

    public static final String KEY_KEEP_SCREEN_ON = "keep_screen_on";

    public static final String KEY_OFFSETS = "offsets";
    public static final String KEY_SHOW_PAGENUM = "show_pagenum";

    public static final String KEY_OPDS_SITES = "opds_sites";

    public static final String KEY_READING_DIRECTION = "reading_direction";

    public static final String KEY_DIM_SYSTEM_UI = "dim_system_ui";

    public static final String KEY_ACCEPT_SELF_SIGNED = "accept_self_signed";

    public static final String KEY_LONG_SHORT = "long_short";

    public static final String KEY_NOOK_TOP_BUTTONS_DIRECTION = "nook_touch_top_buttons_direction";

    public static final String KEY_HIGHLIGHTS = "highlights";

    public static final String KEY_ALLOW_STYLING = "allow_styling";
    public static final String KEY_ALLOW_STYLE_COLOURS = "allow_style_colours";

    public static final String KEY_LAST_TITLE = "last_title";

    public static final String KEY_LAST_ACTIVITY = "last_activity";

    public static final String KEY_ALWAYS_OPEN_LAST_BOOK = "always_open_last_book";

    public static final String KEY_SCAN_FOLDER = "scan_folder";
    public static final String KEY_USE_SCAN_FOLDER = "use_scan_folder";


    // Flag for whether Typhon is running on a Nook Simple Touch - an e-ink
    // based Android device

    // NB: Believe product/model field is "NOOK" on a Nook Touch and 'NookColor'
    // on a Nook Color
    public static final Boolean IS_NOOK_TOUCH = "NOOK".equals(Build.PRODUCT);

    // Flag for any e-ink device. Currently only supports Nook Touch but could
    // expand to other devices like the Sony PRS-T1
    public static final Boolean IS_EINK_DEVICE = IS_NOOK_TOUCH;

    //Which platform version to start text selection on.
    public static final int TEXT_SELECTION_PLATFORM_VERSION = Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    private static final Logger LOG = LoggerFactory
            .getLogger("Configuration");

    private String defaultSerifFont;
    private String defaultSansFont;


    public Configuration(Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;

        if (IS_NOOK_TOUCH) {
            defaultSerifFont = "serif";
            defaultSansFont = "sans";
        } else {
            defaultSerifFont = "IPAMincho";
            defaultSansFont = "IPAMincho";
        }

        // On Nook Touch, preset some different defaults on first load
        // (these values work better w/ e-ink)
        if (IS_NOOK_TOUCH && this.settings.getString(KEY_DEVICE_NAME, null) == null) {
            SharedPreferences.Editor editor = this.settings.edit();
            editor.putString(KEY_FONT_FACE, "sans");
            editor.putString(KEY_SERIF_FONT, "serif");

            editor.putInt(KEY_TEXT_SIZE, 32);
            editor.putString(KEY_SCROLL_STYLE, "timer"); // enum is ScrollStyle.PAGE_TIMER
            final String no_animation = AnimationStyle.NONE.name().toLowerCase(Locale.US);
            editor.putString(KEY_H_ANIMATION, no_animation);
            editor.putString(KEY_V_ANIMATION, no_animation);
            editor.putInt(PREFIX_DAY + "_" + KEY_LINK,
                    Color.rgb(0x40, 0x40, 0x40));
            editor.putInt(PREFIX_NIGHT + "_" + KEY_TEXT, Color.WHITE);
            editor.putInt(PREFIX_NIGHT + "_" + KEY_LINK,
                    Color.rgb(0xb0, 0xb0, 0xb0));
            editor.commit();
        }
    }

    public String getAppVersion() {
        String version = "";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }

        return version;
    }

    public String getUserAgent() {
        return context.getString(R.string.app_name) + "-" + getAppVersion()
                + "/" + Build.MODEL + ";Android-" + Build.VERSION.RELEASE;
    }

    public Locale getLocale() {

        String languageSetting = settings.getString("custom_lang", "default");

        if ("default".equalsIgnoreCase(languageSetting)) {
            return Locale.getDefault();
        }

        return new Locale(languageSetting);
    }

    public Class<? extends TyphonActivity> getLastActivity() {
        String lastActivityString = settings.getString(KEY_LAST_ACTIVITY,
                "ReadingActivity");

        try {
            return (Class<? extends TyphonActivity>) Class.forName(lastActivityString);
        } catch (ClassNotFoundException n) {
            return ReadingActivity.class;
        }

    }

    public void setLastActivity(Class<? extends TyphonActivity> activityClass) {
        updateValue(KEY_LAST_ACTIVITY, activityClass.getCanonicalName());
    }

    public String getBaseOPDSFeed() {
        return BASE_OPDS_FEED;
    }

    public String getSyncServerURL() {
        return settings.getString(KEY_SYNC_SERVER, BASE_SYNC_URL).trim();
    }

    public boolean isAlwaysOpenLastBook() {
        return settings.getBoolean(KEY_ALWAYS_OPEN_LAST_BOOK, false);
    }

    public boolean isVerticalTappingEnabled() {
        return !isRikaiEnabled() && settings.getBoolean(KEY_NAV_TAP_V, true);
    }

    public boolean isHorizontalTappingEnabled() {
        return !isRikaiEnabled() && settings.getBoolean(KEY_NAV_TAP_H, true);
    }

    public boolean isHorizontalSwipeEnabled() {
        return settings.getBoolean(KEY_NAV_SWIPE_H, true);
    }

    public boolean isVerticalSwipeEnabled() {
        return settings.getBoolean(KEY_NAV_SWIPE_V, true)
                && !isScrollingEnabled();
    }

    public boolean isAcceptSelfSignedCertificates() {
        return settings.getBoolean(KEY_ACCEPT_SELF_SIGNED, false);
    }

    public boolean isAllowStyling() {
        return settings.getBoolean(KEY_ALLOW_STYLING, true);
    }

    public int getLastPosition(String fileName) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);

        int pos = bookPrefs.getInt(KEY_POS, -1);

        if (pos != -1) {
            return pos;
        }

        //Fall-back to older settings
        String bookHash = Integer.toHexString(fileName.hashCode());

        pos = settings.getInt(KEY_POS + bookHash, -1);

        if (pos != -1) {
            return pos;
        }

        // Fall-back for even older settings.
        return settings.getInt(KEY_POS + fileName, -1);
    }

    private SharedPreferences getPrefsForBook(String fileName) {

        String bookHash = Integer.toHexString(fileName.hashCode());
        return context.getSharedPreferences(bookHash, 0);
    }

    public void setPageOffsets(String fileName, List<List<Integer>> offsets) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);

        PageOffsets offsetsObject = PageOffsets.fromValues(this, offsets);

        try {
            String json = offsetsObject.toJSON();
            updateValue(bookPrefs, KEY_OFFSETS, json);
        } catch (JSONException js) {
            LOG.error("Error storing page offsets", js);
        }
    }

    public Option<List<List<Integer>>> getPageOffsets(String fileName) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);

        String data = bookPrefs.getString(KEY_OFFSETS, "");

        if (data.length() == 0) {
            return none();
        }

        try {
            PageOffsets offsets = PageOffsets.fromJSON(data);

            if (offsets == null || !offsets.isValid(this)) {
                return none();
            }

            return option(offsets.getOffsets());

        } catch (JSONException js) {
            LOG.error("Could not retrieve page offsets", js);
            return none();
        }
    }

    public List<HighLight> getHightLights(String fileName) {
        SharedPreferences prefs = getPrefsForBook(fileName);

        if (prefs.contains(KEY_HIGHLIGHTS)) {
            return HighLight.fromJSON(fileName, prefs.getString(KEY_HIGHLIGHTS, "[]"));
        }

        return new ArrayList<>();
    }

    public void storeHighlights(String fileName, List<HighLight> highLights) {
        SharedPreferences pref = getPrefsForBook(fileName);
        updateValue(pref, KEY_HIGHLIGHTS, HighLight.toJSON(highLights));
    }

    public LongShortPressBehaviour getLongShortPressBehaviour() {
        String value = settings.getString(KEY_LONG_SHORT,
                LongShortPressBehaviour.NORMAL.name());
        return LongShortPressBehaviour.valueOf(value.toUpperCase(Locale.US));
    }

    public ReadingDirection getReadingDirection() {
        String value = settings.getString(KEY_READING_DIRECTION,
                ReadingDirection.LEFT_TO_RIGHT.name());
        return ReadingDirection.valueOf(value.toUpperCase(Locale.US));
    }

    public void setLastPosition(String fileName, int position) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);

        updateValue(bookPrefs, KEY_POS, position);
    }

    public int getLastIndex(String fileName) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);

        int pos = bookPrefs.getInt(KEY_IDX, -1);

        if (pos != -1) {
            return pos;
        }

        //Fall-backs to older setting in central file
        String bookHash = Integer.toHexString(fileName.hashCode());

        pos = settings.getInt(KEY_IDX + bookHash, -1);

        if (pos != -1) {
            return pos;
        }

        // Fall-back for even older settings.
        return settings.getInt(KEY_IDX + fileName, -1);
    }

    public List<CustomOPDSSite> getCustomOPDSSites() {

        String sites = settings.getString(KEY_OPDS_SITES, "[]");

        List<CustomOPDSSite> result = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(sites);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                CustomOPDSSite site = fromJSON(obj);
                result.add(site);
            }
        } catch (JSONException js) {
            LOG.error("Could not retrieve custom opds sites", js);
        }

        importOldCalibreSite(result);
        return result;
    }

    private void importOldCalibreSite(List<CustomOPDSSite> sites) {

        if (this.getCalibreServer() != null
                && this.getCalibreServer().length() > 0) {
            CustomOPDSSite calibre = new CustomOPDSSite();
            calibre.setName(context.getString(R.string.pref_calibre_server));
            calibre.setUrl(getCalibreServer());
            calibre.setUserName(getCalibreUser());
            calibre.setPassword(getCalibrePassword());

            sites.add(calibre);

            updateValue(CALIBRE_SERVER, null);

            storeCustomOPDSSites(sites);
        }

    }

    public void storeCustomOPDSSites(List<CustomOPDSSite> sites) {

        try {
            JSONArray array = new JSONArray();
            for (CustomOPDSSite site : sites) {
                array.put(site.toJSON());
            }

            updateValue(KEY_OPDS_SITES, array.toString());
        } catch (JSONException js) {
            LOG.error("Error storing custom sites", js);
        }
    }

    public void setLastIndex(String fileName, int index) {

        SharedPreferences bookPrefs = getPrefsForBook(fileName);
        updateValue(bookPrefs, KEY_IDX, index);
    }

    public boolean isVolumeKeyNavEnabled() {
        return settings.getBoolean(KEY_NAV_VOL, false);
    }

    public boolean isVolumeKeyNavChaptersEnabled() {
        return settings.getBoolean(KEY_NAV_VOL_CHAPTERS, false);
    }

    public boolean isNookUpButtonForward() {
        return !isScrollingEnabled()
                && "forward".equals(settings.getString(
                KEY_NOOK_TOP_BUTTONS_DIRECTION, "backward"));
    }

    public String getSynchronizationEmail() {
        return settings.getString(KEY_EMAIL, "").trim();
    }

    public boolean isShowPageNumbers() {
        return settings.getBoolean(KEY_SHOW_PAGENUM, true);
    }

    public String getSynchronizationAccessKey() {
        return settings.getString(ACCESS_KEY, "").trim();
    }

    public boolean isDimSystemUI() {
        return isFullScreenEnabled() && settings.getBoolean(KEY_DIM_SYSTEM_UI, false);
    }

    public boolean isSyncEnabled() {
        String email = getSynchronizationEmail();

        return email.length() > 0;
    }

    public boolean isFullScreenEnabled() {
        return settings.getBoolean(KEY_FULL_SCREEN, false);
    }

    public boolean isStripWhiteSpaceEnabled() {
        return settings.getBoolean(KEY_STRIP_WHITESPACE, false);
    }

    public boolean isRikaiEnabled() {
        return settings.getBoolean(KEY_RIKAI, true);
    }

    public String getAnkiDeckName(){
        return settings.getString(KEY_ANKI_DECK_NAME, context.getString(R.string.pref_anki_deck_name_default));
    }

    public boolean isScrollingEnabled() {
        return false;
        //return isRikaiEnabled() || settings.getBoolean(KEY_SCROLLING, false);
    }

    public String getLastReadTitle() {
        return settings.getString(KEY_LAST_TITLE, "");
    }

    public void setLastReadTitle(String title) {
        updateValue(KEY_LAST_TITLE, title);
    }

    public String getLastOpenedFile() {
        return settings.getString(KEY_LAST_FILE, "");
    }

    public void setLastOpenedFile(String fileName) {
        updateValue(KEY_LAST_FILE, fileName);
    }

    public String getDeviceName() {
        return settings.getString(KEY_DEVICE_NAME, Build.MODEL);
    }

    public int getBottomEmptySpace() {
        return settings.getInt("dict_space", 7);
    }

    public int getTextSize() {
        return settings.getInt(KEY_TEXT_SIZE, 18);
    }

    public void setTextSize(int textSize) {
        updateValue(KEY_TEXT_SIZE, textSize);
    }

    public int getHorizontalMargin() {
        return settings.getInt(KEY_MARGIN_H, 30);
    }

    public int getVerticalMargin() {
        return settings.getInt(KEY_MARGIN_V, 25);
    }

    public int getLineSpacing() {
        return settings.getInt(KEY_LINE_SPACING, 0);
    }

    public boolean isKeepScreenOn() {
        return settings.getBoolean(KEY_KEEP_SCREEN_ON, false);
    }

    public int getTheme() {
        if (getColourProfile() == ColourProfile.NIGHT) {
            return R.style.Theme_AppCompat;
        } else {
            return R.style.Theme_AppCompat_Light_DarkActionBar;
        }

    }

    public void setColourProfile(ColourProfile profile) {
        if (profile == ColourProfile.DAY) {
            updateValue(KEY_NIGHT_MODE, false);
        } else {
            updateValue(KEY_NIGHT_MODE, true);
        }
    }

    public CoverLabelOption getCoverLabelOption() {
        return CoverLabelOption.valueOf(settings.getString(KEY_COVER_LABELS,
                CoverLabelOption.ALWAYS.name().toLowerCase(Locale.US)));
    }

    public ColourProfile getColourProfile() {
        if (settings.getBoolean(KEY_NIGHT_MODE, false)) {
            return ColourProfile.NIGHT;
        } else {
            return ColourProfile.DAY;
        }
    }

    public OrientationLock getScreenOrientation() {
        String orientation = settings.getString(KEY_SCREEN_ORIENTATION,
                OrientationLock.NO_LOCK.name().toLowerCase(Locale.US));
        return OrientationLock.valueOf(orientation.toUpperCase(Locale.US));
    }

    private void updateValue(SharedPreferences prefs, String key, Object value) {
        SharedPreferences.Editor editor = prefs.edit();

        if (value == null) {
            editor.remove(key);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else {
            throw new IllegalArgumentException("Unsupported type: "
                    + value.getClass().getSimpleName());
        }

        editor.commit();
    }

    public String getDictionaryVersion() {
        return settings.getString(KEY_DICTIONARY_VERSION, "1.0.0");
    }

    public void setDictionaryVersion(String version) {
        updateValue(KEY_DICTIONARY_VERSION, version);
    }

    public String getDictionarySettings() {
        return settings.getString(KEY_DICTIONARY_SETTINGS, "");
    }

    public void setDictionarySettings(String settings) {
        updateValue(KEY_DICTIONARY_SETTINGS, settings);
    }

    private void updateValue(String key, Object value) {
        updateValue(settings, key, value);
    }

	private FontFamily loadFamilyFromAssetsTTF(String key, String baseName) {
        Typeface basic = Typeface.createFromAsset(context.getAssets(), baseName
                + ".ttf");
        Typeface boldFace = basic;
        Typeface italicFace = basic;
        Typeface biFace = basic;

        FontFamily fam = new FontFamily(key, basic);
        fam.setBoldTypeface(boldFace);
        fam.setItalicTypeface(italicFace);
        fam.setBoldItalicTypeface(biFace);

        return fam;
    }

    private FontFamily loadFamilyFromAssets(String key, String baseName, boolean skipVariants) {
        Typeface basic = Typeface.createFromAsset(context.getAssets(), baseName
                + ".otf");
        Typeface boldFace = basic;
        Typeface italicFace = basic;
        Typeface biFace = basic;

        if (!skipVariants) {
            boldFace = Typeface.createFromAsset(context.getAssets(),
                    baseName + "-Bold.otf");
            italicFace = Typeface.createFromAsset(context.getAssets(),
                    baseName + "-Italic.otf");
            biFace = Typeface.createFromAsset(context.getAssets(),
                    baseName + "-BoldItalic.otf");
        }

        FontFamily fam = new FontFamily(key, basic);
        fam.setBoldTypeface(boldFace);
        fam.setItalicTypeface(italicFace);
        fam.setBoldItalicTypeface(biFace);

        return fam;
    }

    private FontFamily getFontFamily(String fontKey, String defaultVal) {

        String fontFace = settings.getString(fontKey, defaultVal);

        if (!fontCache.containsKey(fontFace)) {

            if ("mamelon".equals(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "Mamelon", true));
            } else if ("boku2r".equals(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "Boku2", true));
            } else if ("kokoro".equalsIgnoreCase(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "Kokoro", true));
            } else if ("gen_book_bas".equals(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "GentiumBookBasic", false));
            } else if ("gen_bas".equals(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "GentiumBasic", false));
            } else if ("frankruehl".equalsIgnoreCase(fontFace)) {
                fontCache.put(fontFace,
                        loadFamilyFromAssets(fontFace, "FrankRuehl", false));
			} else if ("IPAMincho".equalsIgnoreCase(fontFace)) {
                fontCache.put(fontFace,
							  loadFamilyFromAssetsTTF(fontFace, "ipaexm"));
			} else if ("IPAGothic".equalsIgnoreCase(fontFace)) {
                fontCache.put(fontFace,
							  loadFamilyFromAssetsTTF(fontFace, "ipaexg"));
            } else {

                Typeface face = Typeface.SANS_SERIF;
                if ("sans".equals(fontFace)) {
                    face = Typeface.SANS_SERIF;
                } else if ("serif".equals(fontFace)) {
                    face = Typeface.SERIF;
                } else if ("mono".equals(fontFace)) {
                    face = Typeface.MONOSPACE;
                } else if ("default".equals(fontFace)) {
                    face = Typeface.DEFAULT;
                }

                fontCache.put(fontFace, new FontFamily(fontFace, face));
            }
        }

        return fontCache.get(fontFace);
    }

    public FontFamily getSerifFontFamily() {
        return getFontFamily(KEY_SERIF_FONT, defaultSerifFont);
    }

    public FontFamily getSansSerifFontFamily() {
        return getFontFamily(KEY_SANS_SERIF_FONT, defaultSansFont);
    }

    public FontFamily getDefaultFontFamily() {
        return getFontFamily(KEY_FONT_FACE, defaultSerifFont);
    }

    public int getBrightNess() {
        // Brightness 0 means black screen :)
        return Math.max(1, getProfileSetting(KEY_BRIGHTNESS, 50, 50));
    }

    public void setBrightness(int brightness) {
        if (getColourProfile() == ColourProfile.DAY) {
            updateValue("day_bright", brightness);
        } else {
            updateValue("night_bright", brightness);
        }
    }

    public int getBackgroundColor() {
        return getProfileSetting(KEY_BACKGROUND, Color.WHITE, Color.BLACK);
    }

    public int getTextColor() {
        return getProfileSetting(KEY_TEXT, Color.BLACK, Color.GRAY);
    }

    public int getLinkColor() {
        return getProfileSetting(KEY_LINK, Color.BLUE, Color.rgb(255, 165, 0));
    }

    public int getHighlightColor() {
        return getProfileSetting(KEY_HIGHLIGHT, Color.YELLOW, Color.YELLOW);
    }

    public boolean isUseColoursFromCSS() {

        String setting = KEY_ALLOW_STYLE_COLOURS;
        boolean nightDefault = false;
        boolean dayDefault = true;

        if (getColourProfile() == ColourProfile.NIGHT) {
            return settings.getBoolean(PREFIX_NIGHT + "_" + setting, nightDefault);
        } else {
            return settings.getBoolean(PREFIX_DAY + "_" + setting, dayDefault);
        }
    }


    private int getProfileSetting(String setting, int dayDefault,
                                  int nightDefault) {

        if (getColourProfile() == ColourProfile.NIGHT) {
            return settings.getInt(PREFIX_NIGHT + "_" + setting, nightDefault);
        } else {
            return settings.getInt(PREFIX_DAY + "_" + setting, dayDefault);
        }

    }

    public boolean isBrightnessControlEnabled() {
        return settings.getBoolean(KEY_BRIGHTNESS_CTRL, false);
    }

    public ScrollStyle getAutoScrollStyle() {
        String style = settings.getString(KEY_SCROLL_STYLE,
                ScrollStyle.ROLLING_BLIND.name().toLowerCase(Locale.US));
        if ("rolling_blind".equals(style)) {
            return ScrollStyle.ROLLING_BLIND;
        } else {
            return ScrollStyle.PAGE_TIMER;
        }
    }

    public int getScrollSpeed() {
        return settings.getInt(KEY_SCROLL_SPEED, 20);
    }

    public AnimationStyle getHorizontalAnim() {
        String animH = settings.getString(KEY_H_ANIMATION, AnimationStyle.SLIDE
                .name().toLowerCase(Locale.US));
        return AnimationStyle.valueOf(animH.toUpperCase(Locale.US));
    }

    public AnimationStyle getVerticalAnim() {
        String animV = settings.getString(KEY_V_ANIMATION, AnimationStyle.SLIDE
                .name().toLowerCase(Locale.US));
        return AnimationStyle.valueOf(animV.toUpperCase(Locale.US));
    }

    public LibraryView getLibraryView() {
        String libView = settings.getString(KEY_LIB_VIEW, LibraryView.BOOKCASE
                .name().toLowerCase(Locale.US));
        return LibraryView.valueOf(libView.toUpperCase(Locale.US));
    }

    public void setLibraryView(LibraryView viewStyle) {
        String libView = viewStyle.name().toLowerCase(Locale.US);
        updateValue(KEY_LIB_VIEW, libView);
    }

    public LibrarySelection getLastLibraryQuery() {
        String query = settings.getString(KEY_LIB_SEL,
                LibrarySelection.LAST_ADDED.name().toLowerCase(Locale.US));
        return LibrarySelection.valueOf(query.toUpperCase(Locale.US));
    }

    public void setLastLibraryQuery(LibrarySelection sel) {
        updateValue(KEY_LIB_SEL, sel.name().toLowerCase(Locale.US));
    }

    public String getCalibreServer() {
        return settings.getString(CALIBRE_SERVER, "");
    }

    public String getCalibreUser() {
        return settings.getString(CALIBRE_USER, "");
    }

    public String getCalibrePassword() {
        return settings.getString(CALIBRE_PASSWORD, "");
    }

    public Option<File> getStorageBase() {
        return option(Environment.getExternalStorageDirectory());
    }

    public Option<File> getDownloadsFolder() {

        return firstOption(
                asList(
                        ContextCompat.getExternalFilesDirs(context, "Downloads")
                )
        );
    }

    public Option<File> getLibraryFolder() {

        Option<File> libraryFolder = getStorageBase().map(
                baseFolder -> new File(baseFolder.getAbsolutePath() + "/Typhon/Books"));

        //If the library-folder on external storage exists, return it
        if (!isEmpty(select(libraryFolder, File::exists))) {
            return libraryFolder;
        }

        if (!isEmpty(libraryFolder)) {
            try {
                boolean result = libraryFolder.unsafeGet().mkdirs();

                if (result) {
                    return libraryFolder;
                }

            } catch (Exception e) {
            }
        }

        return firstOption(
                asList(
                        ContextCompat.getExternalFilesDirs(context, "Books")
                )
        );
    }

    public Option<File> getTTSFolder() {
        return firstOption(
                asList(
                        ContextCompat.getExternalCacheDirs(context)
                )
        );
    }

    public boolean getCopyToLibraryOnScan() {
        return settings.getBoolean(KEY_COPY_TO_LIB, true);
    }

    public void setCopyToLibraryOnScan(boolean value) {
        settings.edit()
                .putBoolean(KEY_COPY_TO_LIB, value)
                .commit();
    }

    public boolean getUseCustomScanFolder() {
        return settings.getBoolean(KEY_USE_SCAN_FOLDER, false);
    }

    public void setUseCustomScanFolder(boolean value) {
        settings.edit()
                .putBoolean(KEY_USE_SCAN_FOLDER, value)
                .commit();
    }

    /**
     * Return the default folder path which is shown for the "scan for books" custom directory
     */
    private String getDefaultScanFolder() {
        return Configuration.IS_NOOK_TOUCH ?
                "/media" : /* Nook's default internal content storage (accessible via USB) is under /media */
                getStorageBase().unsafeGet().getAbsolutePath() + "/eBooks";
    }

    /**
     * Return the folder path to show for the "scan for books" custom directory
     */
    public String getScanFolder() {
        return settings.getString(KEY_SCAN_FOLDER, getDefaultScanFolder());
    }

    /**
     * Set the folder path to show for "scan for books" custom directory.
     * Will only save a setting if the default actually changed.
     */
    public void setScanFolder(String value) {
        SharedPreferences.Editor editor = settings.edit();

        if (value == null || value.equals(getDefaultScanFolder())) {
            if (!settings.contains(KEY_SCAN_FOLDER))
                return;
            editor.remove(KEY_SCAN_FOLDER);
        } else if (new File(value).isDirectory()) {
            editor.putString(KEY_SCAN_FOLDER, value);
        }

        editor.commit();
    }

    /**
     * Returns the bytes of available memory left on the heap. Not totally sure
     * if it works reliably.
     */
    public static double getMemoryUsage() {
        long max = Runtime.getRuntime().maxMemory();
        long used = Runtime.getRuntime().totalMemory();

        return (double) used / (double) max;
    }

    /*
        Returns the available bitmap memory.
        On newer Android versions this is the same as the normaL
        heap memory.
     */
    public static double getBitmapMemoryUsage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return getMemoryUsage();
        }

        long max = Runtime.getRuntime().maxMemory();
        long used = Debug.getNativeHeapAllocatedSize();

        return (double) used / (double) max;
    }
}
