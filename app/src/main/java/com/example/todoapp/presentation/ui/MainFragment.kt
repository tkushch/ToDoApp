package com.example.todoapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.adapter.TodoAdapter
import com.example.todoapp.TodoApp
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import androidx.fragment.app.activityViewModels
import com.example.todoapp.presentation.ui.viewmodel.TasksViewModel

class MainFragment : Fragment(), TodoAdapter.OnTasksChangeListener {
    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoItemsRepository: TodoItemsRepository

    private var currentTasks: List<TodoItem> = listOf()
    private var showCompletedTasks: Boolean = true
    private lateinit var doneCounter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCompletedTasks = tasksViewModel.showCompletedTasks.value ?: true
        todoItemsRepository = (requireActivity().application as TodoApp).todoItemsRepository
        fillCurrentTasks()
        doneCounter = view.findViewById(R.id.doneCounter)
        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter = TodoAdapter(todoItemsRepository, this, requireActivity() as MainActivity)
        tasksRecyclerView.adapter = todoAdapter
        todoAdapter.submitList(currentTasks)

        val btnToggleVisibility: ImageButton = view.findViewById(R.id.btn_toggle_visibility)
        btnToggleVisibility.setImageResource(if (showCompletedTasks) R.drawable.eye_on else R.drawable.eye_off)
        btnToggleVisibility.setOnClickListener {
            toggleVisibility(btnToggleVisibility)
        }

        updateDoneCounter()
    }


    private fun toggleVisibility(button: ImageButton) {
        showCompletedTasks = tasksViewModel.toggleShowCompletedTasks()
        button.setImageResource(if (showCompletedTasks) R.drawable.eye_on else R.drawable.eye_off)
        updateTasks()
    }

    private fun fillCurrentTasks() {
        currentTasks = if (showCompletedTasks) {
            todoItemsRepository.getTodoItems()
        } else {
            todoItemsRepository.getUncompletedTodoItems()
        }
    }

    private fun updateTasks() {
        currentTasks = if (showCompletedTasks) {
            todoItemsRepository.getTodoItems()
        } else {
            todoItemsRepository.getUncompletedTodoItems()
        }
        todoAdapter.submitList(currentTasks)
    }

    override fun onTasksChanged() {
        updateTasks()
        updateDoneCounter()
    }

    private fun updateDoneCounter() {
        doneCounter.text = todoItemsRepository.getTodoItems().count { it.done }.toString()
    }
}