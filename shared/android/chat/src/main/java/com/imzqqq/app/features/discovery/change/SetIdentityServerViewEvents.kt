package com.imzqqq.app.features.discovery.change

import androidx.annotation.StringRes
import com.imzqqq.app.core.platform.VectorViewEvents

sealed class SetIdentityServerViewEvents : VectorViewEvents {
    data class Loading(val message: CharSequence? = null) : SetIdentityServerViewEvents()
    data class Failure(@StringRes val errorMessageId: Int, val forDefault: Boolean) : SetIdentityServerViewEvents()
    data class OtherFailure(val failure: Throwable) : SetIdentityServerViewEvents()

    data class ShowTerms(val identityServerUrl: String) : SetIdentityServerViewEvents()

    object NoTerms : SetIdentityServerViewEvents()
    object TermsAccepted : SetIdentityServerViewEvents()
}
