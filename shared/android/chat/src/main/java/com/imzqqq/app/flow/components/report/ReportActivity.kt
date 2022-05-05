package com.imzqqq.app.flow.components.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.databinding.ActivityReportBinding
import com.imzqqq.app.flow.BottomSheetActivity
import com.imzqqq.app.flow.components.report.adapter.ReportPagerAdapter
import com.imzqqq.app.flow.util.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class ReportActivity : BottomSheetActivity() {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: ReportViewModel by viewModels { flowViewModelFactory }
    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accountId = intent?.getStringExtra(ACCOUNT_ID)
        val accountUserName = intent?.getStringExtra(ACCOUNT_USERNAME)
        if (accountId.isNullOrBlank() || accountUserName.isNullOrBlank()) {
            throw IllegalStateException("accountId ($accountId) or accountUserName ($accountUserName) is null")
        }

        viewModel.init(accountId, accountUserName, intent?.getStringExtra(STATUS_ID))

        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)

        supportActionBar?.apply {
            title = getString(R.string.report_username_format, viewModel.accountUserName)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close_24dp)
        }

        initViewPager()
        if (savedInstanceState == null) {
            viewModel.navigateTo(Screen.Statuses)
        }
        subscribeObservables()
    }

    private fun initViewPager() {
        binding.wizard.isUserInputEnabled = false
        binding.wizard.adapter = ReportPagerAdapter(this)
    }

    private fun subscribeObservables() {
        viewModel.navigation.observe(this) { screen ->
            if (screen != null) {
                viewModel.navigated()
                when (screen) {
                    Screen.Statuses -> showStatusesPage()
                    Screen.Note     -> showNotesPage()
                    Screen.Done     -> showDonePage()
                    Screen.Back     -> showPreviousScreen()
                    Screen.Finish   -> closeScreen()
                }
            }
        }

        viewModel.checkUrl.observe(this) {
            if (!it.isNullOrBlank()) {
                viewModel.urlChecked()
                viewUrl(it)
            }
        }
    }

    private fun showPreviousScreen() {
        when (binding.wizard.currentItem) {
            0 -> closeScreen()
            1 -> showStatusesPage()
        }
    }

    private fun showDonePage() {
        binding.wizard.currentItem = 2
    }

    private fun showNotesPage() {
        binding.wizard.currentItem = 1
    }

    private fun closeScreen() {
        finish()
    }

    private fun showStatusesPage() {
        binding.wizard.currentItem = 0
    }

    companion object {
        private const val ACCOUNT_ID = "account_id"
        private const val ACCOUNT_USERNAME = "account_username"
        private const val STATUS_ID = "status_id"

        @JvmStatic
        fun getIntent(context: Context, accountId: String, userName: String, statusId: String? = null) =
            Intent(context, ReportActivity::class.java)
                .apply {
                    putExtra(ACCOUNT_ID, accountId)
                    putExtra(ACCOUNT_USERNAME, userName)
                    putExtra(STATUS_ID, statusId)
                }
    }
}
