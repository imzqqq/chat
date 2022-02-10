package com.imzqqq.app.test.fakes

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import io.mockk.every
import io.mockk.mockk
import java.io.OutputStream

class FakeContext {

    private val contentResolver = mockk<ContentResolver>()
    val instance = mockk<Context>()

    init {
        every { instance.contentResolver } returns contentResolver
    }

    fun givenFileDescriptor(uri: Uri, mode: String, factory: () -> ParcelFileDescriptor?) {
        val fileDescriptor = factory()
        every { contentResolver.openFileDescriptor(uri, mode, null) } returns fileDescriptor
    }

    fun givenOutputStreamFor(uri: Uri): OutputStream {
        val outputStream = mockk<OutputStream>(relaxed = true)
        every { contentResolver.openOutputStream(uri) } returns outputStream
        return outputStream
    }

    fun givenMissingOutputStreamFor(uri: Uri) {
        every { contentResolver.openOutputStream(uri) } returns null
    }
}
