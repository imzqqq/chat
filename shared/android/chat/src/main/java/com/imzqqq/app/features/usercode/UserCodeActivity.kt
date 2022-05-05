package com.imzqqq.app.features.usercode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.commitTransaction
import com.imzqqq.app.core.extensions.exhaustive
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.core.utils.onPermissionDeniedSnackbar
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.matrixto.MatrixToBottomSheet
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@AndroidEntryPoint
class UserCodeActivity : VectorBaseActivity<ActivitySimpleBinding>(),
        MatrixToBottomSheet.InteractionListener {

    val sharedViewModel: UserCodeSharedViewModel by viewModel()

    @Parcelize
    data class Args(
            val userId: String
    ) : Parcelable

    override fun getBinding() = ActivitySimpleBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (f is MatrixToBottomSheet) {
                f.interactionListener = this@UserCodeActivity
            }
            super.onFragmentResumed(fm, f)
        }

        override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
            if (f is MatrixToBottomSheet) {
                f.interactionListener = null
            }
            super.onFragmentPaused(fm, f)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, false)

        if (isFirstCreation()) {
            // should be there early for shared element transition
            showFragment(ShowUserCodeFragment::class, Bundle.EMPTY)
        }

        sharedViewModel.onEach(UserCodeState::mode) { mode ->
            when (mode) {
                UserCodeState.Mode.SHOW      -> showFragment(ShowUserCodeFragment::class, Bundle.EMPTY)
                UserCodeState.Mode.SCAN      -> showFragment(ScanUserCodeFragment::class, Bundle.EMPTY)
                is UserCodeState.Mode.RESULT -> {
                    showFragment(ShowUserCodeFragment::class, Bundle.EMPTY)
                    MatrixToBottomSheet.withLink(mode.rawLink).show(supportFragmentManager, "MatrixToBottomSheet")
                }
            }
        }

        sharedViewModel.observeViewEvents {
            when (it) {
                UserCodeShareViewEvents.Dismiss                       -> ActivityCompat.finishAfterTransition(this)
                UserCodeShareViewEvents.ShowWaitingScreen             -> views.simpleActivityWaitingView.isVisible = true
                UserCodeShareViewEvents.HideWaitingScreen             -> views.simpleActivityWaitingView.isVisible = false
                is UserCodeShareViewEvents.ToastMessage               -> Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                is UserCodeShareViewEvents.NavigateToRoom             -> navigator.openRoom(this, it.roomId)
                is UserCodeShareViewEvents.CameraPermissionNotGranted -> {
                    if (it.deniedPermanently) {
                        onPermissionDeniedSnackbar(R.string.permissions_denied_qr_code)
                    }
                }
                else                                                  -> {
                }
            }
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        super.onDestroy()
    }

    private fun showFragment(fragmentClass: KClass<out Fragment>, bundle: Bundle) {
        if (supportFragmentManager.findFragmentByTag(fragmentClass.simpleName) == null) {
            supportFragmentManager.commitTransaction {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                replace(R.id.simpleFragmentContainer,
                        fragmentClass.java,
                        bundle,
                        fragmentClass.simpleName
                )
            }
        }
    }

    override fun mxToBottomSheetNavigateToRoom(roomId: String) {
        navigator.openRoom(this, roomId)
    }

    override fun mxToBottomSheetSwitchToSpace(spaceId: String) {}

    override fun onBackPressed() = withState(sharedViewModel) {
        when (it.mode) {
            UserCodeState.Mode.SHOW -> super.onBackPressed()
            is UserCodeState.Mode.RESULT,
            UserCodeState.Mode.SCAN -> sharedViewModel.handle(UserCodeActions.SwitchMode(UserCodeState.Mode.SHOW))
        }.exhaustive
    }

    companion object {
        fun newIntent(context: Context, userId: String): Intent {
            return Intent(context, UserCodeActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, Args(userId))
            }
        }
    }
}
