package com.imzqqq.app.features.spaces

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.commitTransaction
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.spaces.preview.SpacePreviewArgs
import com.imzqqq.app.features.spaces.preview.SpacePreviewFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SpacePreviewActivity : VectorBaseActivity<ActivitySimpleBinding>() {

    lateinit var sharedActionViewModel: SpacePreviewSharedActionViewModel

    override fun getBinding(): ActivitySimpleBinding = ActivitySimpleBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedActionViewModel = viewModelProvider.get(SpacePreviewSharedActionViewModel::class.java)
        sharedActionViewModel
                .stream()
                .onEach { action ->
                    when (action) {
                        SpacePreviewSharedAction.DismissAction -> finish()
                        SpacePreviewSharedAction.ShowModalLoading -> showWaitingView()
                        SpacePreviewSharedAction.HideModalLoading -> hideWaitingView()
                        is SpacePreviewSharedAction.ShowErrorMessage -> action.error?.let { showSnackbar(it) }
                    }
                }
                .launchIn(lifecycleScope)

        if (isFirstCreation()) {
            val simpleName = SpacePreviewFragment::class.java.simpleName
            val args = intent?.getParcelableExtra<SpacePreviewArgs>(Mavericks.KEY_ARG)
            if (supportFragmentManager.findFragmentByTag(simpleName) == null) {
                supportFragmentManager.commitTransaction {
                    replace(R.id.simpleFragmentContainer,
                            SpacePreviewFragment::class.java,
                            Bundle().apply { this.putParcelable(Mavericks.KEY_ARG, args) },
                            simpleName
                    )
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context, spaceIdOrAlias: String): Intent {
            return Intent(context, SpacePreviewActivity::class.java).apply {
                putExtra(Mavericks.KEY_ARG, SpacePreviewArgs(spaceIdOrAlias))
            }
        }
    }
}
