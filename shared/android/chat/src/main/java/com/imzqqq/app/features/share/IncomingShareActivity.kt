package com.imzqqq.app.features.share

import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding

@AndroidEntryPoint
class IncomingShareActivity : VectorBaseActivity<ActivitySimpleBinding>(), ToolbarConfigurable {

    override fun getBinding() = ActivitySimpleBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    override fun initUiAndData() {
        if (isFirstCreation()) {
            addFragment(R.id.simpleFragmentContainer, IncomingShareFragment::class.java)
        }
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar, displayBack = false)
    }
}
