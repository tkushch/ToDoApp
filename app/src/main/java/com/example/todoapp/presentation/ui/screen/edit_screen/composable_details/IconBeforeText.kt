package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.presentation.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
fun IconBeforeText(modifier: Modifier = Modifier, priority: Importance = Importance.HIGH) {
    if (priority == Importance.HIGH) {
        Icon(
            painterResource(R.drawable.icon_todo_high),
            null,
            tint = AppTheme.colorScheme.colorRed,
            modifier = modifier
        )
    }

    if (priority == Importance.MEDIUM) {
        Icon(
            painterResource(R.drawable.icon_todo_medium),
            contentDescription = null,
            tint = AppTheme.colorScheme.colorGray,
            modifier = modifier
        )
    }

    if (priority == Importance.LOW) {
        Icon(
            painterResource(R.drawable.icon_todo_low),
            contentDescription = null,
            tint = AppTheme.colorScheme.colorGray,
            modifier = modifier
        )
    }


}
