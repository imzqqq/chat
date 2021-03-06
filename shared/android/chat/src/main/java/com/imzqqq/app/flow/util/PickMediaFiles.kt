package com.imzqqq.app.flow.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickMediaFiles : ActivityResultContract<Boolean, List<Uri>>() {
    override fun createIntent(context: Context, allowMultiple: Boolean): Intent {
        return Intent(Intent.ACTION_GET_CONTENT)
            .addCategory(Intent.CATEGORY_OPENABLE)
            .setType("*/*")
            .apply {
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*", "audio/*"))
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
            }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        if (resultCode == Activity.RESULT_OK) {
            val intentData = intent?.data
            val clipData = intent?.clipData
            if (intentData != null) {
                // Single media, upload it and done.
                return listOf(intentData)
            } else if (clipData != null) {
                val result: MutableList<Uri> = mutableListOf()
                for (i in 0 until clipData.itemCount) {
                    result.add(clipData.getItemAt(i).uri)
                }
                return result
            }
        }
        return emptyList()
    }
}
