package com.imzqqq.app.features.call

enum class CameraType {
    FRONT,
    BACK
}

data class CameraProxy(
        val name: String,
        val type: CameraType
)

sealed class CaptureFormat(val width: Int, val height: Int, val fps: Int) {
    object HD : CaptureFormat(1280, 720, 30)
    object SD : CaptureFormat(640, 480, 30)
}
