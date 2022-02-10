package com.imzqqq.app.core.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R

@EpoxyModelClass(layout = R.layout.item_divider)
abstract class DividerItem : VectorEpoxyModel<DividerItem.Holder>() {
    class Holder : VectorEpoxyHolder()
}
