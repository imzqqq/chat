package com.imzqqq.app.features.crypto.verification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentProgressBinding
import javax.inject.Inject

class QuadSLoadingFragment @Inject constructor() : VectorBaseFragment<FragmentProgressBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProgressBinding {
        return FragmentProgressBinding.inflate(inflater, container, false)
    }
}
