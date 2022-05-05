package com.imzqqq.app.features.spaces

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.viewModel
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.commitTransaction
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.matrixto.MatrixToBottomSheet
import com.imzqqq.app.features.navigation.Navigator
import com.imzqqq.app.features.spaces.explore.SpaceDirectoryArgs
import com.imzqqq.app.features.spaces.explore.SpaceDirectoryFragment
import com.imzqqq.app.features.spaces.explore.SpaceDirectoryViewEvents
import com.imzqqq.app.features.spaces.explore.SpaceDirectoryViewModel

@AndroidEntryPoint
class SpaceExploreActivity : VectorBaseActivity<ActivitySimpleBinding>(), MatrixToBottomSheet.InteractionListener {

    override fun getBinding(): ActivitySimpleBinding = ActivitySimpleBinding.inflate(layoutInflater)

    override fun getTitleRes(): Int = R.string.space_explore_activity_title

    val sharedViewModel: SpaceDirectoryViewModel by viewModel()

    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            if (f is MatrixToBottomSheet) {
                f.interactionListener = this@SpaceExploreActivity
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
            val simpleName = SpaceDirectoryFragment::class.java.simpleName
            val args = intent?.getParcelableExtra<SpaceDirectoryArgs>(Mavericks.KEY_ARG)
            if (supportFragmentManager.findFragmentByTag(simpleName) == null) {
                supportFragmentManager.commitTransaction {
                    replace(R.id.simpleFragmentContainer,
                            SpaceDirectoryFragment::class.java,
                            Bundle().apply { this.putParcelable(Mavericks.KEY_ARG, args) },
                            simpleName
                    )
                }
            }
        }

        sharedViewModel.observeViewEvents {
            when (it) {
                SpaceDirectoryViewEvents.Dismiss                      -> {
                    finish()
                }
                is SpaceDirectoryViewEvents.NavigateToRoom            -> {
                    navigator.openRoom(this, it.roomId)
                }
                is SpaceDirectoryViewEvents.NavigateToMxToBottomSheet -> {
                    MatrixToBottomSheet.withLink(it.link).show(supportFragmentManager, "ShowChild")
                }
            }
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
        super.onDestroy()
    }

    companion object {
        fun newIntent(context: Context, spaceId: String): Intent {
            return Intent(context, SpaceExploreActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, SpaceDirectoryArgs(spaceId))
            }
        }
    }

    override fun mxToBottomSheetNavigateToRoom(roomId: String) {
        navigator.openRoom(this, roomId)
    }

    override fun mxToBottomSheetSwitchToSpace(spaceId: String) {
        navigator.switchToSpace(this, spaceId, Navigator.PostSwitchSpaceAction.None)
    }
}
