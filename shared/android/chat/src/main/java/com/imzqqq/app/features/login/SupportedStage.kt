package com.imzqqq.app.features.login

import org.matrix.android.sdk.api.auth.registration.Stage

/**
 * Stage.Other is not supported, as well as any other new stages added to the SDK before it is added to the list below
 */
fun Stage.isSupported(): Boolean {
    return this is Stage.ReCaptcha ||
            this is Stage.Dummy ||
            this is Stage.Msisdn ||
            this is Stage.Terms ||
            this is Stage.Email
}
