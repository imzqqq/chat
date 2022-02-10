package com.imzqqq.app.features.voice

import java.io.File

interface VoiceRecorder {
    /**
     * Start the recording
     */
    fun startRecord()

    /**
     * Stop the recording
     */
    fun stopRecord()

    /**
     * Remove the file
     */
    fun cancelRecord()

    fun getMaxAmplitude(): Int

    /**
     * Not guaranteed to be a ogg file
     */
    fun getCurrentRecord(): File?

    /**
     * Guaranteed to be a ogg file
     */
    fun getVoiceMessageFile(): File?
}
