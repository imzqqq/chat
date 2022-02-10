package com.imzqqq.app.core.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R

@EpoxyModelClass(layout = R.layout.item_divider_on_surface)
abstract class BottomSheetDividerItem : VectorEpoxyModel<BottomSheetDividerItem.Holder>() {
    class Holder : VectorEpoxyHolder()
}
