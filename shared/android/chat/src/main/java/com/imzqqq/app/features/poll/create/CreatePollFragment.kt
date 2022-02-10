package com.imzqqq.app.features.poll.create

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.configureWith
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.FragmentCreatePollBinding
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class CreatePollArgs(
        val roomId: String,
) : Parcelable

class CreatePollFragment @Inject constructor(
        private val controller: CreatePollController
) : VectorBaseFragment<FragmentCreatePollBinding>(), CreatePollController.Callback {

    private val viewModel: CreatePollViewModel by activityViewModel()

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCreatePollBinding {
        return FragmentCreatePollBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vectorBaseActivity.setSupportActionBar(views.createPollToolbar)

        views.createPollRecyclerView.configureWith(controller, disableItemAnimation = true)
        controller.callback = this

        views.createPollClose.debouncedClicks {
            requireActivity().finish()
        }

        views.createPollButton.debouncedClicks {
            viewModel.handle(CreatePollAction.OnCreatePoll)
        }

        viewModel.onEach(CreatePollViewState::canCreatePoll) { canCreatePoll ->
            views.createPollButton.isEnabled = canCreatePoll
        }

        viewModel.observeViewEvents {
            when (it) {
                CreatePollViewEvents.Success                  -> handleSuccess()
                CreatePollViewEvents.EmptyQuestionError       -> handleEmptyQuestionError()
                is CreatePollViewEvents.NotEnoughOptionsError -> handleNotEnoughOptionsError(it.requiredOptionsCount)
            }
        }
    }

    override fun invalidate() = withState(viewModel) {
        controller.setData(it)
    }

    override fun onQuestionChanged(question: String) {
        viewModel.handle(CreatePollAction.OnQuestionChanged(question))
    }

    override fun onOptionChanged(index: Int, option: String) {
        viewModel.handle(CreatePollAction.OnOptionChanged(index, option))
    }

    override fun onDeleteOption(index: Int) {
        viewModel.handle(CreatePollAction.OnDeleteOption(index))
    }

    override fun onAddOption() {
        viewModel.handle(CreatePollAction.OnAddOption)
        // Scroll to bottom to show "Add Option" button
        views.createPollRecyclerView.apply {
            postDelayed({
                smoothScrollToPosition(adapter?.itemCount?.minus(1) ?: 0)
            }, 100)
        }
    }

    private fun handleSuccess() {
        requireActivity().finish()
    }

    private fun handleEmptyQuestionError() {
        renderToast(getString(R.string.create_poll_empty_question_error))
    }

    private fun handleNotEnoughOptionsError(requiredOptionsCount: Int) {
        renderToast(
                resources.getQuantityString(
                        R.plurals.create_poll_not_enough_options_error,
                        requiredOptionsCount,
                        requiredOptionsCount
                )
        )
    }

    private fun renderToast(message: String) {
        views.createPollToast.removeCallbacks(hideToastRunnable)
        views.createPollToast.text = message
        views.createPollToast.isVisible = true
        views.createPollToast.postDelayed(hideToastRunnable, 2_000)
    }

    private val hideToastRunnable = Runnable {
        views.createPollToast.isVisible = false
    }
}
