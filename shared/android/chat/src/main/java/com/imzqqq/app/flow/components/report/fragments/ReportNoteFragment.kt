package com.imzqqq.app.flow.components.report.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.components.report.ReportViewModel
import com.imzqqq.app.flow.components.report.Screen
import com.imzqqq.app.databinding.FragmentReportNoteBinding
import com.imzqqq.app.flow.util.*
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ReportNoteFragment : Fragment(R.layout.fragment_report_note) {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: ReportViewModel by activityViewModels { flowViewModelFactory }
    private lateinit var binding: FragmentReportNoteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillViews()
        handleChanges()
        handleClicks()
        subscribeObservables()
    }

    private fun handleChanges() {
        binding.editNote.doAfterTextChanged {
            viewModel.reportNote = it?.toString() ?: ""
        }
        binding.checkIsNotifyRemote.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isRemoteNotify = isChecked
        }
    }

    private fun fillViews() {
        binding.editNote.setText(viewModel.reportNote)

        if (viewModel.isRemoteAccount) {
            binding.checkIsNotifyRemote.show()
            binding.reportDescriptionRemoteInstance.show()
        } else {
            binding.checkIsNotifyRemote.hide()
            binding.reportDescriptionRemoteInstance.hide()
        }

        if (viewModel.isRemoteAccount)
            binding.checkIsNotifyRemote.text = getString(R.string.report_remote_instance, viewModel.remoteServer)
        binding.checkIsNotifyRemote.isChecked = viewModel.isRemoteNotify
    }

    private fun subscribeObservables() {
        viewModel.reportingState.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> viewModel.navigateTo(Screen.Done)
                is Loading -> showLoading()
                is Error   -> showError(it.cause)
            }
        }
    }

    private fun showError(error: Throwable?) {
        binding.editNote.isEnabled = true
        binding.checkIsNotifyRemote.isEnabled = true
        binding.buttonReport.isEnabled = true
        binding.buttonBack.isEnabled = true
        binding.progressBar.hide()

        Snackbar.make(binding.buttonBack, if (error is IOException) R.string.error_network else R.string.error_generic, Snackbar.LENGTH_LONG)
            .setAction(R.string.action_retry) {
                sendReport()
            }
            .show()
    }

    private fun sendReport() {
        viewModel.doReport()
    }

    private fun showLoading() {
        binding.buttonReport.isEnabled = false
        binding.buttonBack.isEnabled = false
        binding.editNote.isEnabled = false
        binding.checkIsNotifyRemote.isEnabled = false
        binding.progressBar.show()
    }

    private fun handleClicks() {
        binding.buttonBack.setOnClickListener {
            viewModel.navigateTo(Screen.Back)
        }

        binding.buttonReport.setOnClickListener {
            sendReport()
        }
    }

    companion object {
        fun newInstance() = ReportNoteFragment()
    }
}
