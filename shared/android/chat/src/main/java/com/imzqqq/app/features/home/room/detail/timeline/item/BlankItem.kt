package com.imzqqq.app.features.home.room.detail.timeline.item

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_timeline_event_blank_stub)
abstract class BlankItem : VectorEpoxyModel<BlankItem.BlankHolder>() {
    class BlankHolder : VectorEpoxyHolder()
}
