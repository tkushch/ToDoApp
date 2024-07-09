package com.example.todoapp.presentation.ui.screen.edit_screen.composable_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    vm: EditTaskViewModel? = null,
    goBack: () -> Unit = {}
) {

    val item = vm?.todoItem

    var text by remember {
        mutableStateOf(vm?.text ?: item?.text ?: "")
    }

    var priority by remember {
        mutableStateOf(
            vm?.importance ?: item?.importance ?: Importance.BASIC
        )
    }

    var date by remember {
        mutableStateOf(
            vm?.deadline ?: if (vm?.text == null) item?.deadline else null
        )
    }

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
                                vm?.saveTask(
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
                EditTextField(text, { text = it; vm?.text = it; isError = false }, isError)
            }

            item {
                PrioritySelector(priority) { priority = it; vm?.importance = it }
            }

            item {
                DeadlinePicker(date, vm) { date = it; vm?.deadline = it }
            }

            item {
                DeleteToDo(item != null) {
                    vm?.deleteTask(item?.id)
                    goBack()
                }
            }

        }

    }
}