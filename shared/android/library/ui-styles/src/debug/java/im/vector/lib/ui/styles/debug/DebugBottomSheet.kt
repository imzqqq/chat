package im.vector.lib.ui.styles.debug

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import im.vector.lib.ui.styles.databinding.ActivityDebugMaterialThemeBinding

class DebugBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Reuse tha Activity layout
        val binding = ActivityDebugMaterialThemeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
