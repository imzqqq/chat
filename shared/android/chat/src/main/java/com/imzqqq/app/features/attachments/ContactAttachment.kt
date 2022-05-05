package com.imzqqq.app.features.attachments

/**
 * Data class holding values of a picked contact
 * Can be send as a text message waiting for the protocol to handle contact.
 */
data class ContactAttachment(
        val displayName: String,
        val photoUri: String?,
        val phones: List<String> = emptyList(),
        val emails: List<String> = emptyList()
) {

    fun toHumanReadable(): String {
        return buildString {
            append(displayName)
            phones.concatIn(this)
            emails.concatIn(this)
        }
    }

    private fun List<String>.concatIn(stringBuilder: StringBuilder) {
        if (isNotEmpty()) {
            stringBuilder.append("\n")
            for (i in 0 until size - 1) {
                val value = get(i)
                stringBuilder.append(value).append("\n")
            }
            stringBuilder.append(last())
        }
    }
}
