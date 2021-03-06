package org.matrix.android.sdk.api

import okhttp3.ConnectionSpec
import org.matrix.android.sdk.api.crypto.MXCryptoConfig
import java.net.Proxy

data class MatrixConfiguration(
        val applicationFlavor: String = "Default-application-flavor",
        val cryptoConfig: MXCryptoConfig = MXCryptoConfig(),
        val integrationUIUrl: String = "https://scalar.vector.im/",
        val integrationRestUrl: String = "https://scalar.vector.im/api",
        val integrationWidgetUrls: List<String> = listOf(
                "https://scalar.vector.im/chat/integrations/v1",
                "https://scalar.vector.im/api",
                "https://scalar-staging.vector.im/chat/integrations/v1",
                "https://scalar-staging.vector.im/api",
                "https://scalar-staging.riot.im/scalar/api"
        ),
        /**
         * Optional base url to create client permalinks (eg. https://www.example.com/#/) instead of Matrix ones (to.chat.imzqqq.top links).
         * Do not forget to add the "#" which is required by the permalink parser.
         *
         * Note: this field is only used for permalinks creation, you will also have to edit the string-array `permalink_supported_hosts` in the config file
         * and add it to your manifest to handle these links in the application.
         */
        val clientPermalinkBaseUrl: String? = null,
        /**
         * Optional proxy to connect to the matrix servers.
         * You can create one using for instance Proxy(proxyType, InetSocketAddress.createUnresolved(hostname, port).
         */
        val proxy: Proxy? = null,
        /**
         * TLS versions and cipher suites limitation for unauthenticated requests
         */
        val connectionSpec: ConnectionSpec = ConnectionSpec.RESTRICTED_TLS,
        /**
         * True to advertise support for call transfers to other parties on Matrix calls.
         */
        val supportsCallTransfer: Boolean = false,
        /**
         * MatrixItemDisplayNameFallbackProvider to provide default display name for MatrixItem. By default, the id will be used
         */
        val matrixItemDisplayNameFallbackProvider: MatrixItemDisplayNameFallbackProvider? = null,
        /**
         * RoomDisplayNameFallbackProvider to provide default room display name.
         */
        val roomDisplayNameFallbackProvider: RoomDisplayNameFallbackProvider
) {

    /**
     * Can be implemented by your Application class.
     */
    interface Provider {
        fun providesMatrixConfiguration(): MatrixConfiguration
    }
}
