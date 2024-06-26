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
import androidx.fragment.app.activityViewModels
import com.example.todoapp.presentation.ui.viewmodel.TasksViewModel

class MainFragment : Fragment(), TodoAdapter.OnTasksChangeListener {
    private val tasksViewModel: TasksViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val todoItemsRepository = (requireActivity().application as TodoApp).todoItemsRepository
        tasksViewModel.setTodoItemsRepository(todoItemsRepository)

        val doneCounter = view.findViewById<TextView>(R.id.doneCounter)
        doneCounter.text = tasksViewModel.numberOfCompletedTasks.value.toString()
        tasksViewModel.numberOfCompletedTasks.observe(viewLifecycleOwner) {
            doneCounter.text = it.toString()
        }

        val tasksRecyclerView = view.findViewById<RecyclerView>(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val todoAdapter = TodoAdapter(todoItemsRepository, this, requireActivity() as MainActivity)
        tasksRecyclerView.adapter = todoAdapter
        todoAdapter.submitList(tasksViewModel.currentTasks.value)
        tasksViewModel.currentTasks.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
        }

        val btnToggleVisibility: ImageButton = view.findViewById(R.id.btn_toggle_visibility)
        btnToggleVisibility.setImageResource(if (tasksViewModel.showCompletedTasks.value != false) R.drawable.eye_on else R.drawable.eye_off)
        btnToggleVisibility.setOnClickListener {
            changeVisibility(btnToggleVisibility)
        }
    }

    private fun changeVisibility(button: ImageButton) {
        tasksViewModel.toggleShowCompletedTasks()
        button.setImageResource(if (tasksViewModel.showCompletedTasks.value != false) R.drawable.eye_on else R.drawable.eye_off)
    }

    override fun onTasksChanged() {
        tasksViewModel.updateTasks()
    }
}
