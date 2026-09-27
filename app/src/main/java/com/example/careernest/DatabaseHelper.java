package com.example.careernest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME =
            "CareerNest.db";

    private static final int DATABASE_VERSION = 1;

    // ==========================================
    // TABLE NAMES
    // ==========================================

    public static final String TABLE_USERS =
            "users";

    public static final String TABLE_PROFILES =
            "profiles";

    public static final String TABLE_SAVED_CAREERS =
            "saved_careers";

    public static final String TABLE_ROADMAP =
            "roadmap_progress";

    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    // ==========================================
    // ENABLE FOREIGN KEYS
    // ==========================================

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // ==========================================
    // CREATE DATABASE
    // ==========================================

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ======================================
        // USERS TABLE
        // ======================================

        db.execSQL(
                "CREATE TABLE " + TABLE_USERS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "email TEXT UNIQUE NOT NULL, " +
                        "password TEXT NOT NULL" +
                        ")"
        );

        // ======================================
        // PROFILES TABLE
        // ======================================

        db.execSQL(
                "CREATE TABLE " + TABLE_PROFILES + " (" +
                        "user_id INTEGER PRIMARY KEY, " +
                        "college TEXT, " +
                        "course TEXT, " +
                        "skills TEXT, " +
                        "last_roadmap_career TEXT, " +
                        "FOREIGN KEY(user_id) REFERENCES users(id) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // ======================================
        // SAVED CAREERS TABLE
        // ======================================

        db.execSQL(
                "CREATE TABLE " + TABLE_SAVED_CAREERS + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id INTEGER NOT NULL, " +
                        "career_name TEXT NOT NULL, " +
                        "UNIQUE(user_id, career_name), " +
                        "FOREIGN KEY(user_id) REFERENCES users(id) " +
                        "ON DELETE CASCADE" +
                        ")"
        );

        // ======================================
        // ROADMAP TABLE
        // ======================================

        db.execSQL(
                "CREATE TABLE " + TABLE_ROADMAP + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "user_id INTEGER NOT NULL, " +
                        "career_name TEXT NOT NULL, " +
                        "step_number INTEGER NOT NULL, " +
                        "completed INTEGER DEFAULT 0, " +
                        "UNIQUE(user_id, career_name, step_number), " +
                        "FOREIGN KEY(user_id) REFERENCES users(id) " +
                        "ON DELETE CASCADE" +
                        ")"
        );
    }

    // ==========================================
    // DATABASE UPGRADE
    // ==========================================

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_ROADMAP
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_SAVED_CAREERS
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_PROFILES
        );

        db.execSQL(
                "DROP TABLE IF EXISTS " +
                        TABLE_USERS
        );

        onCreate(db);
    }

    // =========================================================
    // USER METHODS
    // =========================================================

    // ==========================================
    // CREATE USER
    // ==========================================

    public long createUser(
            String name,
            String email,
            String password
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "email",
                email.toLowerCase()
        );

        values.put(
                "password",
                password
        );

        return db.insert(
                TABLE_USERS,
                null,
                values
        );
    }

    // ==========================================
    // CHECK EMAIL
    // ==========================================

    public boolean emailExists(
            String email
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_USERS,
                        new String[]{"id"},
                        "email = ?",
                        new String[]{
                                email.toLowerCase()
                        },
                        null,
                        null,
                        null
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        return exists;
    }

    // ==========================================
    // CHECK EMAIL FOR ANOTHER USER
    // ==========================================

    public boolean emailExistsForOtherUser(
            String email,
            int userId
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_USERS,
                        new String[]{"id"},
                        "email = ? AND id != ?",
                        new String[]{
                                email.toLowerCase(),
                                String.valueOf(userId)
                        },
                        null,
                        null,
                        null
                );

        boolean exists =
                cursor.moveToFirst();

        cursor.close();

        return exists;
    }

    // ==========================================
    // LOGIN
    // ==========================================

    public Cursor loginUser(
            String email,
            String password
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_USERS,
                new String[]{
                        "id",
                        "name",
                        "email"
                },
                "email = ? AND password = ?",
                new String[]{
                        email.toLowerCase(),
                        password
                },
                null,
                null,
                null
        );
    }

    // ==========================================
    // GET USER
    // ==========================================

    public Cursor getUser(
            int userId
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_USERS,
                new String[]{
                        "id",
                        "name",
                        "email"
                },
                "id = ?",
                new String[]{
                        String.valueOf(userId)
                },
                null,
                null,
                null
        );
    }

    // ==========================================
    // UPDATE USER NAME + EMAIL
    // ==========================================

    public boolean updateUserBasicInfo(
            int userId,
            String name,
            String email
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "email",
                email.toLowerCase()
        );

        int rows =
                db.update(
                        TABLE_USERS,
                        values,
                        "id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );

        return rows > 0;
    }

    // =========================================================
    // PROFILE METHODS
    // =========================================================

    // ==========================================
    // CREATE PROFILE
    // ==========================================

    public void createProfile(
            int userId
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "user_id",
                userId
        );

        values.put(
                "college",
                ""
        );

        values.put(
                "course",
                ""
        );

        values.put(
                "skills",
                ""
        );

        values.put(
                "last_roadmap_career",
                "Software Developer"
        );

        db.insertWithOnConflict(
                TABLE_PROFILES,
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );
    }

    // ==========================================
    // UPDATE PROFILE
    // ==========================================

    public boolean updateProfile(
            int userId,
            String college,
            String course,
            String skills
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "college",
                college
        );

        values.put(
                "course",
                course
        );

        values.put(
                "skills",
                skills
        );

        int rows =
                db.update(
                        TABLE_PROFILES,
                        values,
                        "user_id = ?",
                        new String[]{
                                String.valueOf(userId)
                        }
                );

        return rows > 0;
    }

    // ==========================================
    // GET PROFILE
    // ==========================================

    public Cursor getProfile(
            int userId
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_PROFILES,
                new String[]{
                        "college",
                        "course",
                        "skills",
                        "last_roadmap_career"
                },
                "user_id = ?",
                new String[]{
                        String.valueOf(userId)
                },
                null,
                null,
                null
        );
    }

    // ==========================================
    // SAVE LAST ROADMAP CAREER
    // ==========================================

    public void setLastRoadmapCareer(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "last_roadmap_career",
                careerName
        );

        db.update(
                TABLE_PROFILES,
                values,
                "user_id = ?",
                new String[]{
                        String.valueOf(userId)
                }
        );
    }

    // ==========================================
    // GET LAST ROADMAP CAREER
    // ==========================================

    public String getLastRoadmapCareer(
            int userId
    ) {

        Cursor cursor =
                getProfile(userId);

        String career =
                "Software Developer";

        if (cursor != null &&
                cursor.moveToFirst()) {

            int index =
                    cursor.getColumnIndex(
                            "last_roadmap_career"
                    );

            if (index >= 0) {

                String value =
                        cursor.getString(index);

                if (value != null &&
                        !value.trim().isEmpty()) {

                    career = value;
                }
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return career;
    }

    // =========================================================
    // SAVED CAREER METHODS
    // =========================================================

    // ==========================================
    // SAVE CAREER
    // ==========================================

    public boolean saveCareer(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "user_id",
                userId
        );

        values.put(
                "career_name",
                careerName
        );

        long result =
                db.insertWithOnConflict(
                        TABLE_SAVED_CAREERS,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_IGNORE
                );

        return result != -1;
    }

    // ==========================================
    // REMOVE SAVED CAREER
    // ==========================================

    public boolean removeCareer(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int rows =
                db.delete(
                        TABLE_SAVED_CAREERS,
                        "user_id = ? AND career_name = ?",
                        new String[]{
                                String.valueOf(userId),
                                careerName
                        }
                );

        return rows > 0;
    }

    // ==========================================
    // CHECK SAVED CAREER
    // ==========================================

    public boolean isCareerSaved(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_SAVED_CAREERS,
                        new String[]{"id"},
                        "user_id = ? AND career_name = ?",
                        new String[]{
                                String.valueOf(userId),
                                careerName
                        },
                        null,
                        null,
                        null
                );

        boolean saved =
                cursor.moveToFirst();

        cursor.close();

        return saved;
    }

    // ==========================================
    // GET SAVED CAREERS
    // ==========================================

    public Cursor getSavedCareers(
            int userId
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.query(
                TABLE_SAVED_CAREERS,
                new String[]{
                        "id",
                        "career_name"
                },
                "user_id = ?",
                new String[]{
                        String.valueOf(userId)
                },
                null,
                null,
                "id DESC"
        );
    }

    // ==========================================
    // CLEAR SAVED CAREERS
    // ==========================================

    public void clearSavedCareers(
            int userId
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                TABLE_SAVED_CAREERS,
                "user_id = ?",
                new String[]{
                        String.valueOf(userId)
                }
        );
    }

    // =========================================================
    // ROADMAP METHODS
    // =========================================================

    // ==========================================
    // SET STEP COMPLETED
    // ==========================================

    public void setStepCompleted(
            int userId,
            String careerName,
            int stepNumber,
            boolean completed
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "user_id",
                userId
        );

        values.put(
                "career_name",
                careerName
        );

        values.put(
                "step_number",
                stepNumber
        );

        values.put(
                "completed",
                completed ? 1 : 0
        );

        db.insertWithOnConflict(
                TABLE_ROADMAP,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );
    }

    // ==========================================
    // CHECK STEP
    // ==========================================

    public boolean isStepCompleted(
            int userId,
            String careerName,
            int stepNumber
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_ROADMAP,
                        new String[]{
                                "completed"
                        },
                        "user_id = ? AND career_name = ? AND step_number = ?",
                        new String[]{
                                String.valueOf(userId),
                                careerName,
                                String.valueOf(stepNumber)
                        },
                        null,
                        null,
                        null
                );

        boolean completed = false;

        if (cursor.moveToFirst()) {

            completed =
                    cursor.getInt(0) == 1;
        }

        cursor.close();

        return completed;
    }

    // ==========================================
    // GET COMPLETED STEPS COUNT
    // ==========================================

    public int getCompletedStepsCount(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_ROADMAP +
                                " WHERE user_id = ? " +
                                "AND career_name = ? " +
                                "AND completed = 1",
                        new String[]{
                                String.valueOf(userId),
                                careerName
                        }
                );

        int count = 0;

        if (cursor.moveToFirst()) {

            count =
                    cursor.getInt(0);
        }

        cursor.close();

        return count;
    }

    // ==========================================
    // RESET ROADMAP
    // ==========================================

    public void resetRoadmap(
            int userId,
            String careerName
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                TABLE_ROADMAP,
                "user_id = ? AND career_name = ?",
                new String[]{
                        String.valueOf(userId),
                        careerName
                }
        );
    }

    // ==========================================
    // RESET ALL USER ROADMAPS
    // ==========================================

    public void resetAllRoadmaps(
            int userId
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                TABLE_ROADMAP,
                "user_id = ?",
                new String[]{
                        String.valueOf(userId)
                }
        );
    }
}