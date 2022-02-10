package com.imzqqq.app.core.dialogs

import android.app.Activity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.databinding.DialogDeviceVerifyBinding
import org.matrix.android.sdk.api.extensions.getFingerprintHumanReadable
import org.matrix.android.sdk.internal.crypto.model.CryptoDeviceInfo

object ManuallyVerifyDialog {

    fun show(activity: Activity, cryptoDeviceInfo: CryptoDeviceInfo, onVerified: (() -> Unit)) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_device_verify, null)
        val views = DialogDeviceVerifyBinding.bind(dialogLayout)
        val builder = MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.cross_signing_verify_by_text)
                .setView(dialogLayout)
                .setPositiveButton(R.string.encryption_information_verify) { _, _ ->
                    onVerified()
                }
                .setNegativeButton(R.string.cancel, null)

        views.encryptedDeviceInfoDeviceName.text = cryptoDeviceInfo.displayName()
        views.encryptedDeviceInfoDeviceId.text = cryptoDeviceInfo.deviceId
        views.encryptedDeviceInfoDeviceKey.text = cryptoDeviceInfo.getFingerprintHumanReadable()

        builder.show()
    }
}
