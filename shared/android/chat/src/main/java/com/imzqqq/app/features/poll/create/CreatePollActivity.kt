package com.imzqqq.app.features.poll.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.SimpleFragmentActivity

@AndroidEntryPoint
class CreatePollActivity : SimpleFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        views.toolbar.visibility = View.GONE

        val createPollArgs: CreatePollArgs? = intent?.extras?.getParcelable(EXTRA_CREATE_POLL_ARGS)

        if (isFirstCreation()) {
            addFragment(
                    R.id.container,
                    CreatePollFragment::class.java,
                    createPollArgs
            )
        }
    }

    companion object {

        private const val EXTRA_CREATE_POLL_ARGS = "EXTRA_CREATE_POLL_ARGS"

        fun getIntent(context: Context, createPollArgs: CreatePollArgs): Intent {
            return Intent(context, CreatePollActivity::class.java).apply {
                putExtra(EXTRA_CREATE_POLL_ARGS, createPollArgs)
            }
        }
    }
}
