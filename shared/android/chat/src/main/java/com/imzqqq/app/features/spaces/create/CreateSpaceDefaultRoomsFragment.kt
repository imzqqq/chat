package com.imzqqq.app.features.spaces.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.extensions.hideKeyboard
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentSpaceCreateGenericEpoxyFormBinding
import javax.inject.Inject

class CreateSpaceDefaultRoomsFragment @Inject constructor(
        private val epoxyController: SpaceDefaultRoomEpoxyController
) : VectorBaseFragment<FragmentSpaceCreateGenericEpoxyFormBinding>(),
        SpaceDefaultRoomEpoxyController.Listener,
        OnBackPressed {

    private val sharedViewModel: CreateSpaceViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceCreateGenericEpoxyFormBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.recyclerView.configureWith(epoxyController)
        epoxyController.listener = this

        sharedViewModel.onEach {
            epoxyController.setData(it)
        }

        views.nextButton.setText(R.string.create_space)
        views.nextButton.debouncedClicks {
            view.hideKeyboard()
            sharedViewModel.handle(CreateSpaceAction.NextFromDefaultRooms)
        }
    }

    override fun onNameChange(index: Int, newName: String) {
        sharedViewModel.handle(CreateSpaceAction.DefaultRoomNameChanged(index, newName))
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        sharedViewModel.handle(CreateSpaceAction.OnBackPressed)
        return true
    }
}
