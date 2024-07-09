package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    selectedDate: LocalDateTime? = null,
    vm: EditTaskViewModel? = null,
    selectDate: (it: LocalDateTime?) -> Unit = {},

    ) {

    val getTime: (date: LocalDateTime) -> String = { it ->
        DateTimeFormatter.ofPattern("dd.MM.yyyy").format(it)
    }

    var date by remember { mutableStateOf(selectedDate) }

    var dateText by remember { mutableStateOf(date?.let { getTime(it) } ?: "") }

    val cal = Calendar.getInstance().apply {
        GregorianCalendar(
            get(Calendar.YEAR),
            get(Calendar.MONTH),
            get(Calendar.DAY_OF_MONTH),
            0, 0, 0
        )
    }

    val datePickerState = remember {
        DatePickerState(
            yearRange = ((cal.get(GregorianCalendar.YEAR) - 10)..
                    (cal.get(GregorianCalendar.YEAR) + 10)),
            initialDisplayedMonthMillis = selectedDate?.atZone(ZoneId.systemDefault())?.toInstant()
                ?.toEpochMilli()
                ?: cal.timeInMillis,
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = selectedDate?.atZone(ZoneId.systemDefault())?.toInstant()
                ?.toEpochMilli()
                ?: cal.timeInMillis,
            locale = Locale.getDefault()
        )
    }

    var calIsView by remember { mutableStateOf(false) }

    var checked by remember { mutableStateOf(selectedDate != null) }

    LaunchedEffect(date) {
        dateText = if (date != null) getTime(date!!) else ""
        selectDate(date)
        checked = date != null
    }

    LaunchedEffect(checked) {
        if (checked && date == null) {
            date = LocalDateTime.now()
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable { if (!checked) checked = true; calIsView = true }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                stringResource(R.string.deadline),
                color = AppTheme.colorScheme.labelPrimary,
                fontSize = 16.sp

            )
            Spacer(modifier = Modifier.height(4.dp))
            if (date != null)
                Text(
                    text = dateText,
                    color = AppTheme.colorScheme.colorBlue,
                    fontSize = 14.sp
                )
        }

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                date = null
                calIsView = it
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = AppTheme.colorScheme.colorBlue,
                uncheckedTrackColor = AppTheme.colorScheme.backSecondary,
            )
        )
    }

    HorizontalDivider(modifier = Modifier.fillMaxWidth())

    if (calIsView) {
        DatePickerDialog(
            onDismissRequest = { calIsView = false },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton({
                        date = null
                        calIsView = false
                    }) {
                        Text(
                            stringResource(R.string.calendar_cancel),
                            color = AppTheme.colorScheme.colorBlue
                        )
                    }

                    TextButton({
                        date = datePickerState.selectedDateMillis?.let {
                            LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(it),
                                ZoneId.systemDefault()
                            )
                        }
                        calIsView = false
                    }) {
                        Text(
                            stringResource(R.string.calendar_ok),
                            color = AppTheme.colorScheme.colorBlue
                        )
                    }
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.colorScheme.backSecondary,
                selectedDayContainerColor = AppTheme.colorScheme.colorBlue,
                dayContentColor = AppTheme.colorScheme.labelSecondary,
                currentYearContentColor = AppTheme.colorScheme.labelPrimary,
                todayDateBorderColor = AppTheme.colorScheme.colorBlue,
                yearContentColor = AppTheme.colorScheme.labelSecondary,
                selectedYearContainerColor = AppTheme.colorScheme.colorBlue,
                titleContentColor = AppTheme.colorScheme.labelSecondary,
                selectedDayContentColor = AppTheme.colorScheme.labelPrimary,
            )
        ) {
            DatePicker(
                state = datePickerState, showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = AppTheme.colorScheme.backSecondary,
                    selectedDayContainerColor = AppTheme.colorScheme.colorBlue,
                    dayContentColor = AppTheme.colorScheme.labelSecondary,
                    currentYearContentColor = AppTheme.colorScheme.labelPrimary,
                    todayDateBorderColor = AppTheme.colorScheme.colorBlue,
                    yearContentColor = AppTheme.colorScheme.labelSecondary,
                    selectedYearContainerColor = AppTheme.colorScheme.colorBlue,
                    titleContentColor = AppTheme.colorScheme.labelSecondary,
                    selectedDayContentColor = AppTheme.colorScheme.labelPrimary,
                    headlineContentColor = AppTheme.colorScheme.labelPrimary,
                    weekdayContentColor = AppTheme.colorScheme.labelTertiary,
                    navigationContentColor = AppTheme.colorScheme.labelSecondary
                )
            )
        }
    }
}