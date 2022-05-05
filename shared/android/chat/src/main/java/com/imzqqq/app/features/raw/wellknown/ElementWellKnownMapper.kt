package com.imzqqq.app.features.raw.wellknown

import com.squareup.moshi.JsonAdapter
import org.matrix.android.sdk.api.extensions.tryOrNull
import org.matrix.android.sdk.internal.di.MoshiProvider

object ElementWellKnownMapper {

    val adapter: JsonAdapter<ElementWellKnown> = MoshiProvider.providesMoshi().adapter(ElementWellKnown::class.java)

    fun from(value: String): ElementWellKnown? {
        return tryOrNull("Unable to parse well-known data") { adapter.fromJson(value) }
    }
}
