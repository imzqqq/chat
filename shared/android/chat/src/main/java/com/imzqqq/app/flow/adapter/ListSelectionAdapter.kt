package com.imzqqq.app.flow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemPickerListBinding
import com.imzqqq.app.flow.entity.MastoList

class ListSelectionAdapter(context: Context) : ArrayAdapter<MastoList>(context, R.layout.item_picker_list) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding = if (convertView == null) {
            ItemPickerListBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemPickerListBinding.bind(convertView)
        }

        getItem(position)?.let { list ->
            binding.root.text = list.title
        }

        return binding.root
    }
}
