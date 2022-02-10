package im.vector.lib.ui.styles.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import im.vector.lib.ui.styles.R
import im.vector.lib.ui.styles.databinding.DialogProgressMaterialBinding

class MaterialProgressDialog(val context: Context) {
    fun show(message: CharSequence, cancellable: Boolean = false): AlertDialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_progress_material, null)
        val views = DialogProgressMaterialBinding.bind(view)
        views.message.text = message

        return MaterialAlertDialogBuilder(context)
                .setCancelable(cancellable)
                .setView(view)
                .show()
    }
}
