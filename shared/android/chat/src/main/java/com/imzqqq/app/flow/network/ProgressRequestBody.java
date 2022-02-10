package com.imzqqq.app.flow.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public final class ProgressRequestBody extends RequestBody {
    private final InputStream content;
    private final long contentLength;
    private final UploadCallback uploadListener;
    private final MediaType mediaType;

    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface UploadCallback {
        void onProgressUpdate(int percentage);
    }

    public ProgressRequestBody(final InputStream content, long contentLength, final MediaType mediaType, final UploadCallback listener) {
        this.content = content;
        this.contentLength = contentLength;
        this.mediaType = mediaType;
        this.uploadListener = listener;
    }

    @Override
    public MediaType contentType() {
        return mediaType;
    }

    @Override
    public long contentLength() {
        return contentLength;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {

        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long uploaded = 0;

        try {
            int read;
            while ((read = content.read(buffer)) != -1) {
                uploadListener.onProgressUpdate((int)(100 * uploaded / contentLength));

                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            content.close();
        }
    }
}