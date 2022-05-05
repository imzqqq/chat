package org.matrix.android.sdk.api

/**
 * Generic callback interface for asynchronously.
 * @param <T> the type of data to return on success
 */
interface MatrixCallback<in T> {

    /**
     * On success method, default to no-op
     * @param data the data successfully returned from the async function
     */
    fun onSuccess(data: T) {
        // no-op
    }

    /**
     * On failure method, default to no-op
     * @param failure the failure data returned from the async function
     */
    fun onFailure(failure: Throwable) {
        // no-op
    }
}

/**
 * Basic no op implementation
 */
class NoOpMatrixCallback<T> : MatrixCallback<T>
