package com.imzqqq.app.features.login.terms

import org.matrix.android.sdk.internal.auth.registration.LocalizedFlowDataLoginTerms

data class LocalizedFlowDataLoginTermsChecked(val localizedFlowDataLoginTerms: LocalizedFlowDataLoginTerms,
                                              var checked: Boolean = false)
