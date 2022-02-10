package im.vector.lib.multipicker.utils

import android.database.Cursor

fun Cursor.getColumnIndexOrNull(column: String): Int? {
    return getColumnIndex(column).takeIf { it != -1 }
}
