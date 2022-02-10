package com.imzqqq.app.flow.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imzqqq.app.R
import com.imzqqq.app.databinding.ItemAccountFieldBinding
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.entity.Field
import com.imzqqq.app.flow.entity.IdentityProof
import com.imzqqq.app.flow.interfaces.LinkListener
import com.imzqqq.app.flow.util.BindingHolder
import com.imzqqq.app.flow.util.Either
import com.imzqqq.app.flow.util.LinkHelper
import com.imzqqq.app.flow.util.emojify

class AccountFieldAdapter(
        private val linkListener: LinkListener,
        private val animateEmojis: Boolean
) : RecyclerView.Adapter<BindingHolder<ItemAccountFieldBinding>>() {

    var emojis: List<Emoji> = emptyList()
    var fields: List<Either<IdentityProof, Field>> = emptyList()

    override fun getItemCount() = fields.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<ItemAccountFieldBinding> {
        val binding = ItemAccountFieldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemAccountFieldBinding>, position: Int) {
        val proofOrField = fields[position]
        val nameTextView = holder.binding.accountFieldName
        val valueTextView = holder.binding.accountFieldValue

        if (proofOrField.isLeft()) {
            val identityProof = proofOrField.asLeft()

            nameTextView.text = identityProof.provider
            valueTextView.text = LinkHelper.createClickableText(identityProof.username, identityProof.profileUrl)

            valueTextView.movementMethod = LinkMovementMethod.getInstance()

            valueTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle, 0)
        } else {
            val field = proofOrField.asRight()
            val emojifiedName = field.name.emojify(emojis, nameTextView, animateEmojis)
            nameTextView.text = emojifiedName

            val emojifiedValue = field.value.emojify(emojis, valueTextView, animateEmojis)
            LinkHelper.setClickableText(valueTextView, emojifiedValue, null, linkListener)

            if (field.verifiedAt != null) {
                valueTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle, 0)
            } else {
                valueTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            }
        }
    }
}
