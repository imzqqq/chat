package com.imzqqq.app.core.platform

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun <T> LifecycleOwner.lifecycleAwareLazy(initializer: () -> T): Lazy<T> = LifecycleAwareLazy(this, initializer)

private object UninitializedValue

class LifecycleAwareLazy<out T>(
        private val owner: LifecycleOwner,
        initializer: () -> T
) : Lazy<T>, DefaultLifecycleObserver {

    private var initializer: (() -> T)? = initializer

    private var _value: Any? = UninitializedValue

    @Suppress("UNCHECKED_CAST")
    override val value: T
        @MainThread
        get() {
            if (_value === UninitializedValue) {
                _value = initializer!!()
                attachToLifecycle()
            }
            return _value as T
        }

    override fun onDestroy(owner: LifecycleOwner) {
        _value = UninitializedValue
        detachFromLifecycle()
    }

    private fun attachToLifecycle() {
        if (getLifecycleOwner().lifecycle.currentState == Lifecycle.State.DESTROYED) {
            throw IllegalStateException("Initialization failed because lifecycle has been destroyed!")
        }
        getLifecycleOwner().lifecycle.addObserver(this)
    }

    private fun detachFromLifecycle() {
        getLifecycleOwner().lifecycle.removeObserver(this)
    }

    private fun getLifecycleOwner() = when (owner) {
        is Fragment -> owner.viewLifecycleOwner
        else        -> owner
    }

    override fun isInitialized(): Boolean = _value !== UninitializedValue

    override fun toString(): String = if (isInitialized()) value.toString() else "Lazy value not initialized yet."
}
