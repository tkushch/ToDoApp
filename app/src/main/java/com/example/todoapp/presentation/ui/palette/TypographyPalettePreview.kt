package com.example.todoapp.presentation.ui.palette

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.presentation.ui.theme.*

@Composable
fun TypographyPreview() {
    val typographies = listOf(
        "Large Title" to typography.largeTitle,
        "Title" to typography.title,
        "Button" to typography.button,
        "Body" to typography.body,
        "Subhead" to typography.subhead
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        typographies.forEach { (name, textStyle) ->
            TypographyItem(name = name, textStyle = textStyle)
        }
    }
}

@Composable
fun TypographyItem(name: String, textStyle: androidx.compose.ui.text.TextStyle) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(8.dp)
    ) {
        Text(text = name, style = textStyle)
    }
}

@Preview
@Composable
fun PreviewTypography() {
    AppTheme {
        Surface {
            TypographyPreview()
        }
    }
}