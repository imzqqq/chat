package com.imzqqq.app.features.voice

import android.content.Context
import android.os.Build
import javax.inject.Inject

class VoiceRecorderProvider @Inject constructor(
        private val context: Context
) {
    fun provideVoiceRecorder(): VoiceRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            VoiceRecorderQ(context)
        } else {
            VoiceRecorderL(context)
        }
    }
}
