package com.imzqqq.app.core.dialogs

import android.app.Activity
import android.text.Editable
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.SimpleTextWatcher
import com.imzqqq.app.databinding.DialogExportE2eKeysBinding

class ExportKeysDialog {

    fun show(activity: Activity, exportKeyDialogListener: ExportKeyDialogListener) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_export_e2e_keys, null)
        val views = DialogExportE2eKeysBinding.bind(dialogLayout)
        val builder = MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.encryption_export_room_keys)
                .setView(dialogLayout)

        val textWatcher = object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                when {
                    views.exportDialogEt.text.isNullOrEmpty()                                   -> {
                        views.exportDialogSubmit.isEnabled = false
                        views.exportDialogTilConfirm.error = null
                    }
                    views.exportDialogEt.text.toString() == views.exportDialogEtConfirm.text.toString() -> {
                        views.exportDialogSubmit.isEnabled = true
                        views.exportDialogTilConfirm.error = null
                    }
                    else                                                                       -> {
                        views.exportDialogSubmit.isEnabled = false
                        views.exportDialogTilConfirm.error = activity.getString(R.string.passphrase_passphrase_does_not_match)
                    }
                }
            }
        }

        views.exportDialogEt.addTextChangedListener(textWatcher)
        views.exportDialogEtConfirm.addTextChangedListener(textWatcher)

        val exportDialog = builder.show()

        views.exportDialogSubmit.setOnClickListener {
            exportKeyDialogListener.onPassphrase(views.exportDialogEt.text.toString())

            exportDialog.dismiss()
        }
    }

    interface ExportKeyDialogListener {
        fun onPassphrase(passphrase: String)
    }
}
