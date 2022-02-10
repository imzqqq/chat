package com.imzqqq.app.features.spaces.manage

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_room_to_add_in_space_placeholder)
abstract class RoomSelectionPlaceHolderItem : VectorEpoxyModel<RoomSelectionPlaceHolderItem.Holder>() {
    class Holder : VectorEpoxyHolder()
}
