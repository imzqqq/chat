package org.matrix.android.sdk.internal.task

internal enum class TaskThread {
    MAIN,
    COMPUTATION,
    IO,
    CALLER,
    CRYPTO,
    DM_VERIF
}
