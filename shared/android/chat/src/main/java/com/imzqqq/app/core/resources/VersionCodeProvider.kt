package com.imzqqq.app.core.resources

import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import javax.inject.Inject

class VersionCodeProvider @Inject constructor(private val context: Context) {

    /**
     * Returns the version code, read from the Manifest. It is not the same than BuildConfig.VERSION_CODE due to versionCodeOverride
     */
    @NonNull
    fun getVersionCode(): Long {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }
    }
}
