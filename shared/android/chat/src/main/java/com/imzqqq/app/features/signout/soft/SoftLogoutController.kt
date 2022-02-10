package com.imzqqq.app.features.signout.soft

import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Incomplete
import com.airbnb.mvrx.Success
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.loadingItem
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.extensions.toReducedUrl
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.features.login.LoginMode
import com.imzqqq.app.features.signout.soft.epoxy.loginCenterButtonItem
import com.imzqqq.app.features.signout.soft.epoxy.loginErrorWithRetryItem
import com.imzqqq.app.features.signout.soft.epoxy.loginHeaderItem
import com.imzqqq.app.features.signout.soft.epoxy.loginPasswordFormItem
import com.imzqqq.app.features.signout.soft.epoxy.loginRedButtonItem
import com.imzqqq.app.features.signout.soft.epoxy.loginTextItem
import com.imzqqq.app.features.signout.soft.epoxy.loginTitleItem
import com.imzqqq.app.features.signout.soft.epoxy.loginTitleSmallItem
import javax.inject.Inject

class SoftLogoutController @Inject constructor(
        private val stringProvider: StringProvider,
        private val errorFormatter: ErrorFormatter
) : EpoxyController() {

    var listener: Listener? = null

    private var viewState: SoftLogoutViewState? = null

    init {
        // We are requesting a model build directly as the first build of epoxy is on the main thread.
        // It avoids to build the whole list of breadcrumbs on the main thread.
        requestModelBuild()
    }

    fun update(viewState: SoftLogoutViewState) {
        this.viewState = viewState
        requestModelBuild()
    }

    override fun buildModels() {
        val safeViewState = viewState ?: return

        buildHeader(safeViewState)
        buildForm(safeViewState)
        buildClearDataSection()
    }

    private fun buildHeader(state: SoftLogoutViewState) {
        val host = this
        loginHeaderItem {
            id("header")
        }
        loginTitleItem {
            id("title")
            text(host.stringProvider.getString(R.string.soft_logout_title))
        }
        loginTitleSmallItem {
            id("signTitle")
            text(host.stringProvider.getString(R.string.soft_logout_signin_title))
        }
        loginTextItem {
            id("signText1")
            text(host.stringProvider.getString(R.string.soft_logout_signin_notice,
                    state.homeServerUrl.toReducedUrl(),
                    state.userDisplayName,
                    state.userId))
        }
        if (state.hasUnsavedKeys) {
            loginTextItem {
                id("signText2")
                text(host.stringProvider.getString(R.string.soft_logout_signin_e2e_warning_notice))
            }
        }
    }

    private fun buildForm(state: SoftLogoutViewState) {
        val host = this
        when (state.asyncHomeServerLoginFlowRequest) {
            is Incomplete -> {
                loadingItem {
                    id("loading")
                }
            }
            is Fail       -> {
                loginErrorWithRetryItem {
                    id("errorRetry")
                    text(host.errorFormatter.toHumanReadable(state.asyncHomeServerLoginFlowRequest.error))
                    listener { host.listener?.retry() }
                }
            }
            is Success    -> {
                when (state.asyncHomeServerLoginFlowRequest.invoke()) {
                    LoginMode.Password          -> {
                        loginPasswordFormItem {
                            id("passwordForm")
                            stringProvider(host.stringProvider)
                            passwordValue(state.enteredPassword)
                            submitEnabled(state.enteredPassword.isNotEmpty())
                            onPasswordEdited { host.listener?.passwordEdited(it) }
                            errorText((state.asyncLoginAction as? Fail)?.error?.let { host.errorFormatter.toHumanReadable(it) })
                            forgetPasswordClickListener { host.listener?.forgetPasswordClicked() }
                            submitClickListener { host.listener?.submit() }
                        }
                    }
                    is LoginMode.Sso            -> {
                        loginCenterButtonItem {
                            id("sso")
                            text(host.stringProvider.getString(R.string.login_signin_sso))
                            listener { host.listener?.signinFallbackSubmit() }
                        }
                    }
                    is LoginMode.SsoAndPassword -> {
                    }
                    LoginMode.Unsupported       -> {
                        loginCenterButtonItem {
                            id("fallback")
                            text(host.stringProvider.getString(R.string.login_signin))
                            listener { host.listener?.signinFallbackSubmit() }
                        }
                    }
                    LoginMode.Unknown           -> Unit // Should not happen
                }
            }
        }
    }

    private fun buildClearDataSection() {
        val host = this
        loginTitleSmallItem {
            id("clearDataTitle")
            text(host.stringProvider.getString(R.string.soft_logout_clear_data_title))
        }
        loginTextItem {
            id("clearDataText")
            text(host.stringProvider.getString(R.string.soft_logout_clear_data_notice))
        }
        loginRedButtonItem {
            id("clearDataSubmit")
            text(host.stringProvider.getString(R.string.soft_logout_clear_data_submit))
            listener { host.listener?.clearData() }
        }
    }

    interface Listener {
        fun retry()
        fun passwordEdited(password: String)
        fun submit()
        fun signinFallbackSubmit()
        fun clearData()
        fun forgetPasswordClicked()
    }
}
