package com.imzqqq.app.features.spaces.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.activityViewModel
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.platform.OnBackPressed
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.core.resources.StringProvider
import com.imzqqq.app.databinding.FragmentSpaceCreateChoosePrivateModelBinding
import javax.inject.Inject

class ChoosePrivateSpaceTypeFragment @Inject constructor(
        private val stringProvider: StringProvider
) : VectorBaseFragment<FragmentSpaceCreateChoosePrivateModelBinding>(), OnBackPressed {

    private val sharedViewModel: CreateSpaceViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            FragmentSpaceCreateChoosePrivateModelBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.justMeButton.onClick {
            sharedViewModel.handle(CreateSpaceAction.SetSpaceTopology(SpaceTopology.JustMe))
        }

        views.teammatesButton.onClick {
            sharedViewModel.handle(CreateSpaceAction.SetSpaceTopology(SpaceTopology.MeAndTeammates))
        }

        sharedViewModel.onEach { state ->
            views.accessInfoHelpText.text = stringProvider.getString(R.string.create_spaces_make_sure_access, state.name ?: "")
        }
    }

    override fun onBackPressed(toolbarButton: Boolean): Boolean {
        sharedViewModel.handle(CreateSpaceAction.OnBackPressed)
        return true
    }
}
