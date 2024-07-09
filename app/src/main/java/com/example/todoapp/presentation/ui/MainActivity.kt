/**
 * Activity для отображения главного экрана приложения
 * Отвечает за отображение фрагментов, связь между ними и доступ к общим частям приложения
 */


package com.example.todoapp.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.TodoApp
import com.example.todoapp.data.network.background.schedulePeriodicWork
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.presentation.ui.screen.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.screen.main_screen.MainFragment
import com.example.todoapp.presentation.ui.screen.main_screen.adapter.TodoAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity(), TodoAdapter.OnTaskPressListener,
    MainFragment.OnFabClickListener, OnNetworkErrorListener {
    private lateinit var binding: ActivityMainBinding
    private var previousStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Available
    private var isShowingSnackbar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedulePeriodicWork(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }

        (application as TodoApp).connectivityObserver.observe().onEach {
            handleConnectivityStatus(it)
        }.launchIn(lifecycleScope)

        handleConnectivityStatus((application as TodoApp).connectivityObserver.checkCurrentStatus())
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

    override fun onFloatingActionButtonClick() {
        showEditTaskFragment()
    }

    private fun handleConnectivityStatus(status: ConnectivityObserver.Status? = null) {
        val currentStatus = status ?: previousStatus
        if (currentStatus == ConnectivityObserver.Status.Available && previousStatus != ConnectivityObserver.Status.Available) {
            Snackbar.make(
                binding.root, getString(R.string.connection_ok), Snackbar.LENGTH_LONG
            ).show()
        } else if (currentStatus != ConnectivityObserver.Status.Available) {
            Snackbar.make(
                binding.root, getString(R.string.connection_bad), Snackbar.LENGTH_LONG
            ).show()
        }
        previousStatus = currentStatus
    }

    override fun onNetworkError() {
        if (!isShowingSnackbar) {
            Snackbar.make(binding.root, getString(R.string.network_error), Snackbar.LENGTH_SHORT)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        isShowingSnackbar =
                            false
                    }
                })
                .show()
            isShowingSnackbar = true
        }

    }
}
