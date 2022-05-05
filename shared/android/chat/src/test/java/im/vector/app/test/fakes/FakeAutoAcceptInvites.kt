package com.imzqqq.app.test.fakes

import com.imzqqq.app.features.invite.AutoAcceptInvites

class FakeAutoAcceptInvites : AutoAcceptInvites {

    var _isEnabled: Boolean = false

    override val isEnabled: Boolean
        get() = _isEnabled
}
