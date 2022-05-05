package com.imzqqq.app.features.home.room.detail.composer.rainbow

data class RgbColor(
        val r: Int,
        val g: Int,
        val b: Int
)

fun RgbColor.toDashColor(): String {
    return listOf(r, g, b)
            .joinToString(separator = "", prefix = "#") {
                it.toString(16).padStart(2, '0')
            }
}
