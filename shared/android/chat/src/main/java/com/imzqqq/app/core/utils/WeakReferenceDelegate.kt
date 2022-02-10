package com.imzqqq.app.core.utils

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

fun <T> weak(value: T) = WeakReferenceDelegate(value)

class WeakReferenceDelegate<T>(value: T) {

    private var weakReference: WeakReference<T> = WeakReference(value)

    operator fun getValue(thisRef: Any, property: KProperty<*>): T? = weakReference.get()
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        weakReference = WeakReference(value)
    }
}
