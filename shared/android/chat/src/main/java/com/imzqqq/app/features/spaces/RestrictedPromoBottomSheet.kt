package com.imzqqq.app.features.spaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.imzqqq.app.R
import com.imzqqq.app.core.platform.VectorBaseBottomSheetDialogFragment
import com.imzqqq.app.databinding.BottomSheetSpaceAdvertiseRestrictedBinding

class RestrictedPromoBottomSheet : VectorBaseBottomSheetDialogFragment<BottomSheetSpaceAdvertiseRestrictedBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?) =
            BottomSheetSpaceAdvertiseRestrictedBinding.inflate(inflater, container, false)

    override val showExpanded = true

    var learnMoreMode: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render()
        views.skipButton.debouncedClicks {
            dismiss()
        }

        views.learnMore.debouncedClicks {
            if (learnMoreMode) {
                dismiss()
            } else {
                learnMoreMode = true
                render()
            }
        }
    }

    private fun render() {
        if (learnMoreMode) {
            views.title.text = getString(R.string.new_let_people_in_spaces_find_and_join)
            views.topDescription.text = getString(R.string.to_help_space_members_find_and_join)
            views.imageHint.isVisible = true
            views.bottomDescription.isVisible = true
            views.bottomDescription.text = getString(R.string.this_makes_it_easy_for_rooms_to_stay_private_to_a_space)
            views.skipButton.isVisible = false
            views.learnMore.text = getString(R.string.ok)
        } else {
            views.title.text = getString(R.string.help_space_members)
            views.topDescription.text = getString(R.string.help_people_in_spaces_find_and_join)
            views.imageHint.isVisible = false
            views.bottomDescription.isVisible = false
            views.skipButton.isVisible = true
            views.learnMore.text = getString(R.string.learn_more)
        }
    }
}
