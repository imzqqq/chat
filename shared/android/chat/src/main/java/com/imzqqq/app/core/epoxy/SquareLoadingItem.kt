package com.imzqqq.app.core.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R

@EpoxyModelClass(layout = R.layout.item_loading_square)
abstract class SquareLoadingItem : VectorEpoxyModel<SquareLoadingItem.Holder>() {

    class Holder : VectorEpoxyHolder()
}
