package org.matrix.android.sdk.api

/**
 * This object define some global constants regarding the Matrix specification
 */
object MatrixConstants {
    /**
     * Max length for an alias. Room aliases MUST NOT exceed 255 bytes (including the # sigil and the domain).
     * See [maxAliasLocalPartLength]
     * Ref. https://chat.docs.imzqqq.top/spec/appendices#room-aliases
     */
    const val ALIAS_MAX_LENGTH = 255

    fun maxAliasLocalPartLength(domain: String): Int {
        return (ALIAS_MAX_LENGTH - 1 /* # sigil */ - 1 /* ':' */ - domain.length)
                .coerceAtLeast(0)
    }
}
