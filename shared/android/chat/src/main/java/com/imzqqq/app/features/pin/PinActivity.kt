package com.imzqqq.app.features.pin

import android.content.Context
import android.content.Intent
import com.airbnb.mvrx.Mavericks
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding

@AndroidEntryPoint
class PinActivity : VectorBaseActivity<ActivitySimpleBinding>(), ToolbarConfigurable, UnlockedActivity {

    companion object {
        fun newIntent(context: Context, args: PinArgs): Intent {
            return Intent(context, PinActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, args)
            }
        }
    }

    override fun getBinding() = ActivitySimpleBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    override fun initUiAndData() {
        if (isFirstCreation()) {
            val fragmentArgs: PinArgs = intent?.extras?.getParcelable(Mavericks.KEY_ARG) ?: return
            addFragment(R.id.simpleFragmentContainer, PinFragment::class.java, fragmentArgs)
        }
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }
}
