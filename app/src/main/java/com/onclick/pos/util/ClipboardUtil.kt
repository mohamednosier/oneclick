package com.onclick.task.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.onclick.task.R


object ClipboardUtil {

    @JvmStatic
    fun copyToClipBoard(context: Context, txtToCopy: String, label: String) {

        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip: ClipData = ClipData.newPlainText(label, txtToCopy)
        clipboard?.setPrimaryClip(clip) ?: return

        Toast.makeText(
            context,
            context.getString(R.string.copied_successfully, label),
            Toast.LENGTH_SHORT
        ).show()
    }
}