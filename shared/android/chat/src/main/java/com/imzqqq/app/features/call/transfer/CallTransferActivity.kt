package com.imzqqq.app.features.call.transfer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.viewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.error.ErrorFormatter
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivityCallTransferBinding
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class CallTransferArgs(val callId: String) : Parcelable

private const val USER_LIST_FRAGMENT_TAG = "USER_LIST_FRAGMENT_TAG"

@AndroidEntryPoint
class CallTransferActivity : VectorBaseActivity<ActivityCallTransferBinding>() {

    @Inject lateinit var errorFormatter: ErrorFormatter

    private lateinit var sectionsPagerAdapter: CallTransferPagerAdapter

    private val callTransferViewModel: CallTransferViewModel by viewModel()

    override fun getBinding() = ActivityCallTransferBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.vectorCoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waitingView = views.waitingView.waitingView

        callTransferViewModel.observeViewEvents {
            when (it) {
                is CallTransferViewEvents.Dismiss        -> finish()
                CallTransferViewEvents.Loading           -> showWaitingView()
                is CallTransferViewEvents.FailToTransfer -> showSnackbar(getString(R.string.call_transfer_failure))
            }
        }

        sectionsPagerAdapter = CallTransferPagerAdapter(this)
        views.callTransferViewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(views.callTransferTabLayout, views.callTransferViewPager) { tab, position ->
            when (position) {
                CallTransferPagerAdapter.USER_LIST_INDEX -> tab.text = getString(R.string.call_transfer_users_tab_title)
                CallTransferPagerAdapter.DIAL_PAD_INDEX  -> tab.text = getString(R.string.call_dial_pad_title)
            }
        }.attach()
        configureToolbar(views.callTransferToolbar)
        views.callTransferToolbar.title = getString(R.string.call_transfer_title)
        setupConnectAction()
    }

    private fun setupConnectAction() {
        views.callTransferConnectAction.debouncedClicks {
            when (views.callTransferTabLayout.selectedTabPosition) {
                CallTransferPagerAdapter.USER_LIST_INDEX -> {
                    val selectedUser = sectionsPagerAdapter.userListFragment?.getCurrentState()?.getSelectedMatrixId()?.firstOrNull() ?: return@debouncedClicks
                    val action = CallTransferAction.ConnectWithUserId(views.callTransferConsultCheckBox.isChecked, selectedUser)
                    callTransferViewModel.handle(action)
                }
                CallTransferPagerAdapter.DIAL_PAD_INDEX  -> {
                    val phoneNumber = sectionsPagerAdapter.dialPadFragment?.getRawInput() ?: return@debouncedClicks
                    val action = CallTransferAction.ConnectWithPhoneNumber(views.callTransferConsultCheckBox.isChecked, phoneNumber)
                    callTransferViewModel.handle(action)
                }
            }
        }
    }

    companion object {

        fun newIntent(context: Context, callId: String): Intent {
            return Intent(context, CallTransferActivity::class.java).also {
                it.putExtra(Mavericks.KEY_ARG, CallTransferArgs(callId))
            }
        }
    }
}
