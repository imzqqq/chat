package com.imzqqq.app.features.spaces.leave

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.viewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.extensions.commitTransaction
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleLoadingBinding
import com.imzqqq.app.features.spaces.SpaceBottomSheetSettingsArgs
import javax.inject.Inject

@AndroidEntryPoint
class SpaceLeaveAdvancedActivity : VectorBaseActivity<ActivitySimpleLoadingBinding>(),
        ToolbarConfigurable {

    override fun getBinding(): ActivitySimpleLoadingBinding = ActivitySimpleLoadingBinding.inflate(layoutInflater)

    val leaveViewModel: SpaceLeaveAdvancedViewModel by viewModel()

    @Inject lateinit var errorFormatter: ErrorFormatter

    override fun showWaitingView(text: String?) {
        hideKeyboard()
        views.waitingView.waitingStatusText.isGone = views.waitingView.waitingStatusText.text.isNullOrBlank()
        super.showWaitingView(text)
    }

    override fun hideWaitingView() {
        views.waitingView.waitingStatusText.setTextOrHide(null)
        views.waitingView.waitingHorizontalProgress.progress = 0
        views.waitingView.waitingHorizontalProgress.isVisible = false
        super.hideWaitingView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = intent?.getParcelableExtra<SpaceBottomSheetSettingsArgs>(Mavericks.KEY_ARG)

        if (isFirstCreation()) {
            val simpleName = SpaceLeaveAdvancedFragment::class.java.simpleName
            if (supportFragmentManager.findFragmentByTag(simpleName) == null) {
                supportFragmentManager.commitTransaction {
                    replace(
                            R.id.simpleFragmentContainer,
                            SpaceLeaveAdvancedFragment::class.java,
                            Bundle().apply { this.putParcelable(Mavericks.KEY_ARG, args) },
                            simpleName
                    )
                }
            }
        }
    }

    override fun initUiAndData() {
        super.initUiAndData()
        waitingView = views.waitingView.waitingView
        leaveViewModel.onEach { state ->
            when (state.leaveState) {
                is Loading -> {
                    showWaitingView()
                }
                is Success -> {
                    setResult(RESULT_OK)
                    finish()
                }
                is Fail    -> {
                    hideWaitingView()
                    MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.dialog_title_error)
                            .setMessage(errorFormatter.toHumanReadable(state.leaveState.error))
                            .setPositiveButton(R.string.ok) { _, _ ->
                                leaveViewModel.handle(SpaceLeaveAdvanceViewAction.ClearError)
                            }
                            .show()
                }
                else       -> {
                    hideWaitingView()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context, spaceId: String): Intent {
            return Intent(context, SpaceLeaveAdvancedActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, SpaceBottomSheetSettingsArgs(spaceId))
            }
        }
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }
}
