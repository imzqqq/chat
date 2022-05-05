package com.imzqqq.app.features.voice

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

abstract class AbstractVoiceRecorder(
        private val context: Context,
        private val filenameExt: String
) : VoiceRecorder {
    private val outputDirectory: File by lazy {
        File(context.cacheDir, "voice_records").also {
            it.mkdirs()
        }
    }

    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null

    abstract fun setOutputFormat(mediaRecorder: MediaRecorder)
    abstract fun convertFile(recordedFile: File?): File?

    private fun init() {
        createMediaRecorder().let {
            it.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setOutputFormat(it)
            it.setAudioEncodingBitRate(24000)
            it.setAudioSamplingRate(48000)
            mediaRecorder = it
        }
    }

    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
    }

    override fun startRecord() {
        init()
        outputFile = File(outputDirectory, "Voice message.$filenameExt")

        val mr = mediaRecorder ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mr.setOutputFile(outputFile)
        } else {
            mr.setOutputFile(FileOutputStream(outputFile).fd)
        }
        mr.prepare()
        mr.start()
    }

    override fun stopRecord() {
        // Can throw when the record is less than 1 second.
        mediaRecorder?.let {
            it.stop()
            it.reset()
            it.release()
        }
        mediaRecorder = null
    }

    override fun cancelRecord() {
        stopRecord()

        outputFile?.delete()
        outputFile = null
    }

    override fun getMaxAmplitude(): Int {
        return mediaRecorder?.maxAmplitude ?: 0
    }

    override fun getCurrentRecord(): File? {
        return outputFile
    }

    override fun getVoiceMessageFile(): File? {
        return convertFile(outputFile)
    }
}
