package com.example.todoapp.presentation.ui.screen.main_screen.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import java.time.LocalDateTime
import com.example.todoapp.databinding.TodoItemBinding

class TodoAdapter(
    private val onTasksChangedListener: OnTaskChangeListener,
    private val onTaskPressListener: OnTaskPressListener
) :

    ListAdapter<TodoItem, TodoAdapter.TodoViewHolder>(TodoDiffCallback()) {

    interface OnTaskChangeListener {
        fun onTaskChanged(id: String)
    }

    interface OnTaskPressListener {
        fun onTaskPressed(id: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onTasksChangedListener, onTaskPressListener)
    }


    class TodoViewHolder(binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv = binding.mainText
        private val checkBox = binding.checkboxCompleted
        private val tvImportance = binding.importanceFlag
        private val editClickArea = binding.editTaskClickArea

        fun bind(
            todoItem: TodoItem,
            onTasksChangedListener: OnTaskChangeListener,
            onTaskPressListener: OnTaskPressListener,
        ) {
            tv.text = todoItem.text
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = todoItem.done
            updateTextDecoration(todoItem.done, todoItem.deadline)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                updateTextDecoration(isChecked, todoItem.deadline)
                onTasksChangedListener.onTaskChanged(todoItem.id)
            }

            editClickArea.setOnClickListener {
                onTaskPressListener.onTaskPressed(todoItem.id)
            }

            setUpImportance(todoItem.importance)
        }

        private fun setUpImportance(importance: Importance) {
            val context = itemView.context
            when (importance) {
                Importance.LOW -> {
                    tvImportance.text = context.getString(R.string.empty_string)
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.MEDIUM -> {
                    tvImportance.text = context.getString(R.string.medium_importance_symbol)
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.HIGH -> {
                    tvImportance.text = context.getString(R.string.high_importance_symbol)
                    tvImportance.setTextColor(Color.RED)
                }
            }
        }


        private fun updateTextDecoration(done: Boolean, deadline: LocalDateTime?) {
            if (done) {
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tv.setTextColor(Color.GRAY)
            } else if (deadline != null && deadline <= LocalDateTime.now()) {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tv.setTextColor(Color.RED)
            } else {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tv.setTextColor(Color.BLACK)
            }
        }
    }


    class TodoDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
            return oldItem == newItem
        }
    }

}
