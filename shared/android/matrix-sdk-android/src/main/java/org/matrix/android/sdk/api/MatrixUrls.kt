package org.matrix.android.sdk.api

/**
 * This class contains pattern to match Matrix Url, aka mxc urls
 */
object MatrixUrls {
    /**
     * "mxc" scheme, including "://". So "mxc://"
     */
    const val MATRIX_CONTENT_URI_SCHEME = "mxc://"

    /**
     * Return true if the String starts with "mxc://"
     */
    fun String.isMxcUrl() = startsWith(MATRIX_CONTENT_URI_SCHEME)

    /**
     * Remove the "mxc://" prefix. No op if the String is not a Mxc URL
     */
    fun String.removeMxcPrefix() = removePrefix(MATRIX_CONTENT_URI_SCHEME)
}
