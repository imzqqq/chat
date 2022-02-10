package com.imzqqq.app.features.widgets.webview

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.PermissionRequest
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R

object WebviewPermissionUtils {

    @SuppressLint("NewApi")
    fun promptForPermissions(@StringRes title: Int, request: PermissionRequest, context: Context) {
        val allowedPermissions = request.resources.map {
            it to false
        }.toMutableList()
        MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMultiChoiceItems(
                        request.resources.map { webPermissionToHumanReadable(it, context) }.toTypedArray(), null
                ) { _, which, isChecked ->
                    allowedPermissions[which] = allowedPermissions[which].first to isChecked
                }
                .setPositiveButton(R.string.room_widget_resource_grant_permission) { _, _ ->
                    request.grant(allowedPermissions.mapNotNull { perm ->
                        perm.first.takeIf { perm.second }
                    }.toTypedArray())
                }
                .setNegativeButton(R.string.room_widget_resource_decline_permission) { _, _ ->
                    request.deny()
                }
                .show()
    }

    private fun webPermissionToHumanReadable(permission: String, context: Context): String {
        return when (permission) {
            PermissionRequest.RESOURCE_AUDIO_CAPTURE      -> context.getString(R.string.room_widget_webview_access_microphone)
            PermissionRequest.RESOURCE_VIDEO_CAPTURE      -> context.getString(R.string.room_widget_webview_access_camera)
            PermissionRequest.RESOURCE_PROTECTED_MEDIA_ID -> context.getString(R.string.room_widget_webview_read_protected_media)
            else                                          -> permission
        }
    }
}
