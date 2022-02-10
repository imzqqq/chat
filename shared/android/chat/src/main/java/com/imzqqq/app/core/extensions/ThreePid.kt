package com.imzqqq.app.core.extensions

import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.matrix.android.sdk.api.extensions.ensurePrefix
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.api.session.identity.ThreePid

fun ThreePid.getFormattedValue(): String {
    return when (this) {
        is ThreePid.Email  -> email
        is ThreePid.Msisdn -> {
            tryOrNull(message = "Unable to parse the phone number") {
                PhoneNumberUtil.getInstance().parse(msisdn.ensurePrefix("+"), null)
            }
                    ?.let {
                        PhoneNumberUtil.getInstance().format(it, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                    }
                    ?: msisdn
        }
    }
}
