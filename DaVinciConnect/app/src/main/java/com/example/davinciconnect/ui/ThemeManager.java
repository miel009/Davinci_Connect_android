package com.example.davinciconnect.ui;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREFS_NAME = "ThemePrefs";
    private static final String THEME_KEY = "theme_key";
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static void applyTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int theme = prefs.getInt(THEME_KEY, AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ? THEME_DARK : THEME_LIGHT);
        if (theme == THEME_DARK) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void toggleTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentTheme = prefs.getInt(THEME_KEY, THEME_LIGHT);
        int newTheme = currentTheme == THEME_LIGHT ? THEME_DARK : THEME_LIGHT;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(THEME_KEY, newTheme);
        editor.apply();
        applyTheme(context);
    }
}
