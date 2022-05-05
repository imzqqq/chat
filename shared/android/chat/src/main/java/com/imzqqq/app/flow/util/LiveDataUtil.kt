@file:Suppress("UNCHECKED_CAST")

package com.imzqqq.app.flow.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

inline fun <X, Y> LiveData<X>.map(crossinline mapFunction: (X) -> Y): LiveData<Y> =
    Transformations.map(this) { input -> mapFunction(input) }

inline fun <X, Y> LiveData<X>.switchMap(
    crossinline switchMapFunction: (X) -> LiveData<Y>
): LiveData<Y> = Transformations.switchMap(this) { input -> switchMapFunction(input) }

inline fun <X> LiveData<X>.filter(crossinline predicate: (X) -> Boolean): LiveData<X> {
    val liveData = MediatorLiveData<X>()
    liveData.addSource(this) { value ->
        if (predicate(value)) {
            liveData.value = value
        }
    }
    return liveData
}

fun LifecycleOwner.withLifecycleContext(body: LifecycleContext.() -> Unit) =
    LifecycleContext(this).apply(body)

class LifecycleContext(val lifecycleOwner: LifecycleOwner) {
    inline fun <T> LiveData<T>.observe(crossinline observer: (T) -> Unit) =
        this.observe(lifecycleOwner, Observer { observer(it) })

    /**
     * Just hold a subscription,
     */
    fun <T> LiveData<T>.subscribe() =
        this.observe(lifecycleOwner, Observer { })
}

/**
 * Invokes @param [combiner] when value of both @param [a] and @param [b] are not null. Returns
 * [LiveData] with value set to the result of calling [combiner] with value of both.
 * Important! You still need to observe to the returned [LiveData] for [combiner] to be invoked.
 */
fun <A, B, R> combineLiveData(a: LiveData<A>, b: LiveData<B>, combiner: (A, B) -> R): LiveData<R> {
    val liveData = MediatorLiveData<R>()
    liveData.addSource(a) {
        if (a.value != null && b.value != null) {
            liveData.value = combiner(a.value!!, b.value!!)
        }
    }
    liveData.addSource(b) {
        if (a.value != null && b.value != null) {
            liveData.value = combiner(a.value!!, b.value!!)
        }
    }
    return liveData
}

/**
 * Returns [LiveData] with value set to the result of calling [combiner] with value of [a] and [b]
 * after either changes. Doesn't check if either has value.
 * Important! You still need to observe to the returned [LiveData] for [combiner] to be invoked.
 */
fun <A, B, R> combineOptionalLiveData(a: LiveData<A>, b: LiveData<B>, combiner: (A?, B?) -> R): LiveData<R> {
    val liveData = MediatorLiveData<R>()
    liveData.addSource(a) {
        liveData.value = combiner(a.value, b.value)
    }
    liveData.addSource(b) {
        liveData.value = combiner(a.value, b.value)
    }
    return liveData
}

fun <T:Any> Single<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this.toFlowable())

fun <T:Any> Observable<T>.toLiveData(
    @Suppress("UNUSED_PARAMETER")
    backpressureStrategy: BackpressureStrategy = BackpressureStrategy.LATEST
) = LiveDataReactiveStreams.fromPublisher(this.toFlowable(BackpressureStrategy.LATEST))
