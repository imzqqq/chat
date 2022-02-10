package com.imzqqq.app.core.glide

import com.bumptech.glide.load.Option
import org.matrix.android.sdk.internal.crypto.attachments.ElementToDecrypt

const val ElementToDecryptOptionKey = "com.imzqqq.app.core.glide.ElementToDecrypt"

val ELEMENT_TO_DECRYPT = Option.memory(
        ElementToDecryptOptionKey, ElementToDecrypt("", "", ""))
