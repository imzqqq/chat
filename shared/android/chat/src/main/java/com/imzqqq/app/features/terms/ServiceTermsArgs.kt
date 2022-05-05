package com.imzqqq.app.features.terms

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.matrix.android.sdk.api.session.terms.TermsService

@Parcelize
data class ServiceTermsArgs(
        val type: TermsService.ServiceType,
        val baseURL: String,
        val token: String? = null
) : Parcelable
