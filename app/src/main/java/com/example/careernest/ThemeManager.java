package com.example.careernest;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREF_NAME =
            "CareerNestSettings";

    private static final String DARK_MODE =
            "DARK_MODE";

    public static void applySavedTheme(Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        boolean darkMode =
                preferences.getBoolean(
                        DARK_MODE,
                        false
                );

        if (darkMode) {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );

        } else {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
            );
        }
    }

    public static void setDarkMode(
            Context context,
            boolean enabled
    ) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        preferences
                .edit()
                .putBoolean(
                        DARK_MODE,
                        enabled
                )
                .apply();

        if (enabled) {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
            );

        } else {

            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
            );
        }
    }

    public static boolean isDarkMode(Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );

        return preferences.getBoolean(
                DARK_MODE,
                false
        );
    }
}