package com.imzqqq.app.core.intent

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import im.vector.lib.multipicker.utils.getColumnIndexOrNull

fun getFilenameFromUri(context: Context?, uri: Uri): String? {
    if (context != null && uri.scheme == "content") {
        context.contentResolver.query(uri, null, null, null, null)
                ?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        return cursor.getColumnIndexOrNull(OpenableColumns.DISPLAY_NAME)
                                ?.let { cursor.getString(it) }
                    }
                }
    }
    return uri.path?.substringAfterLast('/')
}
