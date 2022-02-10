package com.imzqqq.app.flow.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.databinding.ItemEditFieldBinding
import com.imzqqq.app.flow.entity.StringField
import com.imzqqq.app.flow.util.BindingHolder

class AccountFieldEditAdapter : RecyclerView.Adapter<BindingHolder<ItemEditFieldBinding>>() {

    private val fieldData = mutableListOf<MutableStringPair>()

    fun setFields(fields: List<StringField>) {
        fieldData.clear()

        fields.forEach { field ->
            fieldData.add(MutableStringPair(field.name, field.value))
        }
        if (fieldData.isEmpty()) {
            fieldData.add(MutableStringPair("", ""))
        }

        notifyDataSetChanged()
    }

    fun getFieldData(): List<StringField> {
        return fieldData.map {
            StringField(it.first, it.second)
        }
    }

    fun addField() {
        fieldData.add(MutableStringPair("", ""))
        notifyItemInserted(fieldData.size - 1)
    }

    override fun getItemCount() = fieldData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemEditFieldBinding> {
        val binding = ItemEditFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemEditFieldBinding>, position: Int) {
        holder.binding.accountFieldName.setText(fieldData[position].first)
        holder.binding.accountFieldValue.setText(fieldData[position].second)

        holder.binding.accountFieldName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(newText: Editable) {
                fieldData[holder.bindingAdapterPosition].first = newText.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        holder.binding.accountFieldValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(newText: Editable) {
                fieldData[holder.bindingAdapterPosition].second = newText.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    class MutableStringPair(var first: String, var second: String)
}
