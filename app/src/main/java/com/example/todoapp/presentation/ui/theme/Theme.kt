package com.example.todoapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.todoapp.presentation.ui.theme.local.AppColorScheme
import com.example.todoapp.presentation.ui.theme.local.AppTypography
import com.example.todoapp.presentation.ui.theme.local.LocalAppColorScheme
import com.example.todoapp.presentation.ui.theme.local.LocalAppTypography

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme
    val rippleIndication = rememberRipple()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.backPrimary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides typography,
        LocalIndication provides rippleIndication,
        content = content
    )
}

object AppTheme {
    val colorScheme: AppColorScheme
        @Composable get() = LocalAppColorScheme.current
    val typographyScheme: AppTypography
        @Composable get() = LocalAppTypography.current
}

private val darkColorScheme = AppColorScheme(
    supportSeparator = SupportSeparatorDark,
    supportOverlay = SupportOverlayDark,
    labelPrimary = LabelPrimaryDark,
    labelSecondary = LabelSecondaryDark,
    labelTertiary = LabelTertiaryDark,
    labelDisable = LabelDisableDark,
    colorRed = ColorRedDark,
    colorGreen = ColorGreenDark,
    colorBlue = ColorBlueDark,
    colorLightBlue = ColorLightBlueDark,
    colorGray = ColorGrayDark,
    colorGrayLight = ColorGrayLightDark,
    colorWhite = ColorWhiteDark,
    backPrimary = BackPrimaryDark,
    backSecondary = BackSecondaryDark,
    backElevated = BackElevatedDark,
)

private val lightColorScheme = AppColorScheme(
    supportSeparator = SupportSeparator,
    supportOverlay = SupportOverlay,
    labelPrimary = LabelPrimary,
    labelSecondary = LabelSecondary,
    labelTertiary = LabelTertiary,
    labelDisable = LabelDisable,
    colorRed = ColorRed,
    colorGreen = ColorGreen,
    colorBlue = ColorBlue,
    colorLightBlue = ColorLightBlue,
    colorGray = ColorGray,
    colorGrayLight = ColorGrayLight,
    colorWhite = ColorWhite,
    backPrimary = BackPrimary,
    backSecondary = BackSecondary,
    backElevated = BackElevated,
)


