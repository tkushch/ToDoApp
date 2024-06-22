package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainFragment : Fragment(), TodoAdapter.OnTasksChangeListener {

    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoItemsRepository: TodoItemsRepository
    private val currentTasks: MutableList<TodoItem> = mutableListOf()
    private var showCompletedTasks: Boolean = true
    private lateinit var doneCounter: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showCompletedTasks = savedInstanceState?.getBoolean("showCompletedTasks", true) ?: true


        todoItemsRepository = (requireActivity().application as TodoApp).todoItemsRepository
        updateTasks()

        doneCounter = view.findViewById(R.id.doneCounter)
        tasksRecyclerView = view.findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter = TodoAdapter(currentTasks, this, requireActivity() as MainActivity)
        tasksRecyclerView.adapter = todoAdapter

        val btnToggleVisibility: ImageButton = view.findViewById(R.id.btn_toggle_visibility)
        btnToggleVisibility.setImageResource(if (showCompletedTasks) R.drawable.eye_on else R.drawable.eye_off)
        btnToggleVisibility.setOnClickListener {
            toggleVisibility(btnToggleVisibility)
        }

        updateDoneCounter()
    }


    private fun toggleVisibility(button: ImageButton) {
        showCompletedTasks = !showCompletedTasks
        button.setImageResource(if (showCompletedTasks) R.drawable.eye_on else R.drawable.eye_off)
        updateTasks()
        todoAdapter.updateTasks(currentTasks)
        tasksRecyclerView.post {
            todoAdapter.notifyDataSetChanged()
        }
    }

    private fun updateTasks() {
        currentTasks.clear()
        currentTasks.addAll(todoItemsRepository.getTodoItems().filter {
            if (showCompletedTasks) true else !it.done
        })

    }

    override fun onTasksChanged() {
        updateTasks()
        todoAdapter.updateTasks(currentTasks)
        tasksRecyclerView.post {
            todoAdapter.notifyDataSetChanged()
        }
        updateDoneCounter()

    }

    private fun updateDoneCounter() {
        doneCounter.text = todoItemsRepository.getTodoItems().count { it.done }.toString()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("showCompletedTasks", showCompletedTasks)
    }

}