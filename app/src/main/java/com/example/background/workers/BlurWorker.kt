package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import com.example.background.R
import timber.log.Timber

class BlurWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){

    override fun doWork(): Result {
        val resourceUri = inputData.getString(KEY_IMAGE_URI)
        makeStatusNotification("Blurring image", applicationContext)
        // ADD THIS TO SLOW DOWN THE WORKER
//        sleep()
        // ^^^^
        return  try {

            val resolver = applicationContext.contentResolver
            val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)))

            val blurredBitmap = blurBitmap(applicationContext = applicationContext, bitmap = picture)
            val outputUri = writeBitmapToFile(applicationContext = applicationContext, bitmap = blurredBitmap)
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            makeStatusNotification("Image blurred!", applicationContext)
            Result.success(outputData)
        } catch (e: Exception) {
            Timber.e(e, "Error applying blur")
            Result.failure()
        }
    }

}