/**
 * EditTaskFragmentCompose - класс отвечающий за визуальую часть экрана изменения задачи
 */

package com.example.todoapp.presentation.ui.screen.edit_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.presentation.ui.MainActivity
import com.example.todoapp.presentation.ui.screen.edit_screen.composable_details.EditTaskScreen
import com.example.todoapp.presentation.ui.screen.edit_screen.viewmodel.EditTaskViewModel
import com.example.todoapp.presentation.ui.screen.viewmodelfactory.TodoViewModelFactory
import com.example.todoapp.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditTaskFragmentCompose : Fragment() {

    @Inject
    lateinit var viewModelFactory: TodoViewModelFactory
    private val editTaskViewModel: EditTaskViewModel by viewModels { viewModelFactory }

    private var todoId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        (requireContext() as MainActivity).activityComponent.fragmentComponentFactory().create()
            .inject(this)

        lifecycleScope.launch {
            editTaskViewModel.networkErrors.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { errorMessage ->
                    (requireActivity() as OnNetworkErrorListener).onNetworkError(errorMessage)
                }
        }


        todoId = arguments?.getString(TASK_ID)
        editTaskViewModel.setTodoItem(todoId)

        val composeView = ComposeView(inflater.context)
        composeView.setContent {
            AppTheme {
                EditTaskScreen(vm = editTaskViewModel, goBack = {
                    parentFragmentManager.popBackStack()
                    editTaskViewModel.clearData()
                })

            }
        }

        return composeView
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