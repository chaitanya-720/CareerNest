package com.example.careernest;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataManager {

    private static final String SESSION_PREF =
            "CareerNestSession";

    private static final String CURRENT_USER_EMAIL =
            "CURRENT_USER_EMAIL";

    private static final String CURRENT_USER_ID =
            "CURRENT_USER_ID";

    // ==========================================
    // GET CURRENT USER EMAIL
    // ==========================================

    public static String getCurrentUserEmail(
            Context context
    ) {

        SharedPreferences session =
                context.getSharedPreferences(
                        SESSION_PREF,
                        Context.MODE_PRIVATE
                );

        return session.getString(
                CURRENT_USER_EMAIL,
                null
        );
    }

    // ==========================================
    // GET CURRENT USER ID
    // ==========================================

    public static String getCurrentUserId(
            Context context
    ) {

        SharedPreferences session =
                context.getSharedPreferences(
                        SESSION_PREF,
                        Context.MODE_PRIVATE
                );

        String userId =
                session.getString(
                        CURRENT_USER_ID,
                        null
                );

        if (userId != null &&
                !userId.trim().isEmpty()) {

            return userId;
        }

        String email =
                getCurrentUserEmail(context);

        if (email == null ||
                email.trim().isEmpty()) {

            return "default_user";
        }

        return createSafeUserId(email);
    }

    // ==========================================
    // CREATE SAFE USER ID
    // ==========================================

    public static String createSafeUserId(
            String email
    ) {

        if (email == null) {
            return "default_user";
        }

        return email
                .trim()
                .toLowerCase()
                .replace("@", "_at_")
                .replace(".", "_dot_");
    }

    // ==========================================
    // PROFILE PREFERENCES
    // ==========================================

    public static SharedPreferences getProfilePreferences(
            Context context
    ) {

        return context.getSharedPreferences(
                "CareerNestProfile_" +
                        getCurrentUserId(context),
                Context.MODE_PRIVATE
        );
    }

    // ==========================================
    // ROADMAP PREFERENCES
    // ==========================================

    public static SharedPreferences getRoadmapPreferences(
            Context context
    ) {

        return context.getSharedPreferences(
                "CareerNestRoadmap_" +
                        getCurrentUserId(context),
                Context.MODE_PRIVATE
        );
    }

    // ==========================================
    // SAVED CAREERS PREFERENCES
    // ==========================================

    public static SharedPreferences getSavedCareersPreferences(
            Context context
    ) {

        return context.getSharedPreferences(
                "CareerNestSavedCareers_" +
                        getCurrentUserId(context),
                Context.MODE_PRIVATE
        );
    }

    // ==========================================
    // CHECK LOGIN
    // ==========================================

    public static boolean isLoggedIn(
            Context context
    ) {

        SharedPreferences session =
                context.getSharedPreferences(
                        SESSION_PREF,
                        Context.MODE_PRIVATE
                );

        return session.getBoolean(
                "IS_LOGGED_IN",
                false
        );
    }

    // ==========================================
    // LOGOUT
    // ==========================================

    public static void logout(
            Context context
    ) {

        SharedPreferences session =
                context.getSharedPreferences(
                        SESSION_PREF,
                        Context.MODE_PRIVATE
                );

        session.edit()
                .clear()
                .apply();
    }
}