package im.vector.lib.ui.styles.debug

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import im.vector.lib.ui.styles.databinding.ActivityDebugButtonStylesBinding

abstract class DebugVectorButtonStylesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val views = ActivityDebugButtonStylesBinding.inflate(layoutInflater)
        setContentView(views.root)
    }
}
