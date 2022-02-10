package com.imzqqq.app.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentPromptSimplifiedModeBinding
import com.imzqqq.app.features.settings.VectorPreferences
import javax.inject.Inject

class PromptSimplifiedModeFragment @Inject constructor() : VectorBaseFragment<FragmentPromptSimplifiedModeBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPromptSimplifiedModeBinding {
        return FragmentPromptSimplifiedModeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        views.promptSimplifiedModeOn.setOnClickListener { simplifiedModeOn() }
        views.promptSimplifiedModeOff.setOnClickListener { simplifiedModeOff() }
    }

    private fun simplifiedModeOn() {
        VectorPreferences(requireContext()).setSimplifiedMode(true)
        activity?.finish()
    }

    private fun simplifiedModeOff() {
        VectorPreferences(requireContext()).setSimplifiedMode(false)
        activity?.finish()
    }
}
