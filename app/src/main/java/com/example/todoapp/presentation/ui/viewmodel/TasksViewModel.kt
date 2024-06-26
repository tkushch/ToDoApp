package com.example.todoapp.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksViewModel() : ViewModel() {
    private val _showCompletedTasks = MutableLiveData<Boolean>(true)
    val showCompletedTasks: LiveData<Boolean> get() = _showCompletedTasks


    fun toggleShowCompletedTasks(): Boolean {
        _showCompletedTasks.value = !(_showCompletedTasks.value ?: true)
        return _showCompletedTasks.value ?: true
    }


}