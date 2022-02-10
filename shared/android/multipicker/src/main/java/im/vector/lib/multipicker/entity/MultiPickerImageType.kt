package im.vector.lib.multipicker.entity

import android.net.Uri

data class MultiPickerImageType(
        override val displayName: String?,
        override val size: Long,
        override val mimeType: String?,
        override val contentUri: Uri,
        override val width: Int,
        override val height: Int,
        override val orientation: Int
) : MultiPickerBaseMediaType
