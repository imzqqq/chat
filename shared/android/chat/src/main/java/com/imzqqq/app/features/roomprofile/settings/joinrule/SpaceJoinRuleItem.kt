package com.imzqqq.app.features.roomprofile.settings.joinrule

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.ClickListener
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel
import com.imzqqq.app.core.epoxy.onClick
import com.imzqqq.app.core.extensions.setAttributeTintedImageResource
import com.imzqqq.app.core.utils.DebouncedClickListener
import com.imzqqq.app.features.home.AvatarRenderer
import org.matrix.android.sdk.api.util.MatrixItem

@EpoxyModelClass(layout = R.layout.item_bottom_sheet_joinrule_restricted)
abstract class SpaceJoinRuleItem : VectorEpoxyModel<SpaceJoinRuleItem.Holder>() {

    @EpoxyAttribute
    var selected: Boolean = false

    @EpoxyAttribute
    var needUpgrade: Boolean = false

    @EpoxyAttribute
    lateinit var avatarRenderer: AvatarRenderer

    @EpoxyAttribute
    var restrictedList: List<MatrixItem> = emptyList()

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    lateinit var listener: ClickListener

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.view.onClick(listener)
        holder.upgradeRequiredButton.setOnClickListener(DebouncedClickListener(listener))

        if (selected) {
            holder.radioImage.setAttributeTintedImageResource(R.drawable.ic_radio_on, R.attr.colorPrimary)
            holder.radioImage.contentDescription = holder.view.context.getString(R.string.a11y_checked)
        } else {
            holder.radioImage.setImageDrawable(ContextCompat.getDrawable(holder.view.context, R.drawable.ic_radio_off))
            holder.radioImage.contentDescription = holder.view.context.getString(R.string.a11y_unchecked)
        }

        holder.upgradeRequiredButton.isVisible = needUpgrade
        holder.helperText.isVisible = selected

        val items = listOf(holder.space1, holder.space2, holder.space3, holder.space4, holder.space5)
        holder.spaceMore.isVisible = false
        items.onEach { it.isVisible = false }
        if (!needUpgrade) {
            if (restrictedList.isEmpty()) {
                holder.listTitle.isVisible = false
            } else {
                holder.listTitle.isVisible = true
                restrictedList.forEachIndexed { index, matrixItem ->
                    if (index < items.size) {
                        items[index].isVisible = true
                        avatarRenderer.render(matrixItem, items[index])
                    } else if (index == items.size) {
                        holder.spaceMore.isVisible = true
                    }
                }
            }
        } else {
            holder.listTitle.isVisible = false
            holder.helperText.isVisible = false
        }
    }

    class Holder : VectorEpoxyHolder() {
        val radioImage by bind<ImageView>(R.id.radioIcon)
        val actionTitle by bind<TextView>(R.id.actionTitle)
        val actionDescription by bind<TextView>(R.id.actionDescription)
        val upgradeRequiredButton by bind<Button>(R.id.upgradeRequiredButton)
        val listTitle by bind<TextView>(R.id.listTitle)
        val space1 by bind<ImageView>(R.id.rest1)
        val space2 by bind<ImageView>(R.id.rest2)
        val space3 by bind<ImageView>(R.id.rest3)
        val space4 by bind<ImageView>(R.id.rest4)
        val space5 by bind<ImageView>(R.id.rest5)
        val spaceMore by bind<ImageView>(R.id.rest6)
        val helperText by bind<TextView>(R.id.helperText)
    }
}
