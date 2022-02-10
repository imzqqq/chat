package com.imzqqq.app.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.imzqqq.app.BuildConfig
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.observeK
import com.imzqqq.app.core.extensions.replaceChildFragment
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.startSharePlainTextIntent
import com.imzqqq.app.databinding.FragmentHomeDrawerBinding
import com.imzqqq.app.features.login.PromptSimplifiedModeActivity
import com.imzqqq.app.features.settings.VectorPreferences
import com.imzqqq.app.features.settings.VectorSettingsActivity
import com.imzqqq.app.features.spaces.SpaceListFragment
import com.imzqqq.app.features.usercode.UserCodeActivity
import com.imzqqq.app.features.workers.signout.SignOutUiWorker
import org.matrix.android.sdk.api.session.Session
import org.matrix.android.sdk.api.util.toMatrixItem
import javax.inject.Inject

class HomeDrawerFragment @Inject constructor(
        private val session: Session,
        private val vectorPreferences: VectorPreferences,
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentHomeDrawerBinding>() {

    private lateinit var sharedActionViewModel: HomeSharedActionViewModel

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeDrawerBinding {
        return FragmentHomeDrawerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedActionViewModel = activityViewModelProvider.get(HomeSharedActionViewModel::class.java)

        if (savedInstanceState == null) {
            replaceChildFragment(R.id.homeDrawerGroupListContainer, SpaceListFragment::class.java)
        }
        session.getUserLive(session.myUserId).observeK(viewLifecycleOwner) { optionalUser ->
            val user = optionalUser?.getOrNull()
            if (user != null) {
                avatarRenderer.render(user.toMatrixItem(), views.homeDrawerHeaderAvatarView)
                views.homeDrawerUsernameView.text = user.displayName
                views.homeDrawerUserIdView.text = user.userId
            }
        }
        // Profile
        views.homeDrawerHeader.debouncedClicks {
            sharedActionViewModel.post(HomeActivitySharedAction.CloseDrawer)
            navigator.openSettings(requireActivity(), directAccess = VectorSettingsActivity.EXTRA_DIRECT_ACCESS_GENERAL)
        }
        // Settings
        views.homeDrawerHeaderSettingsView.debouncedClicks {
            sharedActionViewModel.post(HomeActivitySharedAction.CloseDrawer)
            navigator.openSettings(requireActivity())
        }
        // Sign out
        views.homeDrawerHeaderSignoutView.debouncedClicks {
            sharedActionViewModel.post(HomeActivitySharedAction.CloseDrawer)
            SignOutUiWorker(requireActivity()).perform()
        }

        views.homeDrawerQRCodeButton.debouncedClicks {
            UserCodeActivity.newIntent(requireContext(), sharedActionViewModel.session.myUserId).let {
                val options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                requireActivity(),
                                views.homeDrawerHeaderAvatarView,
                                ViewCompat.getTransitionName(views.homeDrawerHeaderAvatarView) ?: ""
                        )
                startActivity(it, options.toBundle())
            }
        }

        views.homeDrawerInviteFriendButton.debouncedClicks {
            session.permalinkService().createPermalink(sharedActionViewModel.session.myUserId)?.let { permalink ->
                val text = getString(R.string.invite_friends_text, permalink)

                startSharePlainTextIntent(
                        fragment = this,
                        activityResultLauncher = null,
                        chooserTitle = getString(R.string.invite_friends),
                        text = text,
                        extraTitle = getString(R.string.invite_friends_rich_title)
                )
            }
        }

        // Debug menu
        views.homeDrawerHeaderDebugView.isVisible = BuildConfig.DEBUG && vectorPreferences.developerMode()
        views.homeDrawerHeaderDebugView.debouncedClicks {
            sharedActionViewModel.post(HomeActivitySharedAction.CloseDrawer)
            navigator.openDebug(requireActivity())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SC: settings migration
        vectorPreferences.scPreferenceUpdate()
        // SC-Easy mode prompt
        PromptSimplifiedModeActivity.showIfRequired(requireContext(), vectorPreferences)
    }
}
