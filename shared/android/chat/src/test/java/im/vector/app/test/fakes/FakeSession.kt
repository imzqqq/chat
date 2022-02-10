package com.imzqqq.app.test.fakes

import com.imzqqq.app.test.testCoroutineDispatchers
import io.mockk.mockk
import org.matrix.android.sdk.api.session.Session

class FakeSession(
        val fakeCryptoService: FakeCryptoService = FakeCryptoService(),
        val fakeSharedSecretStorageService: FakeSharedSecretStorageService = FakeSharedSecretStorageService()
) : Session by mockk(relaxed = true) {

    override fun cryptoService() = fakeCryptoService
    override val sharedSecretStorageService = fakeSharedSecretStorageService
    override val coroutineDispatchers = testCoroutineDispatchers
}
