package im.vector.lib.multipicker.utils

internal fun String?.isMimeTypeImage() = this?.startsWith("image/") == true
internal fun String?.isMimeTypeVideo() = this?.startsWith("video/") == true
internal fun String?.isMimeTypeAudio() = this?.startsWith("audio/") == true
