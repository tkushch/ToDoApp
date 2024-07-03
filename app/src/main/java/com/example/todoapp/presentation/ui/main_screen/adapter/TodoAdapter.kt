package com.example.todoapp.presentation.ui.main_screen.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.Importance
import com.example.todoapp.data.model.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import java.time.LocalDateTime


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
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onTasksChangedListener, onTaskPressListener)
    }


    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTodoText: TextView = itemView.findViewById(R.id.mainText)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxCompleted)
        private val tvImportance: TextView = itemView.findViewById(R.id.importanceFlag)
        private val editClickArea: LinearLayout = itemView.findViewById(R.id.editTaskClickArea)

        fun bind(
            todoItem: TodoItem,
            onTasksChangedListener: OnTaskChangeListener,
            onTaskPressListener: OnTaskPressListener,
        ) {
            tvTodoText.text = todoItem.text
            checkBox.setOnCheckedChangeListener(null)
            checkBox.isChecked = todoItem.done
            updateTextDecoration(tvTodoText, todoItem.done, todoItem.deadline)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                updateTextDecoration(tvTodoText, isChecked, todoItem.deadline)
                onTasksChangedListener.onTaskChanged(todoItem.id)
            }

            editClickArea.setOnClickListener {
                onTaskPressListener.onTaskPressed(todoItem.id)
            }

            when (todoItem.importance) {
                Importance.LOW -> {
                    tvImportance.text = ""
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.MEDIUM -> {
                    tvImportance.text = "!"
                    tvImportance.setTextColor(Color.BLACK)
                }

                Importance.HIGH -> {
                    tvImportance.text = "!!"
                    tvImportance.setTextColor(Color.RED)
                }
            }
        }


        private fun updateTextDecoration(tv: TextView, done: Boolean, deadline: LocalDateTime?) {
            if (done) {
                tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tv.paintFlags = tv.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            if (!done && deadline != null && deadline <= LocalDateTime.now()) {
                tvTodoText.setTextColor(Color.RED)
            } else {
                tvTodoText.setTextColor(Color.BLACK)
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
