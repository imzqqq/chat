package im.vector.lib.multipicker.entity

import android.net.Uri

interface MultiPickerBaseType {
    val displayName: String?
    val size: Long
    val mimeType: String?
    val contentUri: Uri
}
