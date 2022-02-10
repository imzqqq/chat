package com.imzqqq.app.flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ActivityModalTimelineBinding
import com.imzqqq.app.flow.components.timeline.TimelineFragment
import com.imzqqq.app.flow.components.timeline.TimelineViewModel
import com.imzqqq.app.flow.interfaces.ActionButtonActivity

@AndroidEntryPoint
class ModalTimelineActivity : BottomSheetActivity(), ActionButtonActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityModalTimelineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.title_list_timeline)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            val kind = intent?.getSerializableExtra(ARG_KIND) as? TimelineViewModel.Kind
                ?: TimelineViewModel.Kind.HOME
            val argument = intent?.getStringExtra(ARG_ARG)
            supportFragmentManager.beginTransaction()
                .replace(R.id.contentFrame, TimelineFragment.newInstance(kind, argument))
                .commit()
        }
    }

    override fun getActionButton(): FloatingActionButton? = null

    companion object {
        private const val ARG_KIND = "kind"
        private const val ARG_ARG = "arg"

        @JvmStatic
        fun newIntent(
                context: Context,
                kind: TimelineViewModel.Kind,
                argument: String?
        ): Intent {
            val intent = Intent(context, ModalTimelineActivity::class.java)
            intent.putExtra(ARG_KIND, kind)
            intent.putExtra(ARG_ARG, argument)
            return intent
        }
    }
}
