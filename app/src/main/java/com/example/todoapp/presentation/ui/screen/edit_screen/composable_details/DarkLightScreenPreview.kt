package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.presentation.ui.theme.AppTheme

@Preview(showBackground = true, name = "Dark")
@Composable
fun PreviewScreenDark() {
    AppTheme(darkTheme = true) {
        EditTaskScreen()
    }

}

@Preview(showBackground = true, name = "Light")
@Composable
fun PreviewScreenLight() {
    AppTheme(darkTheme = false) {
        EditTaskScreen()
    }
}