package com.imzqqq.app.flow.components.notifications

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.Provides
import javax.inject.Inject

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
    private val notificationsFetcher: NotificationFetcher
) : Worker(context, params) {

    override fun doWork(): Result {
        notificationsFetcher.fetchAndShow()
        return Result.success()
    }
}


class NotificationWorkerFactory @Inject constructor(
    private val notificationsFetcher: NotificationFetcher
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        if (workerClassName == NotificationWorker::class.java.name) {
            return NotificationWorker(appContext, workerParameters, notificationsFetcher)
        }
        return null
    }
}
