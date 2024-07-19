package com.example.todoapp.presentation.ui.theme.control

import android.content.Context

class PreferenceManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    fun saveTheme(theme: String) {
        sharedPreferences.edit().putString("theme_key", theme).apply()
    }

    fun getTheme(): String {
        return sharedPreferences.getString("theme_key", ThemeHelper.SYSTEM_MODE)
            ?: ThemeHelper.SYSTEM_MODE
    }
}