/**
 * UpdateWorker - отвечает за выполнение периодиеческих фоновых задач
 */

package com.example.todoapp.data.network.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.todoapp.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class UpdateWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            request()
            Result.success()
        } catch (e: Exception) {
            Log.e("UpdateWorker", "Network request failed", e)
            Result.retry()
        }
    }

    private suspend fun request()  {
        Log.d("UpdateWorker", "Updating todo items")
        val api = RetrofitClient.api
        val listDto = api.getTodoList()
        Log.d("UpdateWorker", "Status: ${listDto.status}")
        Log.d("UpdateWorker", "Revision: ${listDto.revision}")
        for (element in listDto.list) {
            Log.d("UpdateWorker", element.toString())
        }
    }
}

fun schedulePeriodicWork(context: Context) {
    val periodicWorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(8, TimeUnit.HOURS)
        .build()

    WorkManager.getInstance(context).enqueue(periodicWorkRequest)
}