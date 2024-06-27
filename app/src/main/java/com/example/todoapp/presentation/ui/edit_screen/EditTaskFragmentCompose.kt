package com.example.todoapp.presentation.ui.edit_screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment


class EditTaskFragmentCompose : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskId = arguments?.getString(TASK_ID)

        requireActivity().setContent {
            EditTaskScreen(taskId)
        }
    }


    companion object {
        const val TASK_ID = "taskId"
        fun newInstance(taskId: String?): EditTaskFragmentCompose{
            return EditTaskFragmentCompose().apply {
                arguments = Bundle().apply {
                    putString(TASK_ID, taskId)
                }
            }
        }
    }
}


@Composable
fun EditTaskScreen(taskId: String?) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Task ID: ${taskId ?: "No ID"}")
    }
}