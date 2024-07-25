package com.example.todoapp.presentation.ui.screen.main_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.network.connectivity.ConnectivityObserver
import com.example.todoapp.data.network.connectivity.OnNetworkErrorListener
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.presentation.ui.MainActivity
import com.example.todoapp.presentation.ui.OnChangeFragmentListener
import com.example.todoapp.presentation.ui.screen.main_screen.adapter.TodoAdapter
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel
import com.example.todoapp.presentation.ui.screen.viewmodelfactory.TodoViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainFragment - класс отвечающий за визуальную часть основного экрана
 */
class MainFragment : Fragment(), TodoAdapter.OnTaskChangeListener {

    @Inject
    lateinit var viewModelFactory: TodoViewModelFactory

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    private val tasksViewModel: TasksViewModel by viewModels { viewModelFactory }
    private var onChangeFragmentListener: OnChangeFragmentListener? = null
    private var todoAdapter: TodoAdapter? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireContext() as MainActivity).activityComponent.fragmentComponentFactory().create()
            .inject(this)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter = TodoAdapter(this, requireActivity() as MainActivity)
        binding.tasksRecyclerView.adapter = todoAdapter

        binding.btnToggleVisibility.setOnClickListener {
            tasksViewModel.toggleShowCompletedTasks()
        }

        binding.refreshButton.setOnClickListener {
            tasksViewModel.refreshTasks()
            Toast.makeText(requireContext(), getString(R.string.refresh), Toast.LENGTH_SHORT).show()
        }

        binding.settingsButton.setOnClickListener {
            (requireActivity() as MainActivity).onSettingsButtonClick()
        }

        binding.aboutButton.setOnClickListener {
            (requireActivity() as MainActivity).onInfoButtonClick()
        }

        binding.fab.setOnClickListener {
            onChangeFragmentListener?.onFloatingActionButtonClick()
        }

        subscribeToObservables()
    }

    override fun onTaskChanged(id: String) {
        tasksViewModel.changeTaskStatus(id)
    }

    private fun subscribeToObservables() {
        lifecycleScope.launch {
            tasksViewModel.numberOfCompletedTasks.flowWithLifecycle(
                lifecycle, Lifecycle.State.STARTED
            ).collect { count ->
                binding.doneCounter.text = count.toString()
            }
        }

        lifecycleScope.launch {
            tasksViewModel.showCompletedTasks.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { isVisible ->
                    binding.btnToggleVisibility.setImageResource(if (isVisible) R.drawable.eye_on else R.drawable.eye_off)
                }
        }

        lifecycleScope.launch {
            tasksViewModel.currentTasks.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { todoAdapter?.submitList(it) }
        }

        lifecycleScope.launch {
            tasksViewModel.networkErrors.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { errorMessage ->
                    (requireActivity() as OnNetworkErrorListener).onNetworkError(errorMessage)
                }
        }

        connectivityObserver.observe().onEach {
            if (it == ConnectivityObserver.Status.Available) {
                tasksViewModel.refreshTasks()
            }
        }.launchIn(lifecycleScope)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnChangeFragmentListener) {
            onChangeFragmentListener = context
        } else {
            throw RuntimeException("$context must implement OnChangeFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onChangeFragmentListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        tasksViewModel.refreshTasks()
    }
}


