package com.imzqqq.app.features.roomdirectory.createroom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.addFragment
import com.imzqqq.app.core.platform.ToolbarConfigurable
import com.imzqqq.app.core.platform.VectorBaseActivity
import com.imzqqq.app.databinding.ActivitySimpleBinding
import com.imzqqq.app.features.roomdirectory.RoomDirectorySharedAction
import com.imzqqq.app.features.roomdirectory.RoomDirectorySharedActionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Simple container for [CreateRoomFragment]
 */
@AndroidEntryPoint
class CreateRoomActivity : VectorBaseActivity<ActivitySimpleBinding>(), ToolbarConfigurable {

    private lateinit var sharedActionViewModel: RoomDirectorySharedActionViewModel

    override fun getBinding() = ActivitySimpleBinding.inflate(layoutInflater)

    override fun getCoordinatorLayout() = views.coordinatorLayout

    override fun configure(toolbar: MaterialToolbar) {
        configureToolbar(toolbar)
    }

    override fun initUiAndData() {
        if (isFirstCreation()) {
            addFragment(
                    R.id.simpleFragmentContainer,
                    CreateRoomFragment::class.java,
                    CreateRoomArgs(
                            intent?.getStringExtra(INITIAL_NAME) ?: "",
                            isSpace = intent?.getBooleanExtra(IS_SPACE, false) ?: false
                    )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedActionViewModel = viewModelProvider.get(RoomDirectorySharedActionViewModel::class.java)
        sharedActionViewModel
                .stream()
                .onEach { sharedAction ->
                    when (sharedAction) {
                        is RoomDirectorySharedAction.Back,
                        is RoomDirectorySharedAction.Close -> finish()
                    }
                }
                .launchIn(lifecycleScope)
    }

    companion object {
        private const val INITIAL_NAME = "INITIAL_NAME"
        private const val IS_SPACE = "IS_SPACE"

        fun getIntent(context: Context, initialName: String = "", isSpace: Boolean = false): Intent {
            return Intent(context, CreateRoomActivity::class.java).apply {
                putExtra(INITIAL_NAME, initialName)
                putExtra(IS_SPACE, isSpace)
            }
        }
    }
}
