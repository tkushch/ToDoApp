package com.example.todoapp.presentation.ui.screen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel

class TodoViewModelFactory(
    private val application: Application,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(application) as T
        }

        if  (modelClass.isAssignableFrom(EditTaskViewModel::class.java))  {
            @Suppress("UNCHECKED_CAST")
            return EditTaskViewModel(application) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}