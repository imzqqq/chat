@file:JvmName("ListUtils")

package com.imzqqq.app.flow.util

import java.util.ArrayList
import java.util.LinkedHashSet

/**
 * @return true if list is null or else return list.isEmpty()
 */
fun isEmpty(list: List<*>?): Boolean {
    return list == null || list.isEmpty()
}

/**
 * @return a new ArrayList containing the elements without duplicates in the same order
 */
fun <T> removeDuplicates(list: List<T>): ArrayList<T> {
    val set = LinkedHashSet(list)
    return ArrayList(set)
}

inline fun <T> List<T>.withoutFirstWhich(predicate: (T) -> Boolean): List<T> {
    val newList = toMutableList()
    val index = newList.indexOfFirst(predicate)
    if (index != -1) {
        newList.removeAt(index)
    }
    return newList
}

inline fun <T> List<T>.replacedFirstWhich(replacement: T, predicate: (T) -> Boolean): List<T> {
    val newList = toMutableList()
    val index = newList.indexOfFirst(predicate)
    if (index != -1) {
        newList[index] = replacement
    }
    return newList
}

inline fun <reified R> Iterable<*>.firstIsInstanceOrNull(): R? {
    return firstOrNull { it is R }?.let { it as R }
}
