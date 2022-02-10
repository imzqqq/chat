package com.imzqqq.app.features.displayname

import org.matrix.android.sdk.api.MatrixItemDisplayNameFallbackProvider
import org.matrix.android.sdk.api.util.MatrixItem

// Used to provide the fallback to the MatrixSDK, in the MatrixConfiguration
object VectorMatrixItemDisplayNameFallbackProvider : MatrixItemDisplayNameFallbackProvider {
    override fun getDefaultName(matrixItem: MatrixItem): String {
        // Can customize something from the id if necessary here
        return matrixItem.id
    }
}
