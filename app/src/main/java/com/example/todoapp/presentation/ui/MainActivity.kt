package com.example.todoapp.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.R
import com.example.todoapp.TodoApp
import com.example.todoapp.data.network.background.schedulePeriodicWork
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.di.ActivityComponent
import com.example.todoapp.presentation.ui.screen.divkit_screen.Div2ViewFactory
import com.example.todoapp.presentation.ui.screen.divkit_screen.SampleDivActionHandler
import com.example.todoapp.presentation.ui.screen.divkit_screen.replaceColorsInJson
import com.example.todoapp.presentation.ui.screen.edit_screen.EditTaskFragmentCompose
import com.example.todoapp.presentation.ui.screen.main_screen.MainFragment
import com.example.todoapp.presentation.ui.screen.main_screen.adapter.TodoAdapter
import com.example.todoapp.presentation.ui.screen.setting_screen.SettingsFragment
import com.google.android.material.snackbar.Snackbar
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.picasso.PicassoDivImageLoader
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.json.JSONObject
import javax.inject.Inject

interface OnChangeFragmentListener {
    fun onFloatingActionButtonClick()
    fun onSettingsButtonClick()
    fun onInfoButtonClick()
}

/**
 * Activity для отображения главного экрана приложения
 * Отвечает за отображение фрагментов, связь между ними и доступ к общим частям приложения
 */
class MainActivity : AppCompatActivity(), TodoAdapter.OnTaskPressListener,
    OnChangeFragmentListener, OnNetworkErrorListener {

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var activityComponent: ActivityComponent

    private lateinit var binding: ActivityMainBinding
    private var previousStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Available
    private var isShowingSnackbar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schedulePeriodicWork(applicationContext)
        (application as TodoApp).appComponent.activityComponentFactory().create(this).also {
            activityComponent = it
            it.inject(this)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }

        connectivityObserver.observe().onEach {
            handleConnectivityStatus(it)
        }.launchIn(lifecycleScope)

        handleConnectivityStatus(connectivityObserver.checkCurrentStatus())
    }

    private fun showEditTaskFragment(taskId: String? = null) {
        val fragment = EditTaskFragmentCompose.newInstance(taskId)
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            replace(R.id.fragment_container, fragment)
            addToBackStack(null)
        }
    }

    private fun showSettingsFragment() {
        val fragment = SettingsFragment()
        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
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

    override fun onSettingsButtonClick() {
        showSettingsFragment()
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

    override fun onNetworkError(message: String) {
        if (!isShowingSnackbar) {
            Snackbar.make(binding.root, getString(R.string.network_error), Snackbar.LENGTH_SHORT)
                .addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        isShowingSnackbar = false
                    }
                }).show()
            isShowingSnackbar = true
        }

    }

    private fun createDivConfiguration(): DivConfiguration {
        return DivConfiguration.Builder(PicassoDivImageLoader(this))
            .actionHandler(SampleDivActionHandler(this))
            .visualErrorsEnabled(true)
            .build()
    }

    override fun onInfoButtonClick() {
        val jsonString = assets.open("about_screen.json").bufferedReader().use { it.readText() }
        val updatedJsonString = replaceColorsInJson(this, jsonString)
        val divJson = JSONObject(updatedJsonString)
        val templatesJson = divJson.optJSONObject("templates")
        val cardJson = divJson.getJSONObject("card")

        val divContext = Div2Context(
            baseContext = this,
            configuration = createDivConfiguration(),
            lifecycleOwner = this
        )

        val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
        val divContainer = binding.divContainer
        divContainer.removeAllViews()
        divContainer.addView(divView)
        divContainer.visibility = View.VISIBLE
        binding.fragmentContainer.visibility = View.GONE
    }

    fun onBackButtonClick() {
        binding.divContainer.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
    }
}
