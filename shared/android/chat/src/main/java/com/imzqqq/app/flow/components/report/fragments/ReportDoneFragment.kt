package com.imzqqq.app.flow.components.report.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.R
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.flow.components.report.ReportViewModel
import com.imzqqq.app.flow.components.report.Screen
import com.imzqqq.app.databinding.FragmentReportDoneBinding
import com.imzqqq.app.flow.util.Loading
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import com.imzqqq.app.flow.util.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class ReportDoneFragment : Fragment(R.layout.fragment_report_done) {

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: ReportViewModel by activityViewModels { flowViewModelFactory }
    private lateinit var binding: FragmentReportDoneBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReportDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textReported.text = getString(R.string.report_sent_success, viewModel.accountUserName)
        handleClicks()
        subscribeObservables()
    }

    private fun subscribeObservables() {
        viewModel.muteState.observe(viewLifecycleOwner) {
            if (it !is Loading) {
                binding.buttonMute.show()
                binding.progressMute.show()
            } else {
                binding.buttonMute.hide()
                binding.progressMute.hide()
            }

            binding.buttonMute.setText(
                when (it.data) {
                    true -> R.string.action_unmute
                    else -> R.string.action_mute
                }
            )
        }

        viewModel.blockState.observe(viewLifecycleOwner) {
            if (it !is Loading) {
                binding.buttonBlock.show()
                binding.progressBlock.show()
            } else {
                binding.buttonBlock.hide()
                binding.progressBlock.hide()
            }
            binding.buttonBlock.setText(
                when (it.data) {
                    true -> R.string.action_unblock
                    else -> R.string.action_block
                }
            )
        }
    }

    private fun handleClicks() {
        binding.buttonDone.setOnClickListener {
            viewModel.navigateTo(Screen.Finish)
        }
        binding.buttonBlock.setOnClickListener {
            viewModel.toggleBlock()
        }
        binding.buttonMute.setOnClickListener {
            viewModel.toggleMute()
        }
    }

    companion object {
        fun newInstance() = ReportDoneFragment()
    }
}
