package im.vector.lib.multipicker.entity

import android.net.Uri

data class MultiPickerAudioType(
        override val displayName: String?,
        override val size: Long,
        override val mimeType: String?,
        override val contentUri: Uri,
        val duration: Long,
        var waveform: List<Int>? = null
) : MultiPickerBaseType
