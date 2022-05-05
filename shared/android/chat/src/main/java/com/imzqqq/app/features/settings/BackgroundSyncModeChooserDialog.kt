package com.imzqqq.app.features.settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.imzqqq.app.R
import com.imzqqq.app.databinding.DialogBackgroundSyncModeBinding

class BackgroundSyncModeChooserDialog : DialogFragment() {

    var interactionListener: InteractionListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val initialMode = BackgroundSyncMode.fromString(arguments?.getString(ARG_INITIAL_MODE))

        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_background_sync_mode, null)
        val views = DialogBackgroundSyncModeBinding.bind(view)
        val dialog = MaterialAlertDialogBuilder(requireActivity())
                .setTitle(R.string.settings_background_fdroid_sync_mode)
                .setView(view)
                .setPositiveButton(R.string.cancel, null)
                .create()

        views.backgroundSyncModeBattery.setOnClickListener {
            interactionListener
                    ?.takeIf { initialMode != BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_BATTERY }
                    ?.onOptionSelected(BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_BATTERY)
            dialog.dismiss()
        }
        views.backgroundSyncModeReal.setOnClickListener {
            interactionListener
                    ?.takeIf { initialMode != BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_REALTIME }
                    ?.onOptionSelected(BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_FOR_REALTIME)
            dialog.dismiss()
        }
        views.backgroundSyncModeOff.setOnClickListener {
            interactionListener
                    ?.takeIf { initialMode != BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_DISABLED }
                    ?.onOptionSelected(BackgroundSyncMode.FDROID_BACKGROUND_SYNC_MODE_DISABLED)
            dialog.dismiss()
        }
        return dialog
    }

    interface InteractionListener {
        fun onOptionSelected(mode: BackgroundSyncMode)
    }

    companion object {
        private const val ARG_INITIAL_MODE = "ARG_INITIAL_MODE"

        fun newInstance(selectedMode: BackgroundSyncMode): BackgroundSyncModeChooserDialog {
            val frag = BackgroundSyncModeChooserDialog()
            val args = Bundle()
            args.putString(ARG_INITIAL_MODE, selectedMode.name)
            frag.arguments = args
            return frag
        }
    }
}
