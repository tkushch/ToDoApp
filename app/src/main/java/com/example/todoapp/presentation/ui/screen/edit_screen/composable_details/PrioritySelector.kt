package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.presentation.ui.theme.AppTheme


@Preview(showBackground = true)
@Composable
fun PrioritySelector(
    priority: Importance = Importance.MEDIUM,
    selectPriority: (selected: Importance) -> Unit = {}
) {

    val priorityText = when (priority) {
        Importance.LOW -> stringResource(R.string.todo_priority_low)
        Importance.MEDIUM -> stringResource(R.string.todo_priority_medium)
        Importance.HIGH -> stringResource(R.string.todo_priority_high)
    }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = true }
    ) {
        Text(
            text = stringResource(R.string.importance),
            color = AppTheme.colorScheme.labelPrimary,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = priorityText,
                style = AppTheme.typographyScheme.subhead,
                color = AppTheme.colorScheme.labelPrimary,
                fontSize = 14.sp
            )
        }
        DropDownMenu(expanded, { expanded = false }) { selectPriority(it); expanded = false }
    }


    HorizontalDivider(modifier = Modifier.fillMaxWidth())

}