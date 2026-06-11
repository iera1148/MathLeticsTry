package com.starla.mathletics.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "MathLeticsSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";
    private static final String KEY_GRADE = "grade_level";
    private static final String KEY_TIMER_MODE = "timer_mode";
    private static final String KEY_FLASHCARD_MODE = "flashcard_mode";
    private static final String KEY_TIMER_DURATION = "timer_duration";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void createSession(int userId, String username, String role, int gradeLevel) {
        prefs.edit()
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putString(KEY_ROLE, role)
                .putInt(KEY_GRADE, gradeLevel)
                .apply();
    }

    public void clearSession() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return prefs.getInt(KEY_USER_ID, -1) != -1;
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    public int getGradeLevel() {
        return prefs.getInt(KEY_GRADE, 1);
    }

    public boolean isStudent() {
        return "student".equalsIgnoreCase(getRole());
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(getRole());
    }

    public void setQuizPreferences(boolean timerMode, boolean flashcardMode, int timerDuration) {
        prefs.edit()
                .putBoolean(KEY_TIMER_MODE, timerMode)
                .putBoolean(KEY_FLASHCARD_MODE, flashcardMode)
                .putInt(KEY_TIMER_DURATION, timerDuration)
                .apply();
    }

    public boolean isTimerMode() {
        return prefs.getBoolean(KEY_TIMER_MODE, false);
    }

    public boolean isFlashcardMode() {
        return prefs.getBoolean(KEY_FLASHCARD_MODE, false);
    }

    public int getTimerDuration() {
        return prefs.getInt(KEY_TIMER_DURATION, 60);
    }
}
