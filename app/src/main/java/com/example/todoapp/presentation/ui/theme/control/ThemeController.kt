package com.example.todoapp.presentation.ui.theme.control

import javax.inject.Inject


class ThemeController @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    fun getTheme(): String {
        return preferenceManager.getTheme()
    }

    fun setLightTheme() {
        setTheme(ThemeHelper.LIGHT_MODE)
    }

    fun setDarkTheme() {
        setTheme(ThemeHelper.DARK_MODE)
    }

    fun setSystemTheme() {
        setTheme(ThemeHelper.SYSTEM_MODE)
    }

    fun setTheme(theme: String) {
        ThemeHelper.applyTheme(theme)
        preferenceManager.saveTheme(theme)
    }
}