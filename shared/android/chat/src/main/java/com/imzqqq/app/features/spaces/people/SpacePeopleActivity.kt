package com.imzqqq.app.features.spaces.people

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.commitTransaction
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.platform.GenericIdArgs
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleLoadingBinding
import com.imzqqq.app.features.spaces.share.ShareSpaceBottomSheet
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpacePeopleActivity : VectorBaseActivity<ActivitySimpleLoadingBinding>() {

    override fun getBinding() = ActivitySimpleLoadingBinding.inflate(layoutInflater)

    private lateinit var sharedActionViewModel: SpacePeopleSharedActionViewModel

    override fun initUiAndData() {
        super.initUiAndData()
        waitingView = views.waitingView.waitingView
    }

    override fun showWaitingView(text: String?) {
        hideKeyboard()
        views.waitingView.waitingStatusText.isGone = views.waitingView.waitingStatusText.text.isNullOrBlank()
        super.showWaitingView(text)
    }

    override fun hideWaitingView() {
        views.waitingView.waitingStatusText.text = null
        views.waitingView.waitingStatusText.isGone = true
        views.waitingView.waitingHorizontalProgress.progress = 0
        views.waitingView.waitingHorizontalProgress.isVisible = false
        super.hideWaitingView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = intent?.getParcelableExtra<GenericIdArgs>(Mavericks.KEY_ARG)
        if (isFirstCreation()) {
            val simpleName = SpacePeopleFragment::class.java.simpleName
            if (supportFragmentManager.findFragmentByTag(simpleName) == null) {
                supportFragmentManager.commitTransaction {
                    replace(R.id.simpleFragmentContainer,
                            SpacePeopleFragment::class.java,
                            Bundle().apply { this.putParcelable(Mavericks.KEY_ARG, args) },
                            simpleName
                    )
                }
            }
        }

        sharedActionViewModel = viewModelProvider.get(SpacePeopleSharedActionViewModel::class.java)
        sharedActionViewModel
                .stream()
                .onEach { sharedAction ->
                    when (sharedAction) {
                        SpacePeopleSharedAction.Dismiss             -> finish()
                        is SpacePeopleSharedAction.NavigateToRoom   -> navigateToRooms(sharedAction)
                        SpacePeopleSharedAction.HideModalLoading    -> hideWaitingView()
                        SpacePeopleSharedAction.ShowModalLoading    -> {
                            showWaitingView()
                        }
                        is SpacePeopleSharedAction.NavigateToInvite -> {
                            ShareSpaceBottomSheet.show(supportFragmentManager, sharedAction.spaceId)
                        }
                    }
                }.launchIn(lifecycleScope)
    }

    private fun navigateToRooms(action: SpacePeopleSharedAction.NavigateToRoom) {
        navigator.openRoom(this, action.roomId)
        finish()
    }

    companion object {
        fun newIntent(context: Context, spaceId: String): Intent {
            return Intent(context, SpacePeopleActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, GenericIdArgs(spaceId))
            }
        }
    }
}
