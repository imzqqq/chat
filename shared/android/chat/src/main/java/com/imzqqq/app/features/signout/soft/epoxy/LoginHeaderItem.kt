package com.imzqqq.app.features.signout.soft.epoxy

import com.airbnb.epoxy.EpoxyModelClass
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder
import com.imzqqq.app.core.epoxy.VectorEpoxyModel

@EpoxyModelClass(layout = R.layout.item_login_header)
abstract class LoginHeaderItem : VectorEpoxyModel<LoginHeaderItem.Holder>() {
    class Holder : VectorEpoxyHolder()
}
