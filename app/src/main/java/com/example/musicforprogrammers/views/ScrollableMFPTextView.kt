package com.example.musicforprogrammers.views

import android.content.Context
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet

class ScrollableMFPTextView(
    context: Context,
    attr: AttributeSet
): MFPTextView(context, attr) {
    init {
        this.movementMethod = ScrollingMovementMethod()
        this.isSingleLine = true
        this.marqueeRepeatLimit = -1
        this.isSelected = true
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
    }
}