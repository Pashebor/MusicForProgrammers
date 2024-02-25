package com.example.musicforprogrammers.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatButton
import com.example.musicforprogrammers.R

enum class MFPButtonType(val type: Int) {
    GREY(0),
    PURPLE(1),
    FUSCHIA(2),
    BLUE(3),
    GREEN(4),
    ORANGE(5),
    YELLOW(6),
}

class MFPButton(
    context: Context,
    attrs: AttributeSet
): AppCompatButton(context, attrs) {
    init {
        val customAttributesValues = context.obtainStyledAttributes(attrs, R.styleable.MFPButton)
        val textColor = when (
            customAttributesValues.getInt(R.styleable.MFPTextView_fontType,1)
        ) {
            MFPButtonType.GREY.type -> R.color.grey
            MFPButtonType.PURPLE.type -> R.color.purple
            MFPButtonType.FUSCHIA.type -> R.color.fuschia
            MFPButtonType.BLUE.type -> R.color.blue
            MFPButtonType.GREEN.type -> R.color.green
            MFPButtonType.ORANGE.type -> R.color.orange
            MFPButtonType.YELLOW.type -> R.color.yellow
            else -> R.color.white
        }

        this.background = null
        this.minHeight = 0
        this.minWidth = 0
        this.isAllCaps = false
        this.minimumWidth = 0
        this.minimumHeight = 0

        this.typeface = resources.getFont(R.font.ibm_medium)
        this.text = resources.getString(R.string.button_brackets).format(this.text)
        this.animation = AnimationUtils.loadAnimation(
            context,
            androidx.appcompat.R.anim.abc_grow_fade_in_from_bottom
        )

        this.setPadding(0, 0, 0, 0)
        this.setTextColor(context.getColor(textColor))
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)

        customAttributesValues.recycle()
    }
}