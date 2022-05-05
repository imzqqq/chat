package com.imzqqq.app.features.crypto.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.ColorProvider
import com.imzqqq.app.databinding.FragmentBootstrapReauthBinding
import javax.inject.Inject

class BootstrapReAuthFragment @Inject constructor(
        private val colorProvider: ColorProvider
) : VectorBaseFragment<FragmentBootstrapReauthBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentBootstrapReauthBinding {
        return FragmentBootstrapReauthBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: BootstrapSharedViewModel by parentFragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.bootstrapRetryButton.debouncedClicks { submit() }
        views.bootstrapCancelButton.debouncedClicks { cancel() }
    }

    private fun submit() = withState(sharedViewModel) { state ->
        if (state.step !is BootstrapStep.AccountReAuth) {
            return@withState
        }
        if (state.passphrase != null) {
            sharedViewModel.handle(BootstrapActions.DoInitialize(state.passphrase))
        } else {
            sharedViewModel.handle(BootstrapActions.DoInitializeGeneratedKey)
        }
    }

    private fun cancel() = withState(sharedViewModel) { state ->
        if (state.step !is BootstrapStep.AccountReAuth) {
            return@withState
        }
        sharedViewModel.handle(BootstrapActions.GoBack)
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        if (state.step !is BootstrapStep.AccountReAuth) {
            return@withState
        }
        val failure = state.step.failure
        views.reAuthFailureText.setTextOrHide(failure)
        if (failure == null) {
            views.waitingProgress.isVisible = true
            views.bootstrapCancelButton.isVisible = false
            views.bootstrapRetryButton.isVisible = false
        } else {
            views.waitingProgress.isVisible = false
            views.bootstrapCancelButton.isVisible = true
            views.bootstrapRetryButton.isVisible = true
        }
    }
}
