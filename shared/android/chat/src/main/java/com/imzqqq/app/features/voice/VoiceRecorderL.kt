package com.imzqqq.app.features.voice

import android.content.Context
import android.media.MediaRecorder
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegKitConfig
import com.arthenica.ffmpegkit.Level
import com.arthenica.ffmpegkit.ReturnCode
import com.imzqqq.app.BuildConfig
import timber.log.Timber
import java.io.File

class VoiceRecorderL(context: Context) : AbstractVoiceRecorder(context, "mp4") {
    override fun setOutputFormat(mediaRecorder: MediaRecorder) {
        // Use AAC/MP4 format here
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    override fun convertFile(recordedFile: File?): File? {
        if (BuildConfig.DEBUG) {
            FFmpegKitConfig.setLogLevel(Level.AV_LOG_INFO)
        }
        recordedFile ?: return null
        // Convert to OGG
        val targetFile = File(recordedFile.path.removeSuffix("mp4") + "ogg")
        if (targetFile.exists()) {
            targetFile.delete()
        }
        val start = System.currentTimeMillis()
        val session = FFmpegKit.execute("-i \"${recordedFile.path}\" -c:a libvorbis \"${targetFile.path}\"")
        val duration = System.currentTimeMillis() - start
        Timber.d("Convert to ogg in $duration ms. Size in bytes from ${recordedFile.length()} to ${targetFile.length()}")
        return when {
            ReturnCode.isSuccess(session.returnCode) -> {
                // SUCCESS
                targetFile
            }
            ReturnCode.isCancel(session.returnCode)  -> {
                // CANCEL
                null
            }
            else                                     -> {
                // FAILURE
                Timber.e("Command failed with state ${session.state} and rc ${session.returnCode}.${session.failStackTrace}")
                // TODO throw?
                null
            }
        }
    }
}
