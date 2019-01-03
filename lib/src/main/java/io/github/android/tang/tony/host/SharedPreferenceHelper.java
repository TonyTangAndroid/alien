package io.github.android.tang.tony.host;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hugo.weaving.DebugLog;

@DebugLog
public class SharedPreferenceHelper {

    public static String PREF_KEY_ENABLED = "pref_key_enabled";
    private final SharedPreferences prefs;

    public SharedPreferenceHelper(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean enabled() {
        return prefs.getBoolean(PREF_KEY_ENABLED, false);
    }

    public void update(boolean enabled) {
        prefs.edit().putBoolean(PREF_KEY_ENABLED, enabled).apply();
    }

}