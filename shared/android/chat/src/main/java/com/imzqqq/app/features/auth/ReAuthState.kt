package com.imzqqq.app.features.auth

import com.airbnb.mvrx.MavericksState

data class ReAuthState(
        val title: String? = null,
        val session: String? = null,
        val flowType: String? = null,
        val ssoFallbackPageWasShown: Boolean = false,
        val lastErrorCode: String? = null,
        val resultKeyStoreAlias: String = ""
) : MavericksState {
    constructor(args: ReAuthActivity.Args) : this(
            args.title,
            args.session,
            args.flowType,
            lastErrorCode = args.lastErrorCode,
            resultKeyStoreAlias = args.resultKeyStoreAlias
    )

    constructor() : this(null, null)
}
