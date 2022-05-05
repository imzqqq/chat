package com.imzqqq.app.core.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R

/**
 * Item of size (0, 0).
 * It can be useful to avoid automatic scroll of RecyclerView with Epoxy controller, when the first valuable item changes.
 */
@EpoxyModelClass(layout = R.layout.item_zero)
abstract class ZeroItem : VectorEpoxyModel<ZeroItem.Holder>() {

    class Holder : VectorEpoxyHolder()
}
