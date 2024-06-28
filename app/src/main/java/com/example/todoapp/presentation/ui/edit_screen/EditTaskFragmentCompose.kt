package com.example.todoapp.presentation.ui.edit_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todoapp.R
import com.example.todoapp.TodoApp
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EditTaskFragmentCompose : Fragment() {
    private val editTaskViewModel: EditTaskViewModel by activityViewModels()
    private lateinit var todoItemsRepository: TodoItemsRepository
    private var todoId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todoItemsRepository = (requireActivity().application as TodoApp).todoItemsRepository
        editTaskViewModel.setTodoItemsRepository(todoItemsRepository)

        todoId = arguments?.getString(TASK_ID)
        editTaskViewModel.setTodoItem(todoId)


        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    EditTaskScreen(viewModel = editTaskViewModel) { parentFragmentManager.popBackStack() }
                }
            }
        }
    }


    companion object {
        const val TASK_ID = "taskId"
        fun newInstance(taskId: String?): EditTaskFragmentCompose {
            return EditTaskFragmentCompose().apply {
                arguments = Bundle().apply {
                    putString(TASK_ID, taskId)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dark")
@Composable
fun previewScreenDark() {
    AppTheme(darkTheme = true) {
        EditTaskScreen(viewModel = EditTaskViewModel(), goBack = {})
    }
}

@Preview(showBackground = true, name = "Light")
@Composable
fun previewScreenLight() {
    AppTheme(darkTheme = false) {
        EditTaskScreen(viewModel = EditTaskViewModel(), goBack = {})
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    viewModel: EditTaskViewModel = EditTaskViewModel(),
    goBack: () -> Unit = {}
) {
    val item = viewModel.todoItem

    var text by remember {
        mutableStateOf(item?.text ?: "")
    }

    var priority by remember { mutableStateOf(item?.importance ?: Importance.MEDIUM) }

    var date by remember { mutableStateOf(item?.deadline) }

    var isError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton({ goBack() }) {
                        Icon(
                            Icons.Outlined.Close,
                            null,
                            tint = AppTheme.colorScheme.labelPrimary
                        )
                    }
                },
                actions = {
                    TextButton(
                        {
                            if (text.isNotBlank()) {
                                isError = false
                                viewModel.saveTask(
                                    item?.id,
                                    text = text,
                                    importance = priority,
                                    deadline = date,
                                )
                                goBack()
                            } else {
                                isError = true
                            }
                        },
                    ) {
                        Text(
                            stringResource(R.string.save_button_text),
                            color = AppTheme.colorScheme.colorBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colorScheme.backPrimary,
                    titleContentColor = AppTheme.colorScheme.labelPrimary,
                    scrolledContainerColor = AppTheme.colorScheme.backPrimary,
                    actionIconContentColor = AppTheme.colorScheme.colorBlue,
                ),
            )
        },


        modifier = Modifier.background(AppTheme.colorScheme.backPrimary)
    ) { pad ->
        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .background(AppTheme.colorScheme.backPrimary)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {

            item {
                EditTextField(text, { text = it; isError = false }, isError)
            }

            item {
                PrioritySelector(priority) { priority = it }
            }

            item {
                DeadlinePicker(item?.deadline) { date = it }
            }

            item {
                DeleteToDo(item != null) {
                    viewModel.deleteTask(item?.id)
                    goBack()
                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
fun EditTextField(
    text: String = "",
    onEditText: (text: String) -> Unit = {},
    isError: Boolean = false,
) {
    TextField(

        value = text,
        onValueChange = { onEditText(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.enter_the_task),
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                color = AppTheme.colorScheme.labelSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(shape = RoundedCornerShape(8.dp), elevation = 4.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = AppTheme.colorScheme.backSecondary,
            unfocusedContainerColor = AppTheme.colorScheme.backSecondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = AppTheme.colorScheme.labelPrimary,
            unfocusedTextColor = AppTheme.colorScheme.labelPrimary,
            focusedLabelColor = AppTheme.colorScheme.labelSecondary,
            unfocusedLabelColor = AppTheme.colorScheme.labelSecondary,
            cursorColor = AppTheme.colorScheme.labelSecondary,
            errorIndicatorColor = AppTheme.colorScheme.colorBlue,
            errorTextColor = AppTheme.colorScheme.colorRed,
        ),
        textStyle = AppTheme.typographyScheme.body,
        minLines = 3,
        shape = RoundedCornerShape(8.dp),
        isError = text.isBlank() && isError
    )
}


@Composable
private fun DropDownMenu(
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
            leadingIcon = { IconBeforeText(Importance.LOW) },
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
            leadingIcon = { IconBeforeText(Importance.MEDIUM) }
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
            leadingIcon = { IconBeforeText(Importance.HIGH) },
        )
    }
}

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

@Preview(showBackground  = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeadlinePicker(
    selectedDate: LocalDateTime? = null,
    selectDate: (it: LocalDateTime?) -> Unit = {}
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
                        Text("Cancel", color = AppTheme.colorScheme.colorBlue)
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
                        Text("Apply", color = AppTheme.colorScheme.colorBlue)
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

@Preview(showBackground  = true)
@Composable
fun DeleteToDo(
    enabled: Boolean = true,
    onDelete: () -> Unit = {}
) {
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

@Preview(showBackground = true)
@Composable
fun IconBeforeText(priority: Importance = Importance.HIGH, modifier: Modifier = Modifier) {
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
