/**
 * TodoViewModelFactory - класс-фабрика для создания ViewModel для разных экранов с дополнительными аргументами
 */

package com.example.todoapp.presentation.ui.screen.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel
import dagger.internal.Provider
import javax.inject.Inject

class TodoViewModelFactory @Inject constructor(
    private val todoItemsRepository: TodoItemsRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(
                todoItemsRepository
            ) as T
        }

        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditTaskViewModel(
                todoItemsRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

//class TodoViewModelFactory @Inject constructor(
//    private val tasksViewModel: Provider<TasksViewModel>,
//    private val editTaskViewModel: Provider<EditTaskViewModel>
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        return when {
//            modelClass.isAssignableFrom(TasksViewModel::class.java) -> tasksViewModel.get() as T
//            modelClass.isAssignableFrom(EditTaskViewModel::class.java) -> editTaskViewModel.get() as T
//            else -> throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
//}


//class TodoViewModelFactory @Inject constructor(
//    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
//            modelClass.isAssignableFrom(it.key)
//        }?.value ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
//
//        return try {
//            @Suppress("UNCHECKED_CAST")
//            creator.get() as T
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }
//}