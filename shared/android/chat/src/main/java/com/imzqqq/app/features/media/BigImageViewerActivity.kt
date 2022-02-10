package com.imzqqq.app.features.media

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.core.di.ActiveSessionHolder
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivityBigImageViewerBinding
import javax.inject.Inject

/**
 * Simple Activity to display an avatar in fullscreen
 */
@AndroidEntryPoint
class BigImageViewerActivity : VectorBaseActivity<ActivityBigImageViewerBinding>() {
    @Inject lateinit var sessionHolder: ActiveSessionHolder

    override fun getBinding() = ActivityBigImageViewerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(views.bigImageViewerToolbar)
        supportActionBar?.apply {
            title = intent.getStringExtra(EXTRA_TITLE)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val uri = sessionHolder.getSafeActiveSession()
                ?.contentUrlResolver()
                ?.resolveFullSize(intent.getStringExtra(EXTRA_IMAGE_URL))
                ?.toUri()

        if (uri == null) {
            finish()
        } else {
            views.bigImageViewerImageView.showImage(uri)
        }
    }

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL"

        fun newIntent(context: Context, title: String?, imageUrl: String): Intent {
            return Intent(context, BigImageViewerActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_IMAGE_URL, imageUrl)
            }
        }
    }
}
