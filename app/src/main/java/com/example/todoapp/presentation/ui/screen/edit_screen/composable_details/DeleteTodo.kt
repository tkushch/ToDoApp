package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun DeleteToDo(
    enabled: Boolean = true, onDelete: () -> Unit = {}
) {
    val customRippleTheme = remember {
        object : RippleTheme {
            @Composable
            override fun defaultColor(): Color = AppTheme.colorScheme.colorRed

            @Composable
            override fun rippleAlpha(): RippleAlpha = RippleAlpha(
                pressedAlpha = 0.16f,
                focusedAlpha = 0.12f,
                draggedAlpha = 0.08f,
                hoveredAlpha = 0.04f
            )
        }
    }

    CompositionLocalProvider(LocalRippleTheme provides customRippleTheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(
                onClick = { onDelete() },
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = AppTheme.colorScheme.colorRed,
                    disabledContentColor = AppTheme.colorScheme.labelDisable
                )
            ) {
                Icon(Icons.Filled.Delete, null)
                Text(text = stringResource(R.string.delete))
            }
        }
    }
}

