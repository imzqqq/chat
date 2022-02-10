package org.matrix.android.sdk.api

import org.matrix.android.sdk.api.util.MatrixItem

interface MatrixItemDisplayNameFallbackProvider {
    fun getDefaultName(matrixItem: MatrixItem): String
}
