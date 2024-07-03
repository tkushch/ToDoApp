package com.example.todoapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.screen.main_screen.adapter.TodoAdapter
import com.example.todoapp.presentation.ui.screen.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.screen.main_screen.MainFragment

class MainActivity : AppCompatActivity(), TodoAdapter.OnTaskPressListener,
    MainFragment.OnFabClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }
    }

    private fun showEditTaskFragment(taskId: String? = null) {
        val fragment = EditTaskFragmentCompose.newInstance(taskId)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
    }

    override fun onTaskPressed(id: String) {
        showEditTaskFragment(id)
    }

    override fun onFloatingActionButtonClick(){
        showEditTaskFragment()
    }

}
