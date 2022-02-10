package com.imzqqq.app.features.crypto.quads

import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.test.MvRxTestRule
import com.imzqqq.app.test.fakes.FakeSession
import com.imzqqq.app.test.fakes.FakeStringProvider
import com.imzqqq.app.test.test
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.matrix.android.sdk.api.session.securestorage.IntegrityResult
import org.matrix.android.sdk.api.session.securestorage.KeyInfo
import org.matrix.android.sdk.api.session.securestorage.KeyInfoResult
import org.matrix.android.sdk.api.session.securestorage.SecretStorageKeyContent
import org.matrix.android.sdk.api.session.securestorage.SsssPassphrase

private const val IGNORED_PASSPHRASE_INTEGRITY = false
private val KEY_INFO_WITH_PASSPHRASE = KeyInfo(
        id = "id",
        content = SecretStorageKeyContent(passphrase = SsssPassphrase(null, 0, null))
)
private val KEY_INFO_WITHOUT_PASSPHRASE = KeyInfo(id = "id", content = SecretStorageKeyContent(passphrase = null))

class SharedSecureStorageViewModelTest {

    @get:Rule
    val mvrxTestRule = MvRxTestRule()

    private val stringProvider = FakeStringProvider()
    private val session = FakeSession()
    val args = SharedSecureStorageActivity.Args(keyId = null, emptyList(), "alias")

    @Test
    fun `given a key info with passphrase when initialising then step is EnterPassphrase`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITH_PASSPHRASE)
            val viewModel = createViewModel()
            viewModel
                    .test(this)
                    .assertState(aViewState(
                            hasPassphrase = true,
                            step = SharedSecureStorageViewState.Step.EnterPassphrase
                    ))
                    .finish()
        }
    }

    @Test
    fun `given a key info without passphrase when initialising then step is EnterKey`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITHOUT_PASSPHRASE)

            val viewModel = createViewModel()

            viewModel
                    .test(this)
                    .assertState(aViewState(
                            hasPassphrase = false,
                            step = SharedSecureStorageViewState.Step.EnterKey
                    ))
                    .finish()
        }
    }

    @Test
    fun `given on EnterKey step when going back then dismisses`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITHOUT_PASSPHRASE)

            val viewModel = createViewModel()
            val test = viewModel.test(this)
            viewModel.handle(SharedSecureStorageAction.Back)
            test
                    .assertEvents(SharedSecureStorageViewEvent.Dismiss)
                    .finish()
        }
    }

    @Test
    fun `given on passphrase step when using key then step is EnterKey`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITH_PASSPHRASE)
            val viewModel = createViewModel()
            val test = viewModel.test(this)

            viewModel.handle(SharedSecureStorageAction.UseKey)

            test
                    .assertState(aViewState(
                            hasPassphrase = true,
                            step = SharedSecureStorageViewState.Step.EnterKey
                    ))
                    .finish()
        }
    }

    @Test
    fun `given a key info with passphrase and on EnterKey step when going back then step is EnterPassphrase`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITH_PASSPHRASE)
            val viewModel = createViewModel()
            val test = viewModel.test(this)

            viewModel.handle(SharedSecureStorageAction.UseKey)
            viewModel.handle(SharedSecureStorageAction.Back)

            test
                    .assertState(aViewState(
                    hasPassphrase = true,
                    step = SharedSecureStorageViewState.Step.EnterPassphrase
            ))
                    .finish()
        }
    }

    @Test
    fun `given on passphrase step when going back then dismisses`() {
        runBlockingTest {
            givenKey(KEY_INFO_WITH_PASSPHRASE)
            val viewModel = createViewModel()
            val test = viewModel.test(this)

            viewModel.handle(SharedSecureStorageAction.Back)

            test
                    .assertEvents(SharedSecureStorageViewEvent.Dismiss)
                    .finish()
        }
    }

    private fun createViewModel(): SharedSecureStorageViewModel {
        return SharedSecureStorageViewModel(
                SharedSecureStorageViewState(args),
                stringProvider.instance,
                session
        )
    }

    private fun aViewState(hasPassphrase: Boolean, step: SharedSecureStorageViewState.Step) = SharedSecureStorageViewState(args).copy(
            ready = true,
            hasPassphrase = hasPassphrase,
            checkingSSSSAction = Uninitialized,
            step = step,
            activeDeviceCount = 0,
            showResetAllAction = false,
            userId = ""
    )

    private fun givenKey(keyInfo: KeyInfo) {
        givenHasAccessToSecrets()
        session.fakeSharedSecretStorageService._defaultKey = KeyInfoResult.Success(keyInfo)
    }

    private fun givenHasAccessToSecrets() {
        session.fakeSharedSecretStorageService.integrityResult = IntegrityResult.Success(passphraseBased = IGNORED_PASSPHRASE_INTEGRITY)
    }
}
