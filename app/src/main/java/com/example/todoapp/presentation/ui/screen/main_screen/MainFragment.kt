package com.example.todoapp.presentation.ui.screen.main_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.presentation.ui.screen.main_screen.adapter.TodoAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.todoapp.TodoApp
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.presentation.ui.MainActivity
import com.example.todoapp.presentation.ui.screen.model.TodoViewModelFactory
import com.example.todoapp.presentation.ui.screen.main_screen.viewmodel.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainFragment : Fragment(),
    TodoAdapter.OnTaskChangeListener {
    interface OnFabClickListener {
        fun onFloatingActionButtonClick()
    }

    private val tasksViewModel: TasksViewModel by viewModels {
        TodoViewModelFactory((requireActivity().application as TodoApp).todoItemsRepository)
    }
    private var onFabClickListener: OnFabClickListener? = null
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

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        todoAdapter = TodoAdapter(this, requireActivity() as MainActivity)
        binding.tasksRecyclerView.adapter = todoAdapter

        binding.btnToggleVisibility.setOnClickListener {
            tasksViewModel.toggleShowCompletedTasks()
        }

        binding.fab.setOnClickListener {
            onFabClickListener?.onFloatingActionButtonClick()
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
                    binding.btnToggleVisibility
                        .setImageResource(if (isVisible) R.drawable.eye_on else R.drawable.eye_off)
                }
        }

        lifecycleScope.launch {
            tasksViewModel.currentTasks.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { todoAdapter?.submitList(it) }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFabClickListener) {
            onFabClickListener = context
        } else {
            throw RuntimeException("$context must implement OnButtonClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFabClickListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


