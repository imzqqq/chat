package com.imzqqq.app.features.signout.hard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySignedOutBinding
import com.imzqqq.app.features.MainActivity
import com.imzqqq.app.features.MainActivityArgs
import org.matrix.android.sdk.api.failure.GlobalError
import timber.log.Timber

/**
 * In this screen, the user is viewing a message informing that he has been logged out
 */
@AndroidEntryPoint
class SignedOutActivity : VectorBaseActivity<ActivitySignedOutBinding>() {

    override fun getBinding() = ActivitySignedOutBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        views.signedOutSubmit.setOnClickListener { submit() }
    }

    private fun submit() {
        // All is already cleared when we are here
        MainActivity.restartApp(this, MainActivityArgs())
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SignedOutActivity::class.java)
        }
    }

    override fun handleInvalidToken(globalError: GlobalError.InvalidToken) {
        // No op here
        Timber.w("Ignoring invalid token global error")
    }
}
