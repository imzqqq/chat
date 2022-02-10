package com.imzqqq.app.flow.components.compose.dialog

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemAddPollOptionBinding
import com.imzqqq.app.flow.util.BindingHolder
import com.imzqqq.app.flow.util.onTextChanged
import com.imzqqq.app.flow.util.visible

class AddPollOptionsAdapter(
    private var options: MutableList<String>,
    private val maxOptionLength: Int,
    private val onOptionRemoved: (Boolean) -> Unit,
    private val onOptionChanged: (Boolean) -> Unit
) : RecyclerView.Adapter<BindingHolder<ItemAddPollOptionBinding>>() {

    val pollOptions: List<String>
        get() = options.toList()

    fun addChoice() {
        options.add("")
        notifyItemInserted(options.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemAddPollOptionBinding> {
        val binding = ItemAddPollOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = BindingHolder(binding)
        binding.optionEditText.filters = arrayOf(InputFilter.LengthFilter(maxOptionLength))

        binding.optionEditText.onTextChanged { s, _, _, _ ->
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                options[pos] = s.toString()
                onOptionChanged(validateInput())
            }
        }

        return holder
    }

    override fun getItemCount() = options.size

    override fun onBindViewHolder(holder: BindingHolder<ItemAddPollOptionBinding>, position: Int) {
        holder.binding.optionEditText.setText(options[position])
        holder.binding.optionTextInputLayout.hint = holder.binding.root.context.getString(R.string.poll_new_choice_hint, position + 1)
        holder.binding.deleteButton.visible(position > 1, View.INVISIBLE)
        holder.binding.deleteButton.setOnClickListener {
            holder.binding.optionEditText.clearFocus()
            options.removeAt(holder.bindingAdapterPosition)
            notifyItemRemoved(holder.bindingAdapterPosition)
            onOptionRemoved(validateInput())
        }
    }

    private fun validateInput(): Boolean {
        if (options.contains("") || options.distinct().size != options.size) {
            return false
        }

        return true
    }
}
