package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.presentation.ui.theme.AppTheme


@Composable
fun DropDownMenu(
    expanded: Boolean = false,
    onDismiss: () -> Unit = {},
    onSelect: (selected: Importance) -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        modifier = Modifier.background(AppTheme.colorScheme.backElevated)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.todo_priority_low),
                    color = AppTheme.colorScheme.labelPrimary
                )
            },
            onClick = { onSelect(Importance.LOW) },
            modifier = Modifier.background(AppTheme.colorScheme.backElevated),
            leadingIcon = { IconBeforeText(priority = Importance.LOW) },
        )
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.todo_priority_medium),
                    color = AppTheme.colorScheme.labelPrimary
                )
            },
            onClick = { onSelect(Importance.MEDIUM) },
            modifier = Modifier.background(AppTheme.colorScheme.backElevated),
            leadingIcon = { IconBeforeText(priority = Importance.MEDIUM) }
        )
        DropdownMenuItem(
            text = {
                Text(
                    stringResource(R.string.todo_priority_high),
                    color = AppTheme.colorScheme.colorRed
                )
            },
            onClick = { onSelect(Importance.HIGH) },
            modifier = Modifier.background(AppTheme.colorScheme.backElevated),
            leadingIcon = { IconBeforeText(priority = Importance.HIGH) },
        )
    }
}