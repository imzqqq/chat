package com.imzqqq.app.features.matrixto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.parentFragmentViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.core.extensions.setTextOrHide
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentMatrixToUserCardBinding
import com.imzqqq.app.features.home.AvatarRenderer
import javax.inject.Inject

class MatrixToUserFragment @Inject constructor(
        private val avatarRenderer: AvatarRenderer
) : VectorBaseFragment<FragmentMatrixToUserCardBinding>() {

    private val sharedViewModel: MatrixToBottomSheetViewModel by parentFragmentViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMatrixToUserCardBinding {
        return FragmentMatrixToUserCardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        views.matrixToCardSendMessageButton.debouncedClicks {
            withState(sharedViewModel) {
                it.matrixItem.invoke()?.let { item ->
                    sharedViewModel.handle(MatrixToAction.StartChattingWithUser(item))
                }
            }
        }
    }

    override fun invalidate() = withState(sharedViewModel) { state ->
        when (val item = state.matrixItem) {
            Uninitialized -> {
                views.matrixToCardUserContentVisibility.isVisible = false
            }
            is Loading -> {
                views.matrixToCardUserContentVisibility.isVisible = false
            }
            is Success -> {
                views.matrixToCardUserContentVisibility.isVisible = true
                views.matrixToCardNameText.setTextOrHide(item.invoke().displayName)
                views.matrixToCardUserIdText.setTextOrHide(item.invoke().id)
                avatarRenderer.render(item.invoke(), views.matrixToCardAvatar)
            }
            is Fail -> {
                // TODO display some error copy?
                sharedViewModel.handle(MatrixToAction.FailedToResolveUser)
            }
        }

        when (state.startChattingState) {
            Uninitialized -> {
                views.matrixToCardButtonLoading.isVisible = false
                views.matrixToCardSendMessageButton.isVisible = false
            }
            is Success -> {
                views.matrixToCardButtonLoading.isVisible = false
                views.matrixToCardSendMessageButton.isVisible = true
            }
            is Fail -> {
                views.matrixToCardButtonLoading.isVisible = false
                views.matrixToCardSendMessageButton.isVisible = true
                // TODO display some error copy?
                sharedViewModel.handle(MatrixToAction.FailedToStartChatting)
            }
            is Loading -> {
                views.matrixToCardButtonLoading.isVisible = true
                views.matrixToCardSendMessageButton.isInvisible = true
            }
        }
    }
}
