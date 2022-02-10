package com.imzqqq.app.features.discovery

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.imzqqq.app.R
import com.imzqqq.app.core.epoxy.VectorEpoxyHolder

@EpoxyModelClass(layout = R.layout.item_settings_progress)
abstract class SettingsProgressItem : EpoxyModelWithHolder<SettingsProgressItem.Holder>() {

    class Holder : VectorEpoxyHolder()
}
