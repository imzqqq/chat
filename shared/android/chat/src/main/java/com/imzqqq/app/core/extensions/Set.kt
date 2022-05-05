package com.imzqqq.app.core.extensions

// Create a new Set including the provided element if not already present, or removing the element if already present
fun <T> Set<T>.toggle(element: T, singleElement: Boolean = false): Set<T> {
    return if (contains(element)) {
        if (singleElement) {
            emptySet()
        } else {
            minus(element)
        }
    } else {
        if (singleElement) {
            setOf(element)
        } else {
            plus(element)
        }
    }
}
