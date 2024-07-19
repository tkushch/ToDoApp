package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrioritySelector(
    priority: Importance = Importance.BASIC,
    selectPriority: (selected: Importance) -> Unit = {}
) {
    val priorityText = when (priority) {
        Importance.LOW -> stringResource(R.string.todo_priority_low)
        Importance.BASIC -> stringResource(R.string.todo_priority_medium)
        Importance.IMPORTANT -> stringResource(R.string.todo_priority_high)
    }

    val sheetState = rememberModalBottomSheetState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    var animateImportant by remember { mutableStateOf(false) }
    val delayTime = 200L

    val backgroundColor by animateColorAsState(
        targetValue = if (animateImportant) Color.Red.copy(alpha = 0.5f) else Color.Transparent,
        animationSpec = tween(durationMillis = delayTime.toInt()), label = ""
    )

    LaunchedEffect(animateImportant) {
        if (animateImportant) {
            delay(delayTime)
            animateImportant = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { expanded = true }
    ) {
        Text(
            text = stringResource(R.string.importance),
            color = AppTheme.colorScheme.labelPrimary,
            style = AppTheme.typographyScheme.subhead,
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
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(modifier = Modifier.fillMaxWidth())

    }

    if (expanded) {
        LaunchedEffect(Unit) {
            sheetState.show()
        }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                expanded = false
            },
            containerColor = AppTheme.colorScheme.backSecondary,

            ) {
            val modifier = Modifier.background(Color.Transparent)
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(AppTheme.colorScheme.backSecondary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectPriority(Importance.LOW)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.icon_todo_low),
                        contentDescription = null,
                        tint = AppTheme.colorScheme.colorGray,
                        modifier = modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.todo_priority_low),
                        style = AppTheme.typographyScheme.body,
                        color = AppTheme.colorScheme.labelPrimary,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectPriority(Importance.BASIC)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.icon_todo_medium),
                        contentDescription = null,
                        tint = AppTheme.colorScheme.colorGray,
                        modifier = modifier
                    )
                    Text(
                        text = stringResource(R.string.todo_priority_medium),
                        style = AppTheme.typographyScheme.body,
                        color = AppTheme.colorScheme.labelPrimary,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                        .clickable {
                            selectPriority(Importance.IMPORTANT)
                            animateImportant = true
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material3.Icon(
                        painterResource(R.drawable.icon_todo_high),
                        null,
                        tint = AppTheme.colorScheme.colorRed,
                        modifier = modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.todo_priority_high),
                        style = AppTheme.typographyScheme.body,
                        color = AppTheme.colorScheme.labelPrimary,
                    )
                }
            }
        }
    }
}