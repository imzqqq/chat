package com.imzqqq.app.features.usercode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.utils.PERMISSIONS_FOR_TAKING_PHOTO
import com.imzqqq.app.core.utils.checkPermissions
import com.imzqqq.app.core.utils.registerForPermissionsResult
import com.imzqqq.app.core.utils.startSharePlainTextIntent
import com.imzqqq.app.databinding.FragmentUserCodeShowBinding
import com.imzqqq.app.features.home.AvatarRenderer
import javax.inject.Inject

class ShowUserCodeFragment @Inject constructor(
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentUserCodeShowBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentUserCodeShowBinding {
        return FragmentUserCodeShowBinding.inflate(inflater, container, false)
    }

    val sharedViewModel: UserCodeSharedViewModel by activityViewModel()

    private val openCameraActivityResultLauncher = registerForPermissionsResult { allGranted, deniedPermanently ->
        if (allGranted) {
            doOpenQRCodeScanner()
        } else {
            sharedViewModel.handle(UserCodeActions.CameraPermissionNotGranted(deniedPermanently))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.showUserCodeClose.debouncedClicks {
            sharedViewModel.handle(UserCodeActions.DismissAction)
        }
        views.showUserCodeScanButton.debouncedClicks {
            if (checkPermissions(PERMISSIONS_FOR_TAKING_PHOTO, requireActivity(), openCameraActivityResultLauncher)) {
                doOpenQRCodeScanner()
            }
        }
        views.showUserCodeShareButton.debouncedClicks {
            sharedViewModel.handle(UserCodeActions.ShareByText)
        }

        sharedViewModel.observeViewEvents {
            if (it is UserCodeShareViewEvents.SharePlainText) {
                startSharePlainTextIntent(
                        fragment = this,
                        activityResultLauncher = null,
                        chooserTitle = it.title,
                        text = it.text,
                        extraTitle = it.richPlainText
                )
            }
        }
    }

    private fun doOpenQRCodeScanner() {
        sharedViewModel.handle(UserCodeActions.SwitchMode(UserCodeState.Mode.SCAN))
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        state.matrixItem?.let { avatarRenderer.render(it, views.showUserCodeAvatar) }
        state.shareLink?.let { views.showUserCodeQRImage.setData(it) }
        views.showUserCodeCardNameText.setTextOrHide(state.matrixItem?.displayName)
        views.showUserCodeCardUserIdText.setTextOrHide(state.matrixItem?.id)
    }
}
