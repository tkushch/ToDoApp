package com.example.todoapp.data.network.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todoapp.TodoApp
import com.example.todoapp.data.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * UpdateWorker - отвечает за выполнение периодиеческих фоновых задач
 */
class UpdateWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repo: TodoItemsRepository

    init {
        (context.applicationContext as TodoApp).appComponent.inject(this)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            repo.update()
            Result.success()
        } catch (e: Exception) {
            Log.e("UpdateWorker", "Network request failed", e)
            Result.retry()
        }
    }

}

fun schedulePeriodicWork(context: Context) {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(8, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(periodicWorkRequest)
}

