package im.vector.lib.ui.styles.debug

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import im.vector.lib.ui.styles.R
import im.vector.lib.ui.styles.databinding.ActivityDebugMaterialThemeBinding
import im.vector.lib.ui.styles.dialogs.MaterialProgressDialog

// Rendering is not the same with VectorBaseActivity
abstract class DebugMaterialThemeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val views = ActivityDebugMaterialThemeBinding.inflate(layoutInflater)
        setContentView(views.root)

        setSupportActionBar(views.debugToolbar)
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        views.debugShowSnackbar.setOnClickListener {
            Snackbar.make(views.coordinatorLayout, "Snackbar!", Snackbar.LENGTH_SHORT)
                    .setAction("Action") { }
                    .show()
        }

        views.debugShowToast.setOnClickListener {
            Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show()
        }

        views.debugShowDialog.setOnClickListener {
            showTestDialog(0)
        }

        views.debugShowDialogDestructive.setOnClickListener {
            showTestDialog(R.style.ThemeOverlay_Vector_MaterialAlertDialog_Destructive)
        }

        views.debugShowDialogNegativeDestructive.setOnClickListener {
            showTestDialog(R.style.ThemeOverlay_Vector_MaterialAlertDialog_NegativeDestructive)
        }

        views.debugShowProgressDialog.setOnClickListener {
            MaterialProgressDialog(this)
                    .show(message = "Progress Dialog\nLine 2", cancellable = true)
        }

        views.debugShowBottomSheet.setOnClickListener {
            DebugBottomSheet().show(supportFragmentManager, "TAG")
        }
    }

    private fun showTestDialog(theme: Int) {
        MaterialAlertDialogBuilder(this, theme)
                .setTitle("Dialog title")
                .setMessage("Dialog content\nLine 2")
                .setIcon(R.drawable.ic_debug_icon)
                .setPositiveButton("Positive", null)
                .setNegativeButton("Negative", null)
                .setNeutralButton("Neutral", null)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_debug, menu)
        return true
    }
}
