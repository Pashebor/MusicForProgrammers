package com.example.musicforprogrammers.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.musicforprogrammers.R

enum class MFPFontType(val type: Int) {
    REGULAR(0),
    ITALIC(1)
}

open class MFPTextView(
    context: Context,
    attr: AttributeSet
    ) : AppCompatTextView(context, attr)
{
    init {
        val customAttributesValues = context.obtainStyledAttributes(attr, R.styleable.MFPTextView)

        when (
            customAttributesValues.getInt(R.styleable.MFPTextView_fontType,1)
        ) {
            MFPFontType.REGULAR.type ->
                this.typeface = resources.getFont(R.font.ibm_medium)
            MFPFontType.ITALIC.type ->
                this.typeface = resources.getFont(R.font.ibm_medium_italic)
            else ->
                this.typeface = resources.getFont(R.font.ibm_medium)
        }

        customAttributesValues.recycle()
    }
}