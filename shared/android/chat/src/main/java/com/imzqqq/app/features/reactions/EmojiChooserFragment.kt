package com.imzqqq.app.features.reactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.imzqqq.app.core.extensions.cleanup
import com.imzqqq.app.core.platform.VectorBaseFragment
import com.imzqqq.app.databinding.EmojiChooserFragmentBinding
import javax.inject.Inject

class EmojiChooserFragment @Inject constructor(
        private val emojiRecyclerAdapter: EmojiRecyclerAdapter
) : VectorBaseFragment<EmojiChooserFragmentBinding>(),
        EmojiRecyclerAdapter.InteractionListener,
        ReactionClickListener {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): EmojiChooserFragmentBinding {
        return EmojiChooserFragmentBinding.inflate(inflater, container, false)
    }

    private lateinit var viewModel: EmojiChooserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activityViewModelProvider.get(EmojiChooserViewModel::class.java)
        emojiRecyclerAdapter.reactionClickListener = this
        emojiRecyclerAdapter.interactionListener = this
        views.emojiRecyclerView.adapter = emojiRecyclerAdapter
        viewModel.moveToSection.observe(viewLifecycleOwner) { section ->
            emojiRecyclerAdapter.scrollToSection(section)
        }
        viewModel.emojiData.observe(viewLifecycleOwner) {
            emojiRecyclerAdapter.update(it)
        }
    }

    override fun getCoroutineScope() = lifecycleScope

    override fun firstVisibleSectionChange(section: Int) {
        viewModel.setCurrentSection(section)
    }

    override fun onReactionSelected(reaction: String) {
        viewModel.onReactionSelected(reaction)
    }

    override fun onDestroyView() {
        views.emojiRecyclerView.cleanup()
        emojiRecyclerAdapter.reactionClickListener = null
        emojiRecyclerAdapter.interactionListener = null
        super.onDestroyView()
    }
}
