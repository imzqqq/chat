package com.imzqqq.app.flow.util;

import android.content.ContentResolver;
import android.net.Uri;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtils {

    private static final int DEFAULT_BLOCKSIZE = 16384;

    public static void closeQuietly(@Nullable Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // intentionally unhandled
        }
    }

    public static boolean copyToFile(ContentResolver contentResolver, Uri uri, File file) {
        InputStream from;
        FileOutputStream to;
        try {
            from = contentResolver.openInputStream(uri);
            to = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        if (from == null) {
            return false;
        }
        byte[] chunk = new byte[DEFAULT_BLOCKSIZE];
        try {
            while (true) {
                int bytes = from.read(chunk, 0, chunk.length);
                if (bytes < 0) {
                    break;
                }
                to.write(chunk, 0, bytes);
            }
        } catch (IOException e) {
            return false;
        }
        closeQuietly(from);
        closeQuietly(to);
        return true;
    }
}
