package com.imzqqq.app.features.login

import android.content.Context
import android.content.Intent
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivityLoginBinding
import com.imzqqq.app.features.pin.UnlockedActivity
import com.imzqqq.app.features.settings.VectorPreferences

@AndroidEntryPoint
open class PromptSimplifiedModeActivity : VectorBaseActivity<ActivityLoginBinding>(), ToolbarConfigurable, UnlockedActivity {

    override fun getBinding() = ActivityLoginBinding.inflate(layoutInflater)

    override fun initUiAndData() {
        addFragment(R.id.loginFragmentContainer, PromptSimplifiedModeFragment::class.java)
    }

    companion object {
        fun showIfRequired(context: Context, vectorPreferences: VectorPreferences) {
            if (vectorPreferences.needsSimplifiedModePrompt()) {
                context.startActivity(newIntent(context))
            }
        }
        fun newIntent(context: Context): Intent {
            return Intent(context, PromptSimplifiedModeActivity::class.java)
        }
    }

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }

    override fun onBackPressed() {
        // Don't call super - we don't want to quit on back press, user should select a mode
    }
}
