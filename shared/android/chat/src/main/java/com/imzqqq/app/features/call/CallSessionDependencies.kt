package com.imzqqq.app.features.call

import com.imzqqq.app.features.call.lookup.CallProtocolsChecker
import com.imzqqq.app.features.call.lookup.CallUserMapper
import com.imzqqq.app.features.session.SessionScopedProperty
import org.matrix.android.sdk.api.session.Session

interface VectorCallService {
    val protocolChecker: CallProtocolsChecker
    val userMapper: CallUserMapper
}

val Session.vectorCallService: VectorCallService by SessionScopedProperty {
    object : VectorCallService {
        override val protocolChecker = CallProtocolsChecker(it)
        override val userMapper = CallUserMapper(it, protocolChecker)
    }
}
