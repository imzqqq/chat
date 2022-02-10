package com.imzqqq.app.flow.util

import com.imzqqq.app.flow.entity.Notification
import org.json.JSONArray

/**
 * Serialize to string array and deserialize notifications type
 */
fun serialize(data: Set<Notification.Type>?): String {
    val array = JSONArray()
    data?.forEach {
        array.put(it.presentation)
    }
    return array.toString()
}

fun deserialize(data: String?): Set<Notification.Type> {
    val ret = HashSet<Notification.Type>()
    data?.let {
        val array = JSONArray(data)
        for (i in 0 until array.length()) {
            val item = array.getString(i)
            val type = Notification.Type.byString(item)
            if (type != Notification.Type.UNKNOWN)
                ret.add(type)
        }
    }
    return ret
}
