package im.vector.lib.multipicker.entity

import android.net.Uri

data class MultiPickerFileType(
        override val displayName: String?,
        override val size: Long,
        override val mimeType: String?,
        override val contentUri: Uri
) : MultiPickerBaseType
