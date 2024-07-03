package com.example.todoapp.presentation.ui.palette

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.presentation.ui.theme.*

@Composable
fun ColorPalettePreview() {
    val colors = listOf(
        "SupportSeparator" to AppTheme.colorScheme.supportSeparator,
        "SupportOverlay" to AppTheme.colorScheme.supportOverlay,
        "LabelPrimary" to AppTheme.colorScheme.labelPrimary,
        "LabelSecondary" to AppTheme.colorScheme.labelSecondary,
        "LabelTertiary" to AppTheme.colorScheme.labelTertiary,
        "LabelDisable" to AppTheme.colorScheme.labelDisable,
        "ColorRed" to AppTheme.colorScheme.colorRed,
        "ColorGreen" to AppTheme.colorScheme.colorGreen,
        "ColorBlue" to AppTheme.colorScheme.colorBlue,
        "ColorLightBlue" to AppTheme.colorScheme.colorLightBlue,
        "ColorGray" to AppTheme.colorScheme.colorGray,
        "ColorGrayLight" to AppTheme.colorScheme.colorGrayLight,
        "ColorWhite" to AppTheme.colorScheme.colorWhite,
        "BackPrimary" to AppTheme.colorScheme.backPrimary,
        "BackSecondary" to AppTheme.colorScheme.backSecondary,
        "BackElevated" to AppTheme.colorScheme.backElevated,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        colors.forEach { (name, color) ->
            ColorItem(name = name, color = color)
        }
    }
}

@Composable
fun ColorItem(name: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = color)
            .padding(8.dp)
    ) {
        Text(text = name, color = if (color.luminance() > 0.5) Color.Black else Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPaletteLight() {
    AppTheme(darkTheme = false) {
        Surface {
            ColorPalettePreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewColorPaletteDark() {
    AppTheme(darkTheme = true) {
        Surface {
            ColorPalettePreview()
        }
    }
}