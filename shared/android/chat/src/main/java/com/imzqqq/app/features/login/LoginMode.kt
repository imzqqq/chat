package com.imzqqq.app.features.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.auth.data.SsoIdentityProvider

sealed class LoginMode : Parcelable
/** because persist state */ {
    @Parcelize object Unknown : LoginMode()
    @Parcelize object Password : LoginMode()
    @Parcelize data class Sso(val ssoIdentityProviders: List<SsoIdentityProvider>?) : LoginMode()
    @Parcelize data class SsoAndPassword(val ssoIdentityProviders: List<SsoIdentityProvider>?) : LoginMode()
    @Parcelize object Unsupported : LoginMode()
}

fun LoginMode.ssoIdentityProviders(): List<SsoIdentityProvider>? {
    return when (this) {
        is LoginMode.Sso            -> ssoIdentityProviders
        is LoginMode.SsoAndPassword -> ssoIdentityProviders
        else                        -> null
    }
}

fun LoginMode.hasSso(): Boolean {
    return when (this) {
        is LoginMode.Sso            -> true
        is LoginMode.SsoAndPassword -> true
        else                        -> false
    }
}
