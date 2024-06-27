package com.example.todoapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.commit
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.main_screen.adapter.TodoAdapter
import com.example.todoapp.TodoApp
import com.example.todoapp.data.repository.TodoItemsRepository
//import com.example.todoapp.presentation.ui.edit_screen.EditTaskFragment
import com.example.todoapp.presentation.ui.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.main_screen.MainFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TodoAdapter.OnTaskEditListener {
    private lateinit var fab: FloatingActionButton
    private lateinit var todoItemsRepository: TodoItemsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoItemsRepository = (application as TodoApp).todoItemsRepository

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            showEditTaskFragment()
        }

    }

    private fun showEditTaskFragment(taskId: String? = null) {
        fab.isEnabled = false
        fab.isInvisible = true

        val fragment = EditTaskFragmentCompose.newInstance(taskId)

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                fab.isEnabled = true
                fab.isInvisible = false
            }
        }

        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }

    }

    override fun onTaskEdit(id: String) {
        showEditTaskFragment(id)
    }
}
